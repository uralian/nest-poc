package com.uralian.nest.service

import com.uralian.util.JsonUtils._
import org.json4s.JsonDSL._
import org.json4s._

import scala.concurrent.duration._

/**
  * Access token for communicating with Nest cloud.
  *
  * @param token
  * @param expiresIn
  */
case class AccessToken(token: String, expiresIn: Duration)

/**
  * JSON serializer for AccessToken.
  */
class AccessTokenSerializer extends CustomSerializer[AccessToken](_ =>
  ( {
    case json => AccessToken(json \ "access_token" asString, (json \ "expires_in" asLong) seconds)
  }, {
    case AccessToken(token, expiresIn) => ("access_token" -> token) ~ ("expires_in" -> expiresIn.toSeconds)
  })
)