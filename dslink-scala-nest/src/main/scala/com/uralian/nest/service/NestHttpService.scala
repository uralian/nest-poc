package com.uralian.nest.service

import com.softwaremill.sttp.ResponseAs
import com.softwaremill.sttp.json4s.asJson
import com.uralian.nest.model.{Thermostat, ThermostatSerializer}
import org.json4s.{DefaultFormats, JValue, _}

import scala.concurrent.{ExecutionContext, Future}

/**
  * REST-based implementation of Nest Service.
  *
  * @param client
  */
class NestHttpService(client: NestHttpClient) extends NestService {

  implicit val formats = DefaultFormats + ThermostatSerializer

  implicit val serialization = org.json4s.native.Serialization

  implicit val asJValue: ResponseAs[JValue, Nothing] = asJson[JValue]

  /**
    * Returns all thermostats associated with the specified access token.
    *
    * @param token
    * @param ec
    * @return
    */
  override def getAllThermostats(implicit token: AccessToken, ec: ExecutionContext): Future[Map[String, Thermostat]] =
    client.httpGet[JValue]("").map { json =>
      (json \ "devices" \ "thermostats").extract[Map[String, Thermostat]]
    }
}
