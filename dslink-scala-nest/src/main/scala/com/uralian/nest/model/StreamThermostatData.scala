package com.uralian.nest.model

import com.uralian.util.JsonUtils._
import org.json4s.{CustomSerializer, DefaultFormats, Extraction}
import org.json4s.JsonAST._
import org.json4s.JsonDSL._

/**
  *
  * @param path
  * @param thermostats
  *
  */
case class StreamThermostatData(path: String, thermostats: Map[String, Thermostat], valid: Option[Boolean] = Some(true))

/**
  * JSON serializer for Environment instances.
  */
object StreamThermostatDataSerializer extends CustomSerializer[StreamThermostatData](implicit formats =>
  ( {
    case json => StreamThermostatData(
      path = json \ "path"  asString,
      thermostats = (json \ "data").extract[Map[String, Thermostat]]
    )
  }, {
    case a: StreamThermostatData => {
      implicit val formats = DefaultFormats + ThermostatSerializer
      ("path" -> a.path) ~
        ("thermostats" -> JObject(a.thermostats.map(tr => JField(tr._1, Extraction.decompose(tr._2))).toList)) ~
        ("valid" -> a.valid)
    }
  }))
