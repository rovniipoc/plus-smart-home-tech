package ru.yandex.practicum.event.model.hub_event.scenario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceAction {

    @NotBlank
    private String sensorId;

    @NotBlank
    private ActionType type;

    private Integer value;
}
