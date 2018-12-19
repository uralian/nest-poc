package com.uralian.nest.model

import java.util.TimeZone

import com.uralian.util.JsonUtils._
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JNull

/**
  * Structure data.
  *
  * @param structureId
  * @param name
  * @param countryCode
  * @param timeZone
  * @param away
  * @param thermostatIds
  * @param wheres
  */
case class Structure(structureId: String, name: String, countryCode: String, timeZone: TimeZone, away: AwayState,
                     thermostatIds: List[String], wheres: Map[String, Where])

/**
  * JSON serializer for Structure instances.
  */
object StructureSerializer extends CustomSerializer[Structure](implicit formats =>
  ( {
    case json => Structure(
      structureId = json \ "structure_id" asString,
      name = json \ "name" asString,
      countryCode = json \ "country_code" asString,
      timeZone = TimeZone.getTimeZone(json \ "time_zone" asString),
      away = AwayState.forName(json \ "away" asString),
      thermostatIds = (json \ "thermostats" asArray).map(_ asString),
      wheres = (json \ "wheres").extract[Map[String, Where]]
    )
  }, {
    case _: Where => JNull
  }))