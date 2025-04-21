package ru.yandex.practicum.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.service.OrderService;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/order")
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request) throws FeignException {
        log.info("Получен запрос на создание заказа: {}", request);
        OrderDto response = orderService.createOrder(request);
        log.info("Сформирован ответ на запрос на создание заказа: {}", response);
        return response;
    }

    @Override
    public Collection<OrderDto> getUsersOrders(String username) throws FeignException {
        log.info("Получен запрос на получение заказов пользователя {}", username);
        Collection<OrderDto> response = orderService.getUsersOrders(username);
        log.info("Сформирован ответ на запрос на получение заказов пользователя {}: {}", username, response);
        return response;
    }

    @Override
    public OrderDto returnOrderProducts(ProductReturnRequest request) throws FeignException {
        log.info("Получен запрос на возврат товаров: {}", request);
        OrderDto response = orderService.returnOrderProducts(request);
        log.info("Сформирован ответ на запрос на возврат товаров: {}", response);
        return response;
    }

    @Override
    public OrderDto deliverySuccessful(UUID orderId) throws FeignException {
        log.info("Получен запрос на изменение статуса успешной доставки для заказа {}", orderId);
        OrderDto response = orderService.setOrderDeliverySuccessful(orderId);
        log.info("Сформирован ответ на изменение статуса успешной доставки для заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) throws FeignException {
        log.info("Получен запрос на изменение статуса невыполненной доставки для заказа {}", orderId);
        OrderDto response = orderService.setOrderDeliveryFailed(orderId);
        log.info("Сформирован ответ на изменение статуса невыполненной доставки для заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto assemblySuccessful(UUID orderId) throws FeignException {
        log.info("Получен запрос на изменение статуса доставляется для заказа {}", orderId);
        OrderDto response = orderService.setOrderDeliveryInProgress(orderId);
        log.info("Сформирован ответ на изменение статуса доставляется для заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) throws FeignException {
        log.info("Получен запрос на изменение статуса неуспешной сборки для заказа {}", orderId);
        OrderDto response = orderService.setOrderDeliveryAssemblyFailed(orderId);
        log.info("Сформирован ответ на изменение статуса неуспешной сборки для заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto createOrderPayment(UUID orderId) throws FeignException {
        log.info("Получен запрос на оплату заказа {}", orderId);
        OrderDto response = orderService.createOrderPayment(orderId);
        log.info("Сформирован ответ на оплату заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto paymentSuccessful(UUID orderId) throws FeignException {
        log.info("Получен запрос на изменение статуса оплаты как успешную заказа {}", orderId);
        OrderDto response = orderService.setOrderPaid(orderId);
        log.info("Сформирован ответ на изменение статуса оплаты как успешную заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) throws FeignException {
        log.info("Получен запрос на изменение статуса оплаты как неуспешную заказа {}", orderId);
        OrderDto response = orderService.setOrderPaymentFailed(orderId);
        log.info("Сформирован ответ на изменение статуса оплаты как неуспешную заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto calculateProductCost(UUID orderId) throws FeignException {
        log.info("Получен запрос на подсчет стоимости товаров заказа {}", orderId);
        OrderDto response = orderService.calculateProductCost(orderId);
        log.info("Сформирован ответ на подсчет стоимости товаров заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) throws FeignException {
        log.info("Получен запрос на подсчет стоимости доставки заказа {}", orderId);
        OrderDto response = orderService.calculateDeliveryCost(orderId);
        log.info("Сформирован ответ на подсчет стоимости доставки заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) throws FeignException {
        log.info("Получен запрос на подсчет полной стоимости заказа {}", orderId);
        OrderDto response = orderService.calculateTotalCost(orderId);
        log.info("Сформирован ответ на подсчет полной стоимости заказа {}: {}", orderId, response);
        return response;
    }

    @Override
    public OrderDto completeOrder(UUID orderId) throws FeignException {
        log.info("Получен запрос на изменение статуса как выполнен для заказа {}", orderId);
        OrderDto response = orderService.completeOrder(orderId);
        log.info("Сформирован ответ на изменение статуса как выполнен для заказа {}: {}", orderId, response);
        return response;
    }
}
