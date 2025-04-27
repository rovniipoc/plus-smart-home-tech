package ru.yandex.practicum.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto planDelivery(DeliveryDto delivery) throws FeignException {
        log.info("Получен запрос на создание доставки: {}", delivery);
        DeliveryDto result = deliveryService.createDelivery(delivery);
        log.info("Сформирован ответ на создание доставки: {}", result);
        return result;
    }

    @Override
    public DeliveryDto finishDelivery(UUID deliveryId) throws FeignException {
        log.info("Получен запрос на изменение успешного статуса доставки {}", deliveryId);
        DeliveryDto result = deliveryService.completeDelivery(deliveryId);
        log.info("Сформирован ответ на изменение успешного статуса доставки {}: {}", deliveryId, result);
        return result;
    }

    @Override
    public DeliveryDto deliveryFailed(UUID deliveryId) throws FeignException {
        log.info("Получен запрос на изменение неуспешного статуса доставки {}", deliveryId);
        DeliveryDto result = deliveryService.deliveryFailed(deliveryId);
        log.info("Сформирован ответ на изменение неуспешного статуса доставки {}: {}", deliveryId, result);
        return result;
    }

    @Override
    public double calculateDeliveryCost(OrderDto order) throws FeignException {
        log.info("Получен запрос на подсчет стоимости доставки заказа {}", order);
        double result = deliveryService.calculateDeliveryCost(order);
        log.info("Сформирован ответ на подсчет стоимости доставки заказа {}: {}", order.getOrderId(), result);
        return result;
    }

    @Override
    public DeliveryDto setDeliveryPicked(UUID deliveryId) throws FeignException {
        log.info("Получен запрос на изменение статуса выполняется для доставки {}", deliveryId);
        DeliveryDto result = deliveryService.setDeliveryPicked(deliveryId);
        log.info("Сформирован ответ на изменение статуса выполняется для доставки {}: {}", deliveryId, result);
        return result;
    }
}
