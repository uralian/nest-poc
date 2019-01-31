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
import com.uralian.nest.model.{StreamThermostatData, StreamThermostatDataSerializer, ThermostatSerializer}
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
                                      ex: ExecutionContext) extends StreamSink[StreamThermostatData] {
  val log = LoggerFactory.getLogger(getClass)

  implicit val formats = DefaultFormats + ThermostatSerializer + StreamThermostatDataSerializer
  implicit val materializer: Materializer = ActorMaterializer()

  val producerSettings =
    ProducerSettings(config, new StringSerializer, new StringSerializer)
  val topic = config.getString("kafka.topic")

  /**
    * Transform StreamThermostatData into a String
    *
    */
  val StreamThermostatDataToRecord = Flow[StreamThermostatData].
    map(value => new ProducerRecord[String, String](topic, write(value)))

  /**
    * Store source data into Kafka sink
    *
    * @param source
    * @return
    */
  def sink(source: Source[StreamThermostatData, Any]): Future[Done] = source.
    filter(s => s.valid.equals(Some(true))).
    via(StreamThermostatDataToRecord).
    runWith(Producer.plainSink(producerSettings)).andThen {
      case Failure(t) => log.error("Error while sending data to kafka", t)
      case Success(v) => log.trace("Value inserted")
    }

}

