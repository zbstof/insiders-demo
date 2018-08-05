package services

import java.io.InputStream

import javax.inject.{Inject, Singleton}
import play.api.libs.json
import play.api.libs.json._
import services.Control.using

import scala.collection.mutable
import scala.concurrent.ExecutionContext
import scala.io.Source

@Singleton
class AmazonMappingService @Inject()(val elasticService: ElasticService)(implicit val ec: ExecutionContext) {

  private val boostingsFileName = "boostings.txt"
  private var keywordsByIds = scala.collection.mutable.Map[Int, mutable.Set[String]]()

  val linesIterator: Iterator[String] = Source.fromFile(boostingsFileName).getLines

  for (l <- linesIterator) {
    val keyValues: Array[String] = l.split("=")
    val ids: Array[Int] = keyValues(1).split(",").map(_.trim.toInt)
    val key: String = keyValues(0).trim

    for (id <- ids) keywordsByIds.getOrElseUpdate(id, mutable.Set[String](key)) += key
  }

  private val jsonArrayKeywordsById: mutable.Map[Int, JsValue] = for ((k, v) <- keywordsByIds) yield (k, Json.toJson(v))

  def importFeed = {
    val stream: InputStream = getClass.getResourceAsStream("/amazondata_Electronics_14200.txt")
    val dataFeed = using(scala.io.Source.fromInputStream(stream)) { source => {
        toJson(source.mkString)
      }
    }
    val ndjson = dataFeed.value
//      .map(element => (element))
      .foldLeft[String]("")((ndjson, entry) => ndjson + s"""{ "index" : { "_index" : "data", "_type" : "_doc", "_id" : "${Json.stringify((entry \ "name") get)}" } }"""
      + "\n" +Json.stringify(entry) + "\n")
    elasticService.bulkUpload(ndjson).map(ignored =>
      elasticService.putPromotedListings(jsonArrayKeywordsById))
  }


  def toJson(data: String, default: JsObject = JsObject.empty): JsArray = {
    val items = data.split("(\n\n)*ITEM ")
    JsArray(items.map(item => item.split("\n"))
      .filter(lines => lines.head matches """\d+""")
      .map(lines => {
        val id = lines.head.toInt
        val itemLines = lines.splitAt(1)._2
        val fields: Seq[(String, JsValue)] = itemLines.map(line => line.split("=", 2))
          .filter(_.length == 2)
          .map(field => field(0) -> json.JsString(field(1)))
          .groupBy(_._1)
          .mapValues(entries => {
            val values = entries.map(_._2)
            if (values.length == 1)
              values(0)
            else
              JsArray(values)
          }).toSeq
        default ++ json.JsObject(("name" -> JsNumber(id)) +: fields)
      }))
  }
}
