package services

import java.io.InputStream

import javax.inject.{Inject, Singleton}
import play.api.libs.json
import play.api.libs.json._
import play.api.libs.ws.WSResponse
import services.Control.using

import scala.concurrent.Future

@Singleton
class AmazonMappingService @Inject()(val elasticService: ElasticService) {

  def importFeed: Future[WSResponse] =
  {
    val stream: InputStream = getClass.getResourceAsStream("/amazondata_Electronics_14200.txt")
    val dataFeed = using(scala.io.Source.fromInputStream(stream)) { source => {
      toJson(source.mkString)
    }
    }
    elasticService.bulkUpload(dataFeed.value)
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
        default ++ json.JsObject(("_id" -> JsNumber(id)) +: fields)
      }))
  }
}
