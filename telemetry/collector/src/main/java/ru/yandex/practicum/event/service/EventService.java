package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.event.kafka_client.KafkaClient;
import ru.yandex.practicum.event.mapper.SensorEventMapper;
import ru.yandex.practicum.event.model.sensor_event.SensorEvent;

@Service
@RequiredArgsConstructor
public class EventService {

    private final KafkaClient kafkaClient;

    private static final String SENSORS_TOPIC = "telemetry.sensors.v1";

    public void sendSensorEvent(SensorEvent event) {
        kafkaClient.getProducer().send(new ProducerRecord<>(
                SENSORS_TOPIC,
                null,
                event.getTimestamp().toEpochMilli(),
                null,
                SensorEventMapper.toSensorEventAvro(event))
        );
    }

}
