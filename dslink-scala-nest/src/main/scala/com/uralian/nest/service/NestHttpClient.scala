package com.uralian.nest.service

import akka.NotUsed
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.softwaremill.sttp._
import com.softwaremill.sttp.akkahttp.AkkaHttpBackend
import com.softwaremill.sttp.json4s._
import org.json4s.DefaultFormats
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}

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
                       (implicit ec: ExecutionContext): Future[AccessToken] = {

    implicit val serialization = org.json4s.native.Serialization
    implicit val formats = DefaultFormats + AccessTokenSerializer

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
  def httpGet[T: Manifest](uri: Uri)
                          (implicit at: AccessToken, as: ResponseAs[T, Nothing], ec: ExecutionContext): Future[T] = {
    val request = sttp.auth.bearer(at.token).contentType(MediaTypes.Json)
    val response = request.get(uri).response(as).send()
    response map {
      case Response(Right(body), _, _, _, _) => body
      case Response(Left(bytes), _, _, _, _) => throw new RuntimeException(new String(bytes))
    }
  }

  /**
    * Sends an HTTP GET request to a Nest API endpoint. The endpoint is constructed by concatenating the base API URL
    * followed by "/" and then the 'query' parameter.
    *
    * @param query
    * @param at
    * @param as
    * @param ec
    * @tparam T
    * @return
    */
  def httpGet[T: Manifest](query: String)
                          (implicit at: AccessToken, as: ResponseAs[T, Nothing], ec: ExecutionContext): Future[T] =
    httpGet(uri"${config.apiUrl}/$query")

  val log = LoggerFactory.getLogger(getClass)

  /**
    *
    * @param at
    * @param ec
    * @return
    */
  def httpStream()(implicit at: AccessToken, ec: ExecutionContext): Future[Source[ByteString, NotUsed]] = {

    val request = sttp.auth.bearer(at.token).header("Accept", "text/event-stream")
    log.info(s"sending ${at.token}")
    val response = request.get(uri"${config.apiUrl}/devices/thermostats/").response(asStream[Source[ByteString, NotUsed]]).send()
    response map {
      case Response(Right(body), _, _, _, _) => {

        body
      }
      case Response(Left(bytes), _, _, _, _) => {
        log.error(new String(bytes))
        throw new RuntimeException(new String(bytes))
      }
    }
  }
}
