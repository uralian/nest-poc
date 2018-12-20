package com.uralian.nest.service

import com.softwaremill.sttp.{ResponseAs, asString}
import com.uralian.nest.AbstractITSpec
import com.uralian.nest.model.HvacMode

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Nest HTTP service test suite.
  */
class NestHttpServiceSpec extends AbstractITSpec {

  val client = new NestHttpClient(clientConfig)

  val service = new NestHttpService(client)

  implicit val asInt: ResponseAs[Int, Nothing] = asString.map(_.toInt)

  implicit val asHvacMode: ResponseAs[HvacMode, Nothing] = asString map (_.drop(1).dropRight(1)) map HvacMode.forName

  implicit val asBoolean: ResponseAs[Boolean, Nothing] = asString.map(_.toBoolean)

  var deviceId: String = _

  "getAllThermostats" should {
    "retrieve all thermostats" in {
      whenReady(service.getAllThermostats) { pcts =>
        pcts must not be empty
        deviceId = pcts.head._1
      }
    }
  }

  "getThermostat" should {
    "retrieve a thermostat by id" in {
      whenReady(service.getThermostat(deviceId)) { pct =>
        pct.deviceId mustBe deviceId
      }
    }
    "return None for invalid id" in {
      service.getThermostat("??????????????????").failed.futureValue mustBe an[IllegalArgumentException]
    }
  }

  "getAllStructures" should {
    "retrieve all structures" in {
      whenReady(service.getAllStructures) { struct => struct must not be empty }
    }
  }

  "getEnvironment" should {
    "retrieve all devices and structures" in {
      whenReady(service.getEnvironment) { env =>
        env.thermostats must not be empty
        env.structures must not be empty
      }
    }
  }

  "readThermostatValue" should {
    "retrieve a single thermostat parameter" in {
      whenReady(service.readThermostatValue[Int](deviceId, "humidity")) { humidity =>
        humidity must (be >= 0 and be <= 100)
      }
      whenReady(service.readThermostatValue[HvacMode](deviceId, "hvac_mode")) { mode =>
        mode must not be null
      }
      whenReady(service.readThermostatValue[Boolean](deviceId, "has_fan")) { fan =>
        fan mustBe true
      }
    }
  }
}
