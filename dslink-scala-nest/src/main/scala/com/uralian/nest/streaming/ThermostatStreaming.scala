package com.uralian.nest.streaming

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.uralian.nest.model.{StreamThermostatData, StreamThermostatDataSerializer, ThermostatSerializer}
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods.parse

object ThermostatStreaming {
  implicit val formats = DefaultFormats + ThermostatSerializer + StreamThermostatDataSerializer

  val bracket = "{"
  val dummyStreamData = StreamThermostatData("", Map.empty, Some(false))

  /**
    * Tries to parse a string into a valid StreamData object, if it fails then returns an invalid StreamData object
    */
  val dataSerializer: Flow[String, StreamThermostatData, NotUsed] = {
    Flow[String].map( line => {
      try {
        val extract = line.substring(line.indexOf(bracket), line.length)
        println(extract)
        val extracted = parse(extract).extract[StreamThermostatData]
        extracted
      } catch {
        case e: Exception => println(line); println(e); dummyStreamData
      }
    })
  }
}
