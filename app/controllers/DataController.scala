package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.libs.ws.WSResponse
import play.api.mvc._
import services.{AmazonMappingService, ElasticService}

import scala.concurrent.ExecutionContext

@Singleton
class DataController @Inject()(val cc: ControllerComponents,
                               val elastic: ElasticService,
                               val mappingService: AmazonMappingService)(implicit val ec: ExecutionContext) extends AbstractController(cc) {
  mappingService.importFeed

  def importFeed: Action[AnyContent] = Action.async {
    mappingService.importFeed.map(ignored => Ok)
  }

  def search: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val queryPattern: String = request.getQueryString("pattern") match {
      case Some(pattern) => pattern
      case None => throw new IllegalArgumentException(s"missing required query parameter 'pattern', got ${request.rawQueryString}")
    }
    elastic.search(queryPattern)
      .map((res: WSResponse) => {

        val categoriesBuckets: JsLookupResult = res.body[JsValue] \ "aggregations" \ "by_Binding" \ "buckets"
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
            val name: JsValue = e \ "_source" \ "Title" get

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
}
