package com.uralian.nest.service

import com.softwaremill.sttp._
import com.softwaremill.sttp.json4s._
import com.uralian.nest.AbstractITSpec
import com.uralian.util.JsonUtils._
import org.json4s._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Nest HTTP client test suite.
  */
class NestHttpClientSpec extends AbstractITSpec {

  import clientConfig._

  val client = new NestHttpClient(clientConfig)

  implicit val serialization = org.json4s.native.Serialization

  implicit val asJValue: ResponseAs[JValue, Nothing] = asJson[JValue]

  "obtainAccessToken" should {
    "fail on more than one use per PIN" in {
      val pin = rootConfig.getString("nest-cloud.test-account.pin")
      val fResult = for {
        _ <- client.obtainAccessToken(clientId, clientSecret, pin)
        token <- client.obtainAccessToken(clientId, clientSecret, pin)
      } yield token
      fResult.failed.futureValue mustBe a[RuntimeException]
    }
  }

  "httpGet" should {
    "retrieve root API response" in {
      whenReady(client.httpGet[JValue](apiUri)) { all =>
        val at = (all \ "metadata" \ "access_token" asString)
        at mustBe accessToken.token
      }
    }
    "retrieve a response fragment" in {
      implicit val asInt: ResponseAs[Int, Nothing] = asString.map(_.toInt)
      val fResult = for {
        all <- client.httpGet[JValue](apiUri)
        thermostats = all \ "devices" \ "thermostats"
        tstatId = thermostats.asInstanceOf[JObject].values.keys.head
        humidity <- client.httpGet[Int](uri"$apiUri/devices/thermostats/$tstatId/humidity")
      } yield humidity
      whenReady(fResult) { humidity =>
        humidity must (be >= 0 and be <= 100)
      }
    }
  }
}
