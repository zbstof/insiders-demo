package application

import org.specs2.matcher.ShouldMatchers
import play.api.libs.json.Json
import play.api.test.PlaySpecification
import services.AmazonMappingService

class MappingTest extends PlaySpecification with ShouldMatchers {
  sequential

  val input =
    """ITEM 1
      |Brand=Mediasonic
      |Feature=Converts digital ATSC broadcast to your Analog & Digital TV, Projector, and Monitor
      |Feature=USB Multimedia Player (Support USB Flash Drive up to 64GB)
      |Feature=Support MPEG-1 Layer I & II Dolby Digital Audio, Support MPEG-2/4, H.264/AVC, 1080P HD Digital TV
      |Feature=HDMI 1080P Output / Composite Output / Coaxial Output, Analog Pass Through, Channel 3 / 4 Function
      |Feature=Favorite Channel List, Parental Control Function, Auto Tuning, Closed Caption, Auto, 16:9 Pillar Box, 16:9 Pan G Scan, 4:3 Letter Box, 4:3 Pan G Scan, 4:3 Full, 16:9 Wide Screen
      |Warranty=1 year warranty""".stripMargin

  val expected =
    """[{"_id" : 1,
      |"Binding":"Others",
      |"Brand":"Mediasonic",
      |"Feature":["Converts digital ATSC broadcast to your Analog & Digital TV, Projector, and Monitor",
      |"USB Multimedia Player (Support USB Flash Drive up to 64GB)",
      |"Support MPEG-1 Layer I & II Dolby Digital Audio, Support MPEG-2/4, H.264/AVC, 1080P HD Digital TV",
      |"HDMI 1080P Output / Composite Output / Coaxial Output, Analog Pass Through, Channel 3 / 4 Function",
      |"Favorite Channel List, Parental Control Function, Auto Tuning, Closed Caption, Auto, 16:9 Pillar Box, 16:9 Pan G Scan, 4:3 Letter Box, 4:3 Pan G Scan, 4:3 Full, 16:9 Wide Screen"],
      |"Warranty":"1 year warranty"}]""".stripMargin

  "AmazonMappingService" should {

    "map amazon format to json structure successfully" in {
      val actual = new AmazonMappingService().toJson(input, Json.obj("Binding" -> "Others"))
      actual must equalTo(Json.parse(expected))
    }
  }
}
