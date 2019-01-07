package com.uralian.nest.model

import com.uralian.util.JsonUtils._
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JNull

/**
  *
  * @param path
  * @param thermostats
  */
case class StreamData(path: String, thermostats: Map[String, Thermostat], valid: Option[Boolean] = Some(true))

/**
  * JSON serializer for Environment instances.
  */
object StreamDataSerializer extends CustomSerializer[StreamData](implicit formats =>
  ( {
    case json => StreamData(
      path = json \ "path"  asString,
      thermostats = (json \ "data").extract[Map[String, Thermostat]]
    )
  }, {
    case _: StreamData => JNull
  }))
