package application

import org.specs2.matcher.ShouldMatchers
import play.api.Application
import play.api.libs.json.{JsString, Json}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class TypoTest extends PlaySpecification with ShouldMatchers {
  sequential
  //API: Search by title
  //API: Search by Binding
  //API: Search by Brand
  //API: Search by Color
  //API: Search by Feature

  "Search" should {

    "search with mistakes" in new WithServer {
      private val searchResponse = getRequest(app, "ikovert")
      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check categories and entites
      Json.parse(body) \\ "Color" contains JsString("ikonvert")
      Json.parse(body) \\ "Title" contains JsString("Supersonic iKonvert DTV Digital to Analog Converter Box with HDMI 1080P Out and USB Media Player")
      Json.parse(body) \\ "Brand" contains JsString("Supersonic")
      println("OK")
    }
  }

  private def getRequest(app: Application, keyword: String): WSResponse = {
    val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
    println("Search: " + searchUrl(keyword))
    return await(ws.url(searchUrl(keyword)).get())
  }

  private def searchUrl(pattern: String): String = {
    s"http://localhost:$testServerPort/search?pattern=$pattern"
  }
}
