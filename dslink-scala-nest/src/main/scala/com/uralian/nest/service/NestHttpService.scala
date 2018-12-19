package com.uralian.nest.service

import com.softwaremill.sttp.ResponseAs
import com.softwaremill.sttp.json4s.asJson
import com.uralian.nest.model._
import org.json4s.{DefaultFormats, JValue, _}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

/**
  * REST-based implementation of Nest Service.
  *
  * @param client
  */
class NestHttpService(client: NestHttpClient) extends NestService {

  implicit val formats = DefaultFormats +
    ThermostatSerializer + StructureSerializer + WhereSerializer + EnvironmentSerializer

  implicit val serialization = org.json4s.native.Serialization

  implicit val asJValue: ResponseAs[JValue, Nothing] = asJson[JValue]

  implicit val asThermostat: ResponseAs[Thermostat, Nothing] = asJson[Thermostat]

  implicit val asStructure: ResponseAs[Structure, Nothing] = asJson[Structure]

  implicit val asEnvironment: ResponseAs[Environment, Nothing] = asJson[Environment]

  /**
    * Returns all thermostats associated with the specified access token.
    *
    * @param token
    * @param ec
    * @return
    */
  def getAllThermostats(implicit token: AccessToken, ec: ExecutionContext): Future[Map[String, Thermostat]] =
    client.httpGet[JValue]("").map { json =>
      (json \ "devices" \ "thermostats").extract[Map[String, Thermostat]]
    }

  /**
    * Returns the thermostat by the specified id.
    *
    * @param deviceId
    * @param token
    * @param ec
    * @return
    */
  def getThermostat(deviceId: String)(implicit token: AccessToken, ec: ExecutionContext): Future[Thermostat] =
    client.httpGet[Thermostat](s"devices/thermostats/$deviceId").recover {
      case NonFatal(e) => throw new IllegalArgumentException(s"Invalid deviceId: $deviceId", e)
    }

  /**
    * Returns all structures associated with the specified access token.
    *
    * @param token
    * @param ec
    * @return
    */
  def getAllStructures(implicit token: AccessToken, ec: ExecutionContext): Future[Map[String, Structure]] =
    client.httpGet[JValue]("").map { json =>
      (json \ "structures").extract[Map[String, Structure]]
    }

  /**
    * Returns the complete list of devices and structures associated with the specified access token.
    *
    * @param token
    * @param ec
    * @return
    */
  def getEnvironment(implicit token: AccessToken, ec: ExecutionContext): Future[Environment] =
    client.httpGet[Environment]("")
}