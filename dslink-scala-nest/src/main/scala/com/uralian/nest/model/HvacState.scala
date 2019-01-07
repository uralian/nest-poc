package com.uralian.nest.model

/**
  * HVAC state.
  */
sealed trait HvacState

/**
  * Available HVAC states.
  */
object HvacState {

  case object Heat extends HvacState

  case object Cool extends HvacState

  case object Off extends HvacState

  /**
    * Resolves the HVAC state by its name (case insensitive).
    *
    * @param name
    * @return
    */
  def forName(name: String): HvacState = name.toLowerCase match {
    case "heating" => Heat
    case "heat" => Heat
    case "cooling" => Cool
    case "cool" => Cool
    case "off"  => Off
  }
}