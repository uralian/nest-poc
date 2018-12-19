package com.uralian.nest.model

import java.util.TimeZone

import com.uralian.nest.AbstractUnitSpec
import org.json4s._
import org.json4s.native.JsonMethods._

/**
  * Structure test suite.
  */
class StructureSpec extends AbstractUnitSpec {

  implicit val formats = DefaultFormats + ThermostatSerializer + WhereSerializer + StructureSerializer

  val json = parse(getClass.getResourceAsStream("/devices.json"))

  "WhereSerializer" should {
    "de-serialize Where from JSON" in {
      val wheres = json \\ "wheres"
      val where = wheres.extract[Map[String, Where]].head._2
      where mustBe Where(whereId = "KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SxEI_X6fnhtQQ", name = "Basement")
    }
  }

  "StructureSerializer" should {
    "de-serialize Structure from JSON" in {
      val structures = json \\ "structures"
      val structure = structures.extract[Map[String, Structure]].head._2
      structure mustBe Structure(
        structureId = "4bI0rz4GwpcqkLvf9tf821t3cAvm22Qpexlpj0igWD0AfRDVV-ldDg",
        name = "Headquarters",
        countryCode = "US",
        timeZone = TimeZone.getTimeZone("America/New_York"),
        away = AwayState.Home,
        thermostatIds = List(
          "Gp_Mnef0pHHq_euH-ZjsbFHG0PjbkGTo", "Gp_Mnef0pHGEHI_ylxz0LFHG0PjbkGTo",
          "Gp_Mnef0pHHZno317X9NiVHG0PjbkGTo", "Gp_Mnef0pHEVeS8ShlWAc1HG0PjbkGTo"
        ),
        wheres = Map(
          "KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SxEI_X6fnhtQQ" ->
            Where("KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SxEI_X6fnhtQQ", "Basement"),
          "KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SyOAsImm-94Xg" ->
            Where("KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SyOAsImm-94Xg", "Bedroom"),
          "KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SyRq5RNksxcXg" ->
            Where("KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SyRq5RNksxcXg", "Living Room"),
          "KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SwD2fkFCnicqg" ->
            Where("KbOeS7kwwLhnFiLbbX-60DLnUaloXURWwntI8gHF6SwD2fkFCnicqg", "Office")
        )
      )
    }
  }
}
