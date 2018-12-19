package com.uralian.nest.model

/**
  * Away state.
  */
sealed trait AwayState

/**
  * Available Away states.
  */
object AwayState {

  case object Home extends AwayState

  case object Away extends AwayState

  /**
    * Resolves the Away state by its name (case insensitive).
    *
    * @param name
    * @return
    */
  def forName(name: String) = name.toLowerCase match {
    case "home" => Home
    case "away" => Away
  }
}
