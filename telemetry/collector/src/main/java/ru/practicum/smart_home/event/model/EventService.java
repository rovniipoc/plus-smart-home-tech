package ru.practicum.smart_home.event.model;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.smart_home.event.kafka_client.KafkaClient;
import ru.practicum.smart_home.event.model.sensor_event.SensorEvent;

@Service
@RequiredArgsConstructor
public class EventService {

    private final KafkaClient client;

    public void sendSensorEvent(SensorEvent event, String topic) {

        ProducerRecord<String, SensorEvent> record = new ProducerRecord<>(topic, event);

        client.getProducer().send(record);
    }

}
