package com.uralian.nest

import com.typesafe.config.ConfigFactory
import com.uralian.nest.service.{AccessToken, NestClientConfig}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

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

  /**
    * Nest HTTP client configuration.
    */
  val clientConfig = NestClientConfig(rootConfig.getConfig("nest-cloud"))

  /**
    * Nest API access token.
    */
  implicit val accessToken = AccessToken(rootConfig.getString("nest-cloud.api.token"), null)

  /**
    * Default timeout for Future testing.
    */
  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(250, Millis))
}
