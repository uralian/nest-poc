package com.uralian.nest.service

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Framing, Source}
import akka.util.ByteString
import com.softwaremill.sttp.ResponseAs
import com.softwaremill.sttp.json4s.asJson
import com.uralian.nest.dsa.Main.getClass
import com.uralian.nest.model._
import com.uralian.nest.streaming.{StreamSink, ThermostatStreaming}
import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, JValue, _}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.util.control.NonFatal

/**
  * REST-based implementation of Nest Service.
  *
  * @param client
  */
class NestHttpService(client: NestHttpClient) extends NestService {

  implicit val formats = DefaultFormats +
    ThermostatSerializer + StructureSerializer + WhereSerializer + EnvironmentSerializer + StreamThermostatDataSerializer

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

  /**
    * Reads a single thermostat value specified by its name. Eg. "humidity" or "ambient_temperature_c".
    *
    * @param deviceId
    * @param name
    * @param token
    * @param as
    * @param ec
    * @tparam T
    * @return
    */
  def readThermostatValue[T: Manifest](deviceId: String, name: String)(implicit token: AccessToken,
                                                                       as: ResponseAs[T, Nothing],
                                                                       ec: ExecutionContext): Future[T] =
    client.httpGet[T](s"devices/thermostats/$deviceId/$name").recover {
      case NonFatal(e) => throw new IllegalArgumentException(s"Error retrieving devices/thermostats/$deviceId/$name", e)
    }

  /**
    * Read a stream of data and send it to sink
    *
    * @param token
    * @param ec
    * @param system
    * @tparam T
    * @return
    */
  def readThermostatStream[T]()(implicit token: AccessToken, ec: ExecutionContext,
                                system: ActorSystem): Future[Source[StreamThermostatData, Any]] = {
    implicit val materializer = ActorMaterializer()
    implicit val dataSerializer = ThermostatStreaming.dataSerializer
    val uri = "/devices/thermostats/"
    for {
      source <- client.httpGetStream[StreamThermostatData]("devices/thermostats/")
    } yield {
      source
    }
  }
}