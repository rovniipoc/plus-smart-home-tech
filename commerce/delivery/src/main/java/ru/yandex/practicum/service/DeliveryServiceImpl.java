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

        double totalCost;

//        Коэффициент к стоимости, зависящий от адреса склада:
//        Если адрес склада содержит название ADDRESS_1, то К = 1 + WAREHOUSE_1_ADDRESS_MULTIPLIER
//        Если адрес склада содержит название ADDRESS_2, то К = 1 + WAREHOUSE_2_ADDRESS_MULTIPLIER
        double addressMultiplier = warehouseAddress.getCity().equals("ADDRESS_1")
                ? 1 + WAREHOUSE_1_ADDRESS_MULTIPLIER : 1 + WAREHOUSE_2_ADDRESS_MULTIPLIER;
//        Коэффициент к стоимости, зависящий от хрупкости:
//        Если в заказе есть признак хрупкости, то К = 1 + FRAGILE_MULTIPLIER, иначе К = 1
        double fragileMultiplier = Boolean.TRUE.equals(order.getFragile()) ? 1 + FRAGILE_MULTIPLIER : 1;

        double weightCost = order.getDeliveryWeight() * WEIGHT_MULTIPLIER;
        double volumeCost = order.getDeliveryVolume() * VOLUME_MULTIPLIER;

//        Если нужно доставить на ту же улицу, где находится склад (улица доставки совпадает с адресом склада),
//        то стоимость доставки не увеличивается (К = 1). Иначе К = 1 + STREET_MULTIPLIER
        double streetMultiplier = warehouseAddress.getStreet().equals(destinationAddress.getStreet())
                ? 1 : 1 + STREET_MULTIPLIER;

        totalCost = (BASE_RATE * addressMultiplier * fragileMultiplier + weightCost + volumeCost) * streetMultiplier;

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
