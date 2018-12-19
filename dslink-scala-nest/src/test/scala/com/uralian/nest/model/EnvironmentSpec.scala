package com.uralian.nest.model

import com.uralian.nest.AbstractUnitSpec
import org.json4s._
import org.json4s.native.JsonMethods._

/**
  * Environment test suite.
  */
class EnvironmentSpec extends AbstractUnitSpec {

  implicit val formats = DefaultFormats +
    ThermostatSerializer + WhereSerializer + StructureSerializer + EnvironmentSerializer

  val json = parse(getClass.getResourceAsStream("/devices.json"))

  "EnvironmentSerializer" should {
    "de-serialize devices and structures from JSON" in {
      val environment = json.extract[Environment]
      environment.userId mustBe "z.1.1.ajVtWTdGjfxH3zys4T7QS44SUvrguChdLWwx+8f1fW8="
      environment.clientVersion mustBe 1
      environment.thermostats.keys mustBe Set(
        "Gp_Mnef0pHHq_euH-ZjsbFHG0PjbkGTo", "Gp_Mnef0pHGEHI_ylxz0LFHG0PjbkGTo",
        "Gp_Mnef0pHHZno317X9NiVHG0PjbkGTo", "Gp_Mnef0pHEVeS8ShlWAc1HG0PjbkGTo"
      )
      environment.structures.keys mustBe Set("4bI0rz4GwpcqkLvf9tf821t3cAvm22Qpexlpj0igWD0AfRDVV-ldDg")
    }
  }
}
