package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "actions")
@SecondaryTable(
        name = "scenario_actions",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "action_id")
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ActionType type;

    @Column(name = "value")
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "sensor_id", table = "scenario_actions")
    Sensor sensor;

}
