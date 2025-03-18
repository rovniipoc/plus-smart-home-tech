package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Scenario;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
}
