package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "conditions")
@SecondaryTable(
        name = "scenario_conditions",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "condition_id")
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ConditionType type;

    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    private ConditionOperation operation;

    @Column(name = "value")
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "sensor_id", table = "scenario_conditions")
    Sensor sensor;

    @ManyToMany
    @JoinTable(
            name = "scenario_conditions",
            joinColumns = @JoinColumn(name = "scenario_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    List<Scenario> scenarios;

}
