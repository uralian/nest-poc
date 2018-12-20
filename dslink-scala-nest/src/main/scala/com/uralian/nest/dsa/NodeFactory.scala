package com.uralian.nest.dsa

import com.uralian.dsa._
import com.uralian.nest.model.Thermostat
import org.dsa.iot.dslink.node.Node

/**
  * Factory for DSA nodes.
  */
object NodeFactory {

  /**
    * Creates a new Thermostat node.
    *
    * @param parent
    * @param pct
    * @return
    */
  def createThermostatNode(parent: Node, pct: Thermostat) = {
    val node = parent.createChild(pct.deviceId, null)
      .display(pct.name)
      .serializable(false)
      .config("label" -> pct.label, "nameLong" -> pct.nameLong, "structureId" -> pct.structureId,
        "temperatureScale" -> pct.temperatureScale.symbol, "locale" -> pct.locale.toLanguageTag,
        "whereId" -> pct.whereId, "where" -> pct.whereName)
      .build()

    createValueNode(node, "hvacMode", "HVAC Mode", pct.hvacMode)

    createValueNode(node, "humidity", "Humidity", pct.humidity, Some("%"))

    createValueNode(node, "usingEmergencyHeat", "Emergency Heat On", pct.usingEmergencyHeat)

    createValueNode(node, "hasFan", "Has Fan", pct.hasFan)

    createValueNode(node, "canHeat", "Can Heat", pct.canHeat)

    createValueNode(node, "canCool", "Can Cool", pct.canCool)

    createValueNode(node, "targetTemperature", "Target Temperature",
      pct.targetTemperature.value, Some(pct.targetTemperature.unit.symbol))

    createValueNode(node, "targetTemperatureHigh", "Target Temperature High",
      pct.targetTemperatureHigh.value, Some(pct.targetTemperatureHigh.unit.symbol))

    createValueNode(node, "targetTemperatureLow", "Target Temperature Low",
      pct.targetTemperatureLow.value, Some(pct.targetTemperatureLow.unit.symbol))

    createValueNode(node, "ambientTemperature", "Ambient Temperature",
      pct.ambientTemperature.value, Some(pct.ambientTemperature.unit.symbol))

    createValueNode(node, "awayTemperatureHigh", "Away Temperature High",
      pct.awayTemperatureHigh.value, Some(pct.awayTemperatureHigh.unit.symbol))

    createValueNode(node, "awayTemperatureLow", "Away Temperature Low",
      pct.awayTemperatureLow.value, Some(pct.awayTemperatureLow.unit.symbol))

    createValueNode(node, "locked", "Locked", pct.locked)

    createValueNode(node, "lockedTemperatureMin", "Locked Temperature Min",
      pct.lockedTemperatureMin.value, Some(pct.lockedTemperatureMin.unit.symbol))

    createValueNode(node, "lockedTemperatureMax", "Locked Temperature Max",
      pct.lockedTemperatureMax.value, Some(pct.lockedTemperatureMax.unit.symbol))

    node
  }

  /**
    * Creates a simple value node displaying a single value.
    *
    * @param parent
    * @param name
    * @param displayName
    * @param value
    * @param unit
    * @return
    */
  def createValueNode(parent: Node, name: String, displayName: String, value: Any, unit: Option[String] = None) =
    parent.createChild(name, null).display(displayName).valueType(anyToValue(value).getType)
      .attributes("unit" -> unit.getOrElse("")).value(value).build()
}
