package com.uralian.nest.dsa

import com.uralian.dsa._
import com.uralian.nest.service.{AccessToken, NestService}
import org.dsa.iot.dslink.link.Responder
import org.dsa.iot.dslink.node.Node
import org.dsa.iot.dslink.node.value.ValueType._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Nest application controller.
  */
class AppController(root: Node, service: NestService)(implicit responder: Responder) {

  val log = LoggerFactory.getLogger(getClass)

  initRoot(root)
  log.info("Application controller started")

  /**
    * Initializes the root node.
    *
    * @param root
    */
  private def initRoot(root: Node) = {
    root createChild("newConn", "static") serializable false display "Add Connection" action addConnection build()
    root.children.values filter isConnectionNode foreach initConnNode
    log.info("Node hierarchy initialized")
  }

  /**
    * Initializes the connection node.
    *
    * @param node
    */
  private def initConnNode(node: Node) = {
    val name = node.getName
    log.debug(s"Initializing connection node [$name]")

    implicit val token = AccessToken(node.configurations(keyToken).toString, null)

    service.getEnvironment foreach { env =>
      env.thermostats.values foreach { pct =>
        NodeFactory.createThermostatNode(node, pct)
        log.info(s"Thermostat node [${pct.deviceId + " - " + pct.name}] created")
      }
    }

    node createChild("removeConn", null) serializable false display "Remove Connection" action removeConnection build()
    log.info(s"Connection node [$name] initialized")
  }

  /* actions */

  /**
    * Adds a connection node.
    */
  lazy val addConnection = createAction(
    parameters = List(STRING("name") description "Account name", STRING("token") description "Nest API access token"),
    handler = event => {
      val parent = event.getNode.getParent

      val name = event.getParam[String]("name", !_.isEmpty, "Name cannot be empty").trim
      val token = event.getParam[String]("token", !_.isEmpty, "Token cannot be empty").trim

      val node = parent createChild(name, null) config(keyNodeType -> cfgConn, keyToken -> token) build()
      initConnNode(node)

      log.info(s"Connection [$name] created")
    }
  )

  /**
    * Removes a Nest connection from DSA tree.
    */
  lazy val removeConnection: ActionHandler = event => {
    val node = event.getNode.getParent
    val name = node.getName

    node.delete(true)

    log.info(s"Connection [$name] removed")
  }
}
