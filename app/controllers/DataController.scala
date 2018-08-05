package controllers

import java.io.FileInputStream

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.libs.ws.WSResponse
import play.api.libs.{Files, json}
import play.api.mvc._
import services.ElasticService

import scala.concurrent.ExecutionContext

@Singleton
class DataController @Inject()(val cc: ControllerComponents,
                               val elastic: ElasticService)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  def search: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val queryPattern: String = request.getQueryString("pattern") match {
      case Some(pattern) => pattern
      case None => throw new IllegalArgumentException(s"missing required query parameter 'pattern', got ${request.rawQueryString}")
    }
    elastic.search(queryPattern)
      .map((res: WSResponse) => {

        val categoriesBuckets: JsLookupResult = res.body[JsValue] \ "aggregations" \ "by_city" \ "buckets"
        val categories: IndexedSeq[JsObject] = categoriesBuckets.get
          .as[JsArray]
          .value
          .map(e => {
            val name: JsValue = e \ "key" get
            val hits: JsValue = e \ "doc_count" get

            JsObject(Seq("name" -> name, "hits" -> hits))
          })

        val hits: JsLookupResult = res.body[JsValue] \ "hits" \ "hits"
        val items: IndexedSeq[JsObject] = hits.get
          .as[JsArray]
          .value
          .map(e => {
            val name: JsValue = e \ "_source" \ "firstname" get

            JsObject(Seq("name" -> name))
          })

        val categoriesArray = JsArray(categories)
        val itemsArray = JsArray(items)

        Ok(JsObject(Seq("categories" -> categoriesArray, "items" -> itemsArray)))
      })
  }

  def schema: Action[AnyContent] = Action.async {
    elastic.getSchema.map((res: JsValue) => Ok(res))
  }

  def bulkUpload: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val body: AnyContent = request.body
    val entries: IndexedSeq[JsValue] = body.asJson.map((data: JsValue) => data.asInstanceOf[JsArray].value).getOrElse(
      throw new IllegalArgumentException("Body: " + body)
    )

    elastic.bulkUpload(entries)
      .map((response: WSResponse) => Status(response.status)(Json.parse(response.body)))
  }

  def fileUpload: Action[MultipartFormData[Files.TemporaryFile]] = Action.async(parse.multipartFormData) { request =>
    val stream = new FileInputStream(request.body.files.head.ref.getAbsoluteFile)
    val json = try {
      Json.parse(stream)
    } finally {
      stream.close()
    }
    val entries = json.asInstanceOf[JsArray].value
    elastic.bulkUpload(entries)
      .map(response => Status(response.status)(Json.parse(response.body)))
  }
}
