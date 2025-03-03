package ru.yandex.practicum.event.model.sensor_event;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {

    @NotBlank
    private int temperatureC;
    @NotBlank
    private int humidity;
    @NotBlank
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
