package application

import org.specs2.matcher.ShouldMatchers
import play.api.Application
import play.api.libs.json.{JsString, Json}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class SearchTest extends PlaySpecification with ShouldMatchers {
  sequential

  "Search" should {

    "search by Title" in new WithServer {
      private val searchResponse=getRequest(app,"RCA Digital-to-Analog Converter Box")
      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check categories and entites
      Json.parse(body) \\ "Title" contains JsString("RCA Digital-to-Analog Converter Box")
      Json.parse(body) \\ "Binding" contains JsString("Electronics")
      Json.parse(body) \\ "Brand" contains JsString("RCA")
      println("OK")
    }

  "search by Binding" in new WithServer {
      private val searchResponse=getRequest(app,"Game Cartridge")
      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check categories and entites
      Json.parse(body) \\ "Title" contains JsString("Gunnar Optiks INT-00101 Intercept Full Rim Advanced Video Gaming Glasses with Amber Lens Tint, Onyx Frame Finish")
      Json.parse(body) \\ "Binding" contains JsString("Game Cartridge")
      Json.parse(body) \\ "Brand" contains JsString("Gunnar Optiks")
      println("OK")
    }

  "search by Brand" in new WithServer {
      private val searchResponse=getRequest(app,"DTX9950")
      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check categories and entites
    Json.parse(body) \\ "Brand" contains JsString("DTX9950")
    Json.parse(body) \\ "Title" contains JsString("Digital Stream Analog Pass-through DTV Converter Box")
    Json.parse(body) \\ "Binding" contains JsString("Electronics")
      println("OK")
    }

  "search by Color" in new WithServer {
      private val searchResponse=getRequest(app,"ikonvert")
      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check categories and entites
    Json.parse(body) \\ "Color" contains JsString("ikonvert")
    Json.parse(body) \\ "Title" contains JsString("Supersonic iKonvert DTV Digital to Analog Converter Box with HDMI 1080P Out and USB Media Player")
    Json.parse(body) \\ "Brand" contains JsString("Supersonic")
      println("OK")
    }

  "search by Feature" in new WithServer {
      private val searchResponse=getRequest(app,"USB Multimedia Player (Support USB Flash Drive up to 64GB)")
      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check categories and entites
    Json.parse(body) \\ "Title" contains JsString("Mediasonic HW180STB HomeWorx HDTV Digital Converter Box with Media Player Function, Dolby Digital and HDMI Out")
    Json.parse(body) \\ "Binding" contains JsString("Electronics")
    Json.parse(body) \\ "Brand" contains JsString("Mediasonic")
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
