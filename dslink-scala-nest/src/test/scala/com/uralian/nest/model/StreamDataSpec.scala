package com.uralian.nest.model

import java.util.Locale

import com.uralian.nest.AbstractUnitSpec
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import squants.thermal.{Celsius, Fahrenheit}
import squants.thermal.TemperatureConversions._

class StreamDataSpec extends AbstractUnitSpec {

  implicit val formats = DefaultFormats + ThermostatSerializer + StreamDataSerializer

  "ThermostatSerializer" should {
    "de-serialize Thermostat from JSON" in {
      val raw = parse(getClass.getResourceAsStream("/stream.json"))
      val json = parse(getClass.getResourceAsStream("/stream.json")).extract[StreamData]
      val thermostat = json.thermostats.head._2
      thermostat mustBe Thermostat(
        deviceId = "j5mQopvmZ_Nygt4_ld0adEsl6khKs_eO",
        name = "Family Room",
        label = "",
        nameLong = "Family Room Thermostat",
        humidity = 50,
        locale = Locale.US,
        temperatureScale = Celsius,
        usingEmergencyHeat = false,
        hasFan = true,
        canHeat = true,
        canCool = true,
        hvacMode = HvacMode.Heat,
        hvacState = HvacState.Heat,
        targetTemperature = 70.F,
        targetTemperatureHigh = 75.F,
        targetTemperatureLow = 68.F,
        ambientTemperature = 70.F,
        awayTemperatureHigh = 80.F,
        awayTemperatureLow = 60.F,
        locked = false,
        lockedTemperatureMin = 68.F,
        lockedTemperatureMax = 72.F,
        whereId = "4D4LeQ-tY2wArCrzvew0XCgyxnLvuFamaVhgRm7Zbdo9LFHR_-Q4Mw",
        whereName = "Family Room",
        structureId = "YpnTVBNzquPdDjNf7ZoJW8SOi1SXA2C-b9Evw96b6p-uF5gKJTrcAg"
      )
    }
  }

}
