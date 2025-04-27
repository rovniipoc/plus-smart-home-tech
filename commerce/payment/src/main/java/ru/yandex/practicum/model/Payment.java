package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.dto.PaymentState;

import java.util.UUID;

@Entity
@Data
@Table(name = "payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    UUID paymentId;

    UUID orderId;

    double totalPayment;

    double deliveryTotal;

    double feeTotal;

    @Enumerated(EnumType.STRING)
    PaymentState paymentState;

}
