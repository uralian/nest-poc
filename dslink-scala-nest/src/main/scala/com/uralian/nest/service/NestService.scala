package com.uralian.nest.service

import com.uralian.nest.model.{Environment, Structure, Thermostat}

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
}
