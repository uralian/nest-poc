package com.uralian.nest.model

import java.util.Locale

import com.uralian.util.JsonUtils._
import org.json4s.CustomSerializer
import org.json4s.JsonAST._
import org.json4s.JsonDSL._
import squants.thermal.TemperatureConversions._
import squants.thermal.{Celsius, Fahrenheit, Temperature, TemperatureScale}
import ThermostatConstants._

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
      deviceId = json \ DEVICE_ID asString,
      name = json \ NAME asString,
      label = json \ LABEL asString,
      nameLong = json \ NAME_LONG asString,
      humidity = json \ HUMIDITY asInt,
      locale = Locale.forLanguageTag(json \ LOCALE asString),
      temperatureScale = (json \ TEMPERATURE_SCALE asString) toLowerCase match {
        case "c" => Celsius
        case "f" => Fahrenheit
      },
      usingEmergencyHeat = json \ IS_EMERGENCY_HEAT asBoolean,
      hasFan = json \ HAS_FAN asBoolean,
      canHeat = json \ CAN_HEAT asBoolean,
      canCool = json \ CAN_COOL asBoolean,
      hvacMode = HvacMode.forName(json \ HVAC_MODE asString),
      hvacState = HvacState.forName(json \ HVAC_STATE asString),
      targetTemperature = (json \ TARGET_TEMP_F asInt) F,
      targetTemperatureHigh = (json \ TARGET_TEMP_HIGH_F asInt) F,
      targetTemperatureLow = (json \ TARGET_TEMP_LOW_F asInt) F,
      ambientTemperature = (json \ AMBIENT_TEMP_F asInt) F,
      awayTemperatureHigh = (json \ AWAY_TEMP_HIGH_F asInt) F,
      awayTemperatureLow = (json \ AWAY_TEMP_LOW_F asInt) F,
      locked = (json \ IS_LOCKED asBoolean),
      lockedTemperatureMin = (json \ LOCKED_TEMP_MIN_F asInt) F,
      lockedTemperatureMax = (json \ LOCKED_TEMP_MAX_F asInt) F,
      whereId = json \ WHERE_ID asString,
      whereName = json \ WHERE_NAME asString,
      structureId = json \ STRUCTURE_ID asString)
  }, {
    case t: Thermostat =>
      (DEVICE_ID -> t.deviceId) ~
        (NAME -> t.name) ~
        (LABEL -> t.label) ~
        (NAME_LONG -> t.nameLong) ~
        (HUMIDITY -> t.humidity) ~
        (LOCALE -> t.locale.toLanguageTag) ~
        (TEMPERATURE_SCALE -> t.temperatureScale.symbol.toLowerCase()) ~
        (IS_EMERGENCY_HEAT -> t.usingEmergencyHeat) ~
        (HAS_FAN -> t.hasFan) ~
        (CAN_HEAT -> t.canHeat) ~
        (CAN_COOL -> t.canCool) ~
        (HVAC_MODE -> t.hvacMode.toString) ~
        (HVAC_STATE -> t.hvacState.toString) ~
        (TARGET_TEMP_F -> t.targetTemperature.toFahrenheitDegrees) ~
        (TARGET_TEMP_HIGH_F -> t.targetTemperatureHigh.toFahrenheitDegrees) ~
        (TARGET_TEMP_LOW_F -> t.targetTemperatureLow.toFahrenheitDegrees) ~
        (AMBIENT_TEMP_F -> t.ambientTemperature.toFahrenheitDegrees) ~
        (AWAY_TEMP_HIGH_F -> t.awayTemperatureHigh.toFahrenheitDegrees) ~
        (AWAY_TEMP_LOW_F -> t.awayTemperatureLow.toFahrenheitDegrees) ~
        (IS_LOCKED -> t.locked) ~
        (LOCKED_TEMP_MIN_F -> t.lockedTemperatureMin.toFahrenheitDegrees) ~
        (LOCKED_TEMP_MAX_F -> t.lockedTemperatureMax.toFahrenheitDegrees) ~
        (WHERE_ID -> t.whereId) ~
        (WHERE_NAME -> t.whereName) ~
        (STRUCTURE_ID -> t.structureId)



  })
)