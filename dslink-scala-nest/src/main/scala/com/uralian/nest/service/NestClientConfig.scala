package com.uralian.nest.service

import com.softwaremill.sttp._
import com.typesafe.config.Config

/**
  * Nest cloud client configuration.
  *
  * @param clientId
  * @param clientSecret
  * @param accessTokenUrl
  * @param grantType
  * @param apiUrl
  */
case class NestClientConfig(clientId: String, clientSecret: String, accessTokenUrl: String, grantType: String,
                            apiUrl: String) {
  val accessTokenUri = uri"$accessTokenUrl"
  val apiUri = uri"$apiUrl"
}

/**
  * Factory for [[NestClientConfig]] instances.
  */
object NestClientConfig {

  /**
    * Creates a [[NestClientConfig]] instance from the supplied configuration.
    *
    * @param cfg
    * @return
    */
  def apply(cfg: Config): NestClientConfig = {
    val clientId = cfg.getString("auth.client.id")
    val clientSecret = cfg.getString("auth.client.secret")
    val accessTokenUrl = cfg.getString("auth.access_token.url")
    val grantType = cfg.getString("auth.grant_type")
    val apiUrl = cfg.getString("api.url")

    NestClientConfig(clientId = clientId, clientSecret = clientSecret, accessTokenUrl = accessTokenUrl,
      grantType = grantType, apiUrl = apiUrl)
  }
}
