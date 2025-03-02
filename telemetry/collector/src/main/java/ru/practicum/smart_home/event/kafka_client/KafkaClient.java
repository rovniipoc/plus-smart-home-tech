package ru.practicum.smart_home.event.kafka_client;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

public interface KafkaClient {

    Producer<String, SpecificRecordBase> getProducer();

}
