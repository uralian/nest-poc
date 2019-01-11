package com.uralian.nest.streaming

import java.io.StringWriter

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.{Flow, Framing, Source}
import akka.util.ByteString
import com.typesafe.config.Config
import com.uralian.nest.model.{StreamData, StreamDataSerializer, ThermostatSerializer}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class KafkaStreamSink(config: Config)(implicit system: ActorSystem,
                                      ex: ExecutionContext) extends StreamSink[StreamData] {
  val log = LoggerFactory.getLogger(getClass)

  implicit val formats = DefaultFormats + ThermostatSerializer + StreamDataSerializer
  implicit val materializer: Materializer = ActorMaterializer()

  val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
  val bracket = "{"
  val dummyStreamData = StreamData("", Map.empty, Some(false))
  val topic = config.getString("kafka.topic")

  /**
    * Tries to parse a string into a valid StreamData object, if it fails then returns an invalid StreamData object
    */
  val dataSerializer: Flow[String, StreamData, NotUsed] = {
    Flow[String].map( line => {
      try {
        val extract = line.substring(line.indexOf(bracket), line.length)
        val extracted = parse(extract).extract[StreamData]
        extracted
      } catch {
        case e: Exception => println(line); println(e); dummyStreamData
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
    map(value => new ProducerRecord[String, String](topic, write(value)))

  /**
    * Store source data into Kafka sink
    *
    * @param source
    * @return
    */
  def sink(source: Source[ByteString, NotUsed]): Future[Done] = source.
    via(converter).
    via(streamDataToRecord).
    runWith(Producer.plainSink(producerSettings)).andThen {
      case Failure(t) => log.error("Error while sending data to kafka", t)
      case Success(v) => log.trace("Value inserted")
    }

}
