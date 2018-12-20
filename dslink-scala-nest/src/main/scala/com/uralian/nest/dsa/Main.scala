package com.uralian.nest.dsa

import com.typesafe.config.{Config, ConfigFactory}
import com.uralian.dsa._
import com.uralian.nest.service.{NestClientConfig, NestHttpClient, NestHttpService}
import org.dsa.iot.dslink.DSLinkHandler
import org.slf4j.LoggerFactory

/**
  * Application entry point.
  */
object Main extends App {

  val log = LoggerFactory.getLogger(getClass)

  val service = createNestService(ConfigFactory.load())
  log.info("Nest HTTP service created")

  lazy val connector = DSAConnector(args)
  lazy val connection = connector.start(LinkMode.RESPONDER)

  implicit def responder = connection.responder

  val root = connection.responderLink.getNodeManager.getSuperRoot
  val controller = new AppController(root, service)

  waitForShutdown()

  /**
    * Waits for user to press ENTER, then shuts the app down.
    *
    * @return
    */
  private def waitForShutdown() = {
    println("\nPress ENTER to exit")
    Console.in.readLine
    connector.stop
    sys.exit(0)
  }

  /**
    * Creates a Nest API service.
    *
    * @param cfg
    * @return
    */
  private def createNestService(cfg: Config) = {
    val clientConfig = NestClientConfig(cfg.getConfig("nest-cloud"))
    val client = new NestHttpClient(clientConfig)
    new NestHttpService(client)
  }
}

/**
  * Dummy DSLink handler to be put in dslink.json.
  */
class DummyDSLinkHandler extends DSLinkHandler