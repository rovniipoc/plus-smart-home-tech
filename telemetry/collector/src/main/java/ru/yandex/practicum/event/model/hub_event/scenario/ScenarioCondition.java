package ru.yandex.practicum.event.model.hub_event.scenario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScenarioCondition {

    @NotBlank
    private String sensorId;

    @NotBlank
    private ConditionType type;

    @NotBlank
    private OperationType operation;

    int value;
}
