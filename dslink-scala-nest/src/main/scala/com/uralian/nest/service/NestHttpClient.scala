package com.uralian.nest.service

import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.json4s._
import org.json4s.DefaultFormats

import scala.concurrent.ExecutionContext

/**
  * HTTP client for accessing Nest API.
  *
  * @param config
  */
class NestHttpClient(config: NestClientConfig) {

  import config._

  implicit val backend = AkkaHttpBackend()

  /**
    * Obtais a security token for using API.
    *
    * @param clientId
    * @param clientSecret
    * @param pinCode
    * @param ec
    * @return
    */
  def obtainAccessToken(clientId: String, clientSecret: String, pinCode: String)
                       (implicit ec: ExecutionContext) = {

    implicit val serialization = org.json4s.native.Serialization
    implicit val formats = DefaultFormats + new AccessTokenSerializer

    val request = sttp.body("client_id" -> clientId, "client_secret" -> clientSecret, "grant_type" -> grantType,
      "code" -> pinCode).response(asJson[AccessToken]).post(accessTokenUri)
    request.send().map {
      case Response(Right(body), _, _, _, _) => body
      case Response(Left(bytes), _, _, _, _) => throw new RuntimeException(new String(bytes))
    }
  }

  /**
    * Sends an HTTP GET request to the specified endpoint and returns the response as specified by the type.
    *
    * @param uri
    * @param at
    * @param as
    * @tparam T
    * @return
    */
  def httpGet[T: Manifest](uri: Uri)(implicit at: AccessToken, as: ResponseAs[T, Nothing], ec: ExecutionContext) = {
    val request = sttp.auth.bearer(at.token).contentType(MediaTypes.Json)
    val response = request.get(uri).response(as).send()
    response map {
      case Response(Right(body), _, _, _, _) => body
      case Response(Left(bytes), _, _, _, _) => throw new RuntimeException(new String(bytes))
    }
  }
}
