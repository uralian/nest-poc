package com.uralian.nest.service

import com.uralian.nest.AbstractITSpec

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Nest HTTP service test suite.
  */
class NestHttpServiceSpec extends AbstractITSpec {

  val client = new NestHttpClient(clientConfig)

  val service = new NestHttpService(client)

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
}
