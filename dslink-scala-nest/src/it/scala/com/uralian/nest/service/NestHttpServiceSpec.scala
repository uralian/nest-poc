package com.uralian.nest.service

import com.uralian.nest.AbstractITSpec

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Nest HTTP service test suite.
  */
class NestHttpServiceSpec extends AbstractITSpec {

  val client = new NestHttpClient(clientConfig)

  val service = new NestHttpService(client)

  "getAllThermostats" should {
    "retrieve all thermostats" in {
      whenReady(service.getAllThermostats) { tstats => tstats must not be empty }
    }
  }
}
