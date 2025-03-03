package ru.yandex.practicum.event.mapper;

import ru.yandex.practicum.event.model.sensor_event.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class SensorEventMapper {

    public static SensorEventAvro toSensorEventAvro(SensorEvent sensorEvent) {

        Object payload;

        switch (sensorEvent) {
            case MotionSensorEvent event -> {
                payload = MotionSensorAvro.newBuilder()
                        .setLinkQuality(event.getLinkQuality())
                        .setMotion(event.isMotion())
                        .setVoltage(event.getVoltage())
                        .build();
            }
            case ClimateSensorEvent event -> {
                payload = ClimateSensorAvro.newBuilder()
                        .setCo2Level(event.getCo2Level())
                        .setHumidity(event.getHumidity())
                        .setTemperatureC(event.getTemperatureC())
                        .build();
            }
            case LightSensorEvent event -> {
                payload = LightSensorAvro.newBuilder()
                        .setLinkQuality(event.getLinkQuality())
                        .setLuminosity(event.getLuminosity())
                        .build();
            }
            case SwitchSensorEvent event -> {
                payload = SwitchSensorAvro.newBuilder()
                        .setState(event.isState())
                        .build();
            }
            case TemperatureSensorEvent event -> {
                payload = TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(event.getTemperatureC())
                        .setTemperatureF(event.getTemperatureF())
                        .build();
            }
            default -> {
                throw new RuntimeException("Ошибка конвертации события датчика: " + sensorEvent);
            }
        }

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(payload)
                .build();
    }
}
