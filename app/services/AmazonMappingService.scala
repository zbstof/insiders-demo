package services

import play.api.libs.json
import play.api.libs.json._

class AmazonMappingService {
  def toJson(data: String, default: JsObject = JsObject.empty): JsValue = {
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
