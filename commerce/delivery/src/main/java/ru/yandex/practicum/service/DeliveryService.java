package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto delivery);

    DeliveryDto completeDelivery(UUID deliveryId);

    DeliveryDto deliveryFailed(UUID deliveryId);

    Double calculateDeliveryCost(OrderDto order);

    DeliveryDto setDeliveryPicked(UUID deliveryId);
}
