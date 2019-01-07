package com.uralian.nest.streaming

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.{Flow, Framing, Source}
import akka.util.ByteString
import com.typesafe.config.Config
import com.uralian.nest.model.StreamData
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}

import scala.concurrent.Future

class KafkaStreamSink(config: Config)(implicit system: ActorSystem) extends StreamSink[StreamData] {
  implicit val serializer = org.json4s.DefaultFormats + com.uralian.nest.model.StreamDataSerializer
  implicit val materializer: Materializer = ActorMaterializer()

  val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
  val bracket = "{"
  val dummyStreamData = StreamData("", Map.empty, Some(false))

  /**
    * Tries to parse a string into a valid StreamData object, if it fails then returns an invalid StreamData object
    */
  val dataSerializer: Flow[String, StreamData, NotUsed] = {
    Flow[String].map( line => {
      try {
        val extract = line.substring(line.indexOf(bracket), line.length)
        parse(extract).extract[StreamData]
      } catch {
        case e: Exception => dummyStreamData
      }
    })
  }

  /**
    * Breaks incoming ByteString into lines
    *
    */
  val lineDelimiter: Flow[ByteString, ByteString, NotUsed] =
    Framing.delimiter(ByteString("\n"), Int.MaxValue, allowTruncation = true)

  /**
    * Converts from BytString to StreamData
    *
    */
  val converter: Flow[ByteString, StreamData, NotUsed] = Flow[ByteString].
    via(lineDelimiter).
    map(_.utf8String).
    via(dataSerializer).
    filter(s => s.valid.equals(Some(true)))

  /**
    * Transform streamData into a String
    *
    */
  val streamDataToRecord = Flow[StreamData].
    map(value => new ProducerRecord[String, String](config.getString("kafka.topic"), write(value)))

  /**
    * Store source data into Kafka sink
    *
    * @param source
    * @return
    */
  def sink(source: Source[ByteString, NotUsed]): Future[Done] = source.
    via(converter).
    via(streamDataToRecord).
    runWith(Producer.plainSink(producerSettings))

}
