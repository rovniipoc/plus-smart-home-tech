package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sensors")
@Data
public class Sensor {

    @Id
    private Long id;

    @Column(name = "hub_id")
    private String hubId;

}
