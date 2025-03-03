package ru.yandex.practicum.event.model.sensor_event;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    @NotBlank
    private int linkQuality;
    @NotBlank
    private boolean motion;
    @NotBlank
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
