package com.uralian.nest.service

import akka.Done
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import com.softwaremill.sttp.ResponseAs
import com.uralian.nest.model.{Environment, StreamThermostatData, Structure, Thermostat}
import com.uralian.nest.streaming.StreamSink

import scala.concurrent.{ExecutionContext, Future}

/**
  * Nest Service.
  */
trait NestService {

  /**
    * Returns all thermostats for the specified access token.
    *
    * @param token
    * @param ec
    * @return
    */
  def getAllThermostats(implicit token: AccessToken, ec: ExecutionContext): Future[Map[String, Thermostat]]

  /**
    * Returns the thermostat by the specified id.
    *
    * @param deviceId
    * @param token
    * @param ec
    * @return
    */
  def getThermostat(deviceId: String)(implicit token: AccessToken, ec: ExecutionContext): Future[Thermostat]

  /**
    * Returns all structures associated with the specified access token.
    *
    * @param token
    * @param ec
    * @return
    */
  def getAllStructures(implicit token: AccessToken, ec: ExecutionContext): Future[Map[String, Structure]]

  /**
    * Returns the complete list of devices and structures associated with the specified access token.
    *
    * @param token
    * @param ec
    * @return
    */
  def getEnvironment(implicit token: AccessToken, ec: ExecutionContext): Future[Environment]

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
                                                                       ec: ExecutionContext): Future[T]

  /**
    * Reads stream of data and pushes to sink
    *
    */
  def readThermostatStream[T]()(implicit token: AccessToken, ec: ExecutionContext,
                                system: ActorSystem): Future[Source[StreamThermostatData, Any]]
}