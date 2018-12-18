package com.uralian.nest.model

import java.util.Locale

import com.uralian.nest.AbstractUnitSpec
import org.json4s._
import org.json4s.native.JsonMethods._
import squants.thermal.Fahrenheit
import squants.thermal.TemperatureConversions._

/**
  * Thermostat test suite.
  */
class ThermostatSpec extends AbstractUnitSpec {

  implicit val formats = DefaultFormats + ThermostatSerializer

  "ThermostatSerializer" should {
    "de-serialize thermostat from JSON" in {
      val json = parse(getClass.getResourceAsStream("/devices.json"))
      val thermostats = json \ "devices" \ "thermostats"
      val thermostat = thermostats.extract[Map[String, Thermostat]].head._2
      thermostat mustBe Thermostat(
        deviceId = "Gp_Mnef0pHHZno317X9NiVHG0PjbkGTo",
        name = "Living Room (114B)",
        label = "114B",
        nameLong = "Living Room Thermostat (114B)",
        humidity = 50,
        locale = Locale.US,
        temperatureScale = Fahrenheit,
        usingEmergencyHeat = false,
        hasFan = true,
        canHeat = true,
        canCool = true,
        hvacMode = HvacMode.Heat,
        targetTemperature = 68.F,
        targetTemperatureHigh = 79.F,
        targetTemperatureLow = 66.F,
        ambientTemperature = 70.F,
        awayTemperatureHigh = 76.F,
        awayTemperatureLow = 55.F,
        locked = false,
        lockedTemperatureMin = 68.F,
        lockedTemperatureMax = 72.F,
        whereId = "KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SyRq5RNksxcXg",
        whereName = "Living Room",
        structureId = "4bI0rz4GwpcqkLvf9tf821t3cAvm22Qpexlpj0igWD0AfRDVV-ldDg"
      )
    }
  }
}
