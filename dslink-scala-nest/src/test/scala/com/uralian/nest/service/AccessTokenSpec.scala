package com.uralian.nest.service

import com.uralian.nest.AbstractUnitSpec
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, Extraction}

import scala.concurrent.duration._

/**
  * AccessToken test suite.
  */
class AccessTokenSpec extends AbstractUnitSpec {

  implicit val formats = DefaultFormats + new AccessTokenSerializer

  "AccessTokenSerializer" should {
    "serialize token into JSON" in {
      val token = AccessToken("abcdefgh0123456789", 10 days)
      val json = Extraction.decompose(token)
      json mustBe ("access_token" -> "abcdefgh0123456789") ~ ("expires_in" -> 10 * 24 * 3600)
    }
    "deserialize token from JSON" in {
      val json = ("access_token" -> "abcdefgh0123456789") ~ ("expires_in" -> 315360000)
      val token = read[AccessToken](compact(render(json)))
      token mustBe AccessToken("abcdefgh0123456789", 3650 days)
    }
  }

}