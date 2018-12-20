package com.uralian.nest.model

/**
  * HVAC mode.
  */
sealed trait HvacMode

/**
  * Available HVAC modes.
  */
object HvacMode {

  case object Heat extends HvacMode

  case object Cool extends HvacMode

  case object Heat_Cool extends HvacMode

  case object Eco extends HvacMode

  case object Off extends HvacMode

  /**
    * Resolves the HVAC mode by its name (case insensitive).
    *
    * @param name
    * @return
    */
  def forName(name: String): HvacMode = name.toLowerCase match {
    case "heat"      => Heat
    case "cool"      => Cool
    case "heat-cool" => Heat_Cool
    case "eco"       => Eco
    case "off"       => Off
  }
}