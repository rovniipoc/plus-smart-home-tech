package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sensors")
@Data
public class Sensor { //он же Device

    @Id
    private String id;

    @Column(name = "hub_id")
    private String hubId;

}
