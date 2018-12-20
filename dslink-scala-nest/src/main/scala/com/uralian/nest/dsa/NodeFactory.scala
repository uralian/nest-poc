package com.uralian.nest.dsa

import com.uralian.dsa._
import com.uralian.nest.model.Thermostat
import org.dsa.iot.dslink.node.Node
import squants.thermal.{Temperature, TemperatureScale}

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

    createValueNode(node, "hvacState", "HVAC State", pct.hvacState)

    createValueNode(node, "humidity", "Humidity", pct.humidity, Some("%"))

    createValueNode(node, "usingEmergencyHeat", "Emergency Heat On", pct.usingEmergencyHeat)

    createValueNode(node, "hasFan", "Has Fan", pct.hasFan)

    createValueNode(node, "canHeat", "Can Heat", pct.canHeat)

    createValueNode(node, "canCool", "Can Cool", pct.canCool)

    createTemperatureNode(node, "targetTemperature", "Target Temperature", pct.targetTemperature, pct.temperatureScale)

    createTemperatureNode(node, "targetTemperatureHigh", "Target Temperature High", pct.targetTemperatureHigh, pct.temperatureScale)

    createTemperatureNode(node, "targetTemperatureLow", "Target Temperature Low", pct.targetTemperatureLow, pct.temperatureScale)

    createTemperatureNode(node, "ambientTemperature", "Ambient Temperature", pct.ambientTemperature, pct.temperatureScale)

    createTemperatureNode(node, "awayTemperatureHigh", "Away Temperature High", pct.awayTemperatureHigh, pct.temperatureScale)

    createTemperatureNode(node, "awayTemperatureLow", "Away Temperature Low", pct.awayTemperatureLow, pct.temperatureScale)

    createValueNode(node, "locked", "Locked", pct.locked)

    createTemperatureNode(node, "lockedTemperatureMin", "Locked Temperature Min", pct.lockedTemperatureMin, pct.temperatureScale)

    createTemperatureNode(node, "lockedTemperatureMax", "Locked Temperature Max", pct.lockedTemperatureMax, pct.temperatureScale)

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

  /**
    * Creates a temperature value node, adjusting its value to the specified scale.
    *
    * @param parent
    * @param name
    * @param displayName
    * @param temperature
    * @param scale
    * @return
    */
  def createTemperatureNode(parent: Node, name: String, displayName: String,
                            temperature: Temperature, scale: TemperatureScale) = {
    val scaled = temperature.in(scale)
    createValueNode(parent, name, displayName, scaled.value, Some(scaled.unit.symbol))
  }
}
