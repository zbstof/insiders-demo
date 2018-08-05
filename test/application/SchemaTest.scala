package application

import org.specs2.matcher.ShouldMatchers
import play.api.libs.json.{JsString, Json}
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.test.{PlaySpecification, WithServer}

// TODO: currently these tests require mongo and elastic running locally on default ports
class SchemaTest extends PlaySpecification with ShouldMatchers {
  sequential

  "Schema" should {

    "return json schema" in new WithServer {
      val ws: WSClient = app.injector.instanceOf(classOf[WSClient])

      println("Search: " + searchUrl())
      private val searchResponse: WSResponse = await(ws.url(searchUrl()).get())

      searchResponse.status must equalTo(OK)
      val body = searchResponse.body
      println("Response: " + body)
      //TODO: check some in body
    }
  }

  private def searchUrl(): String = {
    s"http://localhost:$testServerPort/schema"
  }
}
