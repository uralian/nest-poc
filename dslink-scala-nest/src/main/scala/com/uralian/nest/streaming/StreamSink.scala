package com.uralian.nest.streaming

import akka.{Done, NotUsed}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future

trait StreamSink[T] {
  def sink(source: Source[ByteString, NotUsed]): Future[Done]
  val converter: Flow[ByteString, T, NotUsed]
}