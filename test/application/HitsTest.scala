package application

import org.specs2.matcher.ShouldMatchers
import play.api.Application
import play.api.libs.json.{JsNumber, Json}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class HitsTest extends PlaySpecification with ShouldMatchers {
  sequential
  //  Search hits for iphone
  //  Wireless Phone Accessory - 845
  //Electronics - 315
  //  Office Product - 64
  //  Personal Computers - 49
  //  Watch - 26


  "Search" should {

    "return count of 'hits' for each category" in new WithServer {
      private val searchResponse = getRequest(app, "iphone")
      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check categories and entites
      Json.parse(body) \\ "hits" contains JsNumber(845)
      Json.parse(body) \\ "hits" contains JsNumber(315)
      Json.parse(body) \\ "hits" contains JsNumber(64)
      Json.parse(body) \\ "hits" contains JsNumber(49)
      Json.parse(body) \\ "hits" contains JsNumber(26)
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
