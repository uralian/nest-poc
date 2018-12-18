package com.uralian.nest

import com.typesafe.config.ConfigFactory
import com.uralian.nest.service.{AccessToken, NestClientConfig}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

/**
  * Base trait for integration test specifications.
  */
trait AbstractITSpec extends Suite
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll
  with OptionValues
  with ScalaFutures {

  val rootConfig = ConfigFactory.load("integration.conf")

  val clientConfig = NestClientConfig(rootConfig.getConfig("nest-cloud"))

  val pin = rootConfig.getString("nest-cloud.test-account.pin")

  val accessToken = AccessToken(rootConfig.getString("nest-cloud.api.token"), null)
}
