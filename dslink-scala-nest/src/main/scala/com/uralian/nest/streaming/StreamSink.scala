package com.uralian.nest.streaming

import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import com.uralian.nest.model.StreamThermostatData

import scala.concurrent.Future

trait StreamSink[T] {
  def sink(source: Source[StreamThermostatData, Any]): Future[Done]
}