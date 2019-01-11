package com.uralian.nest.model

import java.util.Locale

import com.uralian.util.JsonUtils._
import org.json4s.CustomSerializer
import org.json4s.JsonAST._
import org.json4s.JsonDSL._
import squants.thermal.TemperatureConversions._
import squants.thermal.{Celsius, Fahrenheit, Temperature, TemperatureScale}

/**
  * Thermostat data.
  *
  * @param deviceId
  * @param name
  * @param label
  * @param nameLong
  * @param humidity
  * @param locale
  * @param temperatureScale
  * @param usingEmergencyHeat
  * @param hasFan
  * @param canHeat
  * @param canCool
  * @param hvacMode
  * @param targetTemperature
  * @param targetTemperatureHigh
  * @param targetTemperatureLow
  * @param ambientTemperature
  * @param awayTemperatureHigh
  * @param awayTemperatureLow
  * @param locked
  * @param lockedTemperatureMin
  * @param lockedTemperatureMax
  * @param whereId
  * @param whereName
  * @param structureId
  */
case class Thermostat(deviceId: String,
                      name: String,
                      label: String,
                      nameLong: String,
                      humidity: Int,
                      locale: Locale,
                      temperatureScale: TemperatureScale,
                      usingEmergencyHeat: Boolean,
                      hasFan: Boolean,
                      canHeat: Boolean,
                      canCool: Boolean,
                      hvacMode: HvacMode,
                      hvacState: HvacState,
                      targetTemperature: Temperature,
                      targetTemperatureHigh: Temperature,
                      targetTemperatureLow: Temperature,
                      ambientTemperature: Temperature,
                      awayTemperatureHigh: Temperature,
                      awayTemperatureLow: Temperature,
                      locked: Boolean,
                      lockedTemperatureMin: Temperature,
                      lockedTemperatureMax: Temperature,
                      whereId: String,
                      whereName: String,
                      structureId: String)

/**
  * JSON serializer for Thermostat instances.
  */
object ThermostatSerializer extends CustomSerializer[Thermostat](_ =>
  ( {
    case json => Thermostat(
      deviceId = json \ "device_id" asString,
      name = json \ "name" asString,
      label = json \ "label" asString,
      nameLong = json \ "name_long" asString,
      humidity = json \ "humidity" asInt,
      locale = Locale.forLanguageTag(json \ "locale" asString),
      temperatureScale = (json \ "temperature_scale" asString) toLowerCase match {
        case "c" => Celsius
        case "f" => Fahrenheit
      },
      usingEmergencyHeat = json \ "is_using_emergency_heat" asBoolean,
      hasFan = json \ "has_fan" asBoolean,
      canHeat = json \ "can_heat" asBoolean,
      canCool = json \ "can_cool" asBoolean,
      hvacMode = HvacMode.forName(json \ "hvac_mode" asString),
      hvacState = HvacState.forName(json \ "hvac_state" asString),
      targetTemperature = (json \ "target_temperature_f" asInt) F,
      targetTemperatureHigh = (json \ "target_temperature_high_f" asInt) F,
      targetTemperatureLow = (json \ "target_temperature_low_f" asInt) F,
      ambientTemperature = (json \ "ambient_temperature_f" asInt) F,
      awayTemperatureHigh = (json \ "away_temperature_high_f" asInt) F,
      awayTemperatureLow = (json \ "away_temperature_low_f" asInt) F,
      locked = (json \ "is_locked" asBoolean),
      lockedTemperatureMin = (json \ "locked_temp_min_f" asInt) F,
      lockedTemperatureMax = (json \ "locked_temp_max_f" asInt) F,
      whereId = json \ "where_id" asString,
      whereName = json \ "where_name" asString,
      structureId = json \ "structure_id" asString)
  }, {
    case t: Thermostat =>
      ("device_id" -> t.deviceId) ~
        ("name" -> t.name) ~
        ("label" -> t.label) ~
        ("name_long" -> t.nameLong) ~
        ("humidity" -> t.humidity) ~
        ("locale" -> t.locale.toLanguageTag) ~
        ("temperature_scale" -> t.temperatureScale.symbol.toLowerCase()) ~
        ("is_using_emergency_heat" -> t.usingEmergencyHeat) ~
        ("has_fan" -> t.hasFan) ~
        ("can_heat" -> t.canHeat) ~
        ("can_cool" -> t.canCool) ~
        ("hvac_mode" -> t.hvacMode.toString) ~
        ("hvac_state" -> t.hvacState.toString) ~
        ("target_temperature_f" -> t.targetTemperature.toFahrenheitDegrees) ~
        ("target_temperature_high_f" -> t.targetTemperatureHigh.toFahrenheitDegrees) ~
        ("target_temperature_low_f" -> t.targetTemperatureLow.toFahrenheitDegrees) ~
        ("ambient_temperature_f" -> t.ambientTemperature.toFahrenheitDegrees) ~
        ("away_temperature_high_f" -> t.awayTemperatureHigh.toFahrenheitDegrees) ~
        ("away_temperature_low_f" -> t.awayTemperatureLow.toFahrenheitDegrees) ~
        ("is_locked" -> t.locked) ~
        ("locked_temp_min_f" -> t.lockedTemperatureMin.toFahrenheitDegrees) ~
        ("locked_temp_max_f" -> t.lockedTemperatureMax.toFahrenheitDegrees) ~
        ("where_id" -> t.whereId) ~
        ("where_name" -> t.whereName) ~
        ("structure_id" -> t.structureId)



  })
)