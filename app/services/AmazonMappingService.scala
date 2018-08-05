package services

import java.io.InputStream

import javax.inject.{Inject, Singleton}
import play.api.libs.json
import play.api.libs.json._
import services.Control.using

@Singleton
class AmazonMappingService @Inject()(val elasticService: ElasticService) {
  private val stream: InputStream = this.getClass.getResourceAsStream("amazondata_Electronics_14200.txt")
  private val dataFeed = using(scala.io.Source.fromInputStream(stream)) { source => {
    toJson(source.mkString)
  }
  }
  elasticService.bulkUpload(dataFeed.value)

  def toJson(data: String, default: JsObject = JsObject.empty): JsArray = {
    val items = data.split("\n\n")
    JsArray(items.map(item => item.split("\n"))
      .map(lines => {
        val id = lines.head.split(" ", 2)(1).toInt
        val itemLines = lines.splitAt(1)._2
        val fields: Seq[(String, JsValue)] = itemLines.map(line => line.split("=", 2))
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
