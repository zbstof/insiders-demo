package services

import java.nio.charset.StandardCharsets

import akka.util.ByteString
import javax.inject.{Inject, Singleton}
import play.api.http.HttpVerbs
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.libs.ws.ahc.AhcCurlRequestLogger
import play.api.libs.ws.{InMemoryBody, WSClient, WSResponse}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ElasticService @Inject()(val ws: WSClient, val boostings: BoostingService)(implicit val ec: ExecutionContext) {

  // we are using one mapping and data type per instance of app
  private val esDocRoot: String = "http://localhost:9200/data/_doc"
  private val esDocRoot2: String = "http://localhost:9200/bank/account"

  def getSchema: Future[JsValue] = {
    ws.url(s"$esDocRoot/_mapping")
      .withMethod(HttpVerbs.GET)
      .execute()
      .map(transformMappingToSimpleSchema)
  }

  def getBoostings: JsValue = {
    toJson(boostings.currentBoostings)
  }

  private def transformMappingToSimpleSchema(res: WSResponse) = {
    def getFieldType(value: JsValue): JsValue = (value \ "type").get.as[JsValue]

    JsObject((res.body[JsValue] \ "data" \ "mappings" \ "_doc" \ "properties")
      .get
      .as[JsObject]
      .fields
      .map(field => (field._1, getFieldType(field._2))))
  }

  def search(queryPattern: String): Future[WSResponse] = {
//    val groupingField = """category"""
    val groupingField = """city"""
//    var fields = Map("title" -> 5, "brand" -> 4, "binding" -> 3, "color" -> 2, "features" -> 1)
    var fields = Map("firstname" -> 5, "employer" -> 1)
      .map{ case (k, v) => "\"" + k + "^" + v + "\""}
      .mkString("[", ", ", "]")

    ws.url(s"$esDocRoot2/_search")
      .withMethod(HttpVerbs.GET)
      .withBody(Json.parse(
        s"""
           |{
           |  "query": {
           |    "multi_match" : {
           |      "query": "$queryPattern",
           |      "fields": $fields,
           |      "fuzziness": "AUTO"
           |    }
           |  },
           |  "aggs": {
           |    "by_$groupingField" : {
           |      "terms" : {
           |        "field" : "$groupingField.keyword"
           |      }
           |    }
           |  }
           |}
        """.stripMargin))
      .execute()
  }

  def bulkUpload(elements: IndexedSeq[JsValue]): Future[WSResponse] = {
    val ndjson = elements
      .map(element => Json.stringify(element))
      .foldLeft[String]("")((ndjson, entry) => ndjson + """{ "index" : { "_index" : "data", "_type" : "_doc" } }""" + "\n" + entry + "\n")
    ws.url(s"$esDocRoot/_bulk")
      .withMethod(HttpVerbs.POST)
      .withHttpHeaders("Content-Type" -> "application/x-ndjson")
      .withBody(InMemoryBody(ByteString(
        ndjson.stripMargin, StandardCharsets.UTF_8)))
      .withRequestFilter(AhcCurlRequestLogger())
      .execute()
  }
}
