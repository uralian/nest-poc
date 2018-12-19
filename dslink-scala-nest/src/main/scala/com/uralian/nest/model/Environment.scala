package com.uralian.nest.model

import com.uralian.util.JsonUtils._
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JNull

/**
  * Environment data (devices + structures).
  *
  * @param thermostats
  * @param structures
  */
case class Environment(userId: String,
                       clientVersion: Int,
                       thermostats: Map[String, Thermostat],
                       structures: Map[String, Structure])

/**
  * JSON serializer for Environment instances.
  */
object EnvironmentSerializer extends CustomSerializer[Environment](implicit formats =>
  ( {
    case json => Environment(
      userId = json \ "metadata" \ "user_id" asString,
      clientVersion = json \ "metadata" \ "client_version" asInt,
      thermostats = (json \ "devices" \ "thermostats").extract[Map[String, Thermostat]],
      structures = (json \ "structures").extract[Map[String, Structure]]
    )
  }, {
    case _: Environment => JNull
  }))
