package ru.yandex.practicum.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.repository.ScenarioRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;

    @Override
    public String getEventType() {
        return ScenarioAddedEventAvro.class.getName();
    }

    @Transactional
    @Override
    public void handle(HubEventAvro event) {

        log.info("Поступил для сохранения scenario: {}", event);

        ScenarioAddedEventAvro scenarioEvent = (ScenarioAddedEventAvro) event.getPayload();
        Scenario scenario = new Scenario();
        scenario.setHubId(event.getHubId());
        scenario.setName(scenario.getName());

        scenarioRepository.save(scenario);
        log.info("В БД сохранен scenario: {}", scenario);
    }
}
