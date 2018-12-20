package com.uralian.nest

import com.uralian.dsa._
import org.dsa.iot.dslink.node.Node

/**
  * Types, constants and helper methods for Nest DSA.
  */
package object dsa {

  /* predefined configs */

  val keyNodeType = "nodeType"
  val keyToken = "token"

  val cfgConn = "connection"

  /**
    * Returns the node type as specified by [[keyNodeType]] config.
    *
    * @param node
    * @return
    */
  def getNodeType(node: Node) = node.configurations.get(keyNodeType) map (_.asInstanceOf[String])

  /**
    * Returns true if the node's [[keyNodeType]] config is set to [[cfgConn]]
    *
    * @param node
    * @return
    */
  def isConnectionNode(node: Node) = getNodeType(node) == Some(cfgConn)

}
