package ru.yandex.practicum.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProductsDto {

    UUID bookingId;

    Double deliveryWeight;

    Double deliveryVolume;

    boolean fragile;

    Map<UUID, Integer> products;

    UUID orderId;

    UUID deliveryId;
}
