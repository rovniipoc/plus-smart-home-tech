package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.DeliveryState;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    private static final double BASE_RATE = 5.0;
    private static final double WAREHOUSE_1_ADDRESS_MULTIPLIER = 1;
    private static final double WAREHOUSE_2_ADDRESS_MULTIPLIER = 2;
    private static final double FRAGILE_MULTIPLIER = 0.2;
    private static final double WEIGHT_MULTIPLIER = 0.3;
    private static final double VOLUME_MULTIPLIER = 0.2;
    private static final double STREET_MULTIPLIER = 0.2;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = DeliveryMapper.toDelivery(deliveryDto);
        delivery = deliveryRepository.save(delivery);
        log.info("Новая доставка создана: {}", delivery);
        return DeliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    @Transactional
    public DeliveryDto completeDelivery(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        delivery = deliveryRepository.save(delivery);
        orderClient.deliverySuccessful(delivery.getOrderId());
        log.info("Доставка {} успешно отправлена", deliveryId);
        return DeliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    @Transactional
    public DeliveryDto deliveryFailed(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        delivery = deliveryRepository.save(delivery);
        orderClient.deliveryFailed(delivery.getOrderId());
        log.info("Доставка {} не удалась", deliveryId);
        return DeliveryMapper.toDeliveryDto(delivery);
    }

    @Override
    public Double calculateDeliveryCost(OrderDto order) {
        Delivery delivery = getDelivery(order.getDeliveryId());
        Address warehouseAddress = delivery.getFromAddress();
        Address destinationAddress = delivery.getToAddress();

        double totalCost = BASE_RATE;

        totalCost += warehouseAddress.getCity().equals("ADDRESS_1")
                ? totalCost * WAREHOUSE_1_ADDRESS_MULTIPLIER : totalCost * WAREHOUSE_2_ADDRESS_MULTIPLIER;

        totalCost += Boolean.TRUE.equals(order.getFragile()) ? totalCost * FRAGILE_MULTIPLIER : 0;

        totalCost += order.getDeliveryWeight() * WEIGHT_MULTIPLIER;

        totalCost += order.getDeliveryVolume() * VOLUME_MULTIPLIER;

        totalCost += warehouseAddress.getStreet().equals(destinationAddress.getStreet())
                ? 0 : totalCost * STREET_MULTIPLIER;

        log.info("Стоимость доставки рассчитана: {}", totalCost);
        return totalCost;
    }

    @Override
    @Transactional
    public DeliveryDto setDeliveryPicked(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        UUID orderId = delivery.getOrderId();
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        warehouseClient.shippedToDelivery(new ShippedToDeliveryRequest(orderId, deliveryId));
        orderClient.assemblySuccessful(orderId);
        delivery = deliveryRepository.save(delivery);
        log.info("Доставка {} выполняется", deliveryId);
        return DeliveryMapper.toDeliveryDto(delivery);
    }

    private Delivery getDelivery(UUID id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Доставка " + id + " не найдена"));
    }
}
