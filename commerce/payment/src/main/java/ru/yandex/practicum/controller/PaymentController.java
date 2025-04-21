package ru.yandex.practicum.controller;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @Override
    public PaymentDto createPayment(OrderDto order) throws FeignException {
        log.info("Получен запрос на создание платежа для заказа {}", order.getOrderId());
        PaymentDto response = paymentService.createPayment(order);
        log.info("Сформирован ответ на создание платежа для заказа {}: {}", order.getOrderId(), response);
        return response;
    }

    @Override
    public double calculateProductCost(OrderDto order) throws FeignException {
        log.info("Получен запрос на подсчет стоимости товаров заказа {}", order.getOrderId());
        double response = paymentService.calculateProductCost(order);
        log.info("Сформирован ответ на подсчет стоимости товаров заказа {}: {}", order.getOrderId(), response);
        return response;
    }

    @Override
    public double calculateTotalCost(OrderDto order) throws FeignException {
        log.info("Получен запрос на подсчет полной стоимости заказа {}", order.getOrderId());
        double response = paymentService.calculateTotalCost(order);
        log.info("Сформирован ответ на подсчет полной стоимости заказа {}: {}", order.getOrderId(), response);
        return response;
    }

    @Override
    public void setPaymentSuccessful(UUID paymentId) throws FeignException {
        log.info("Получен запрос на изменение успешного статуса для платежа {}", paymentId);
        paymentService.setPaymentSuccessful(paymentId);
        log.info("Выполнен запрос на изменение успешного статуса для платежа {}", paymentId);
    }

    @Override
    public void setPaymentFailed(UUID paymentId) throws FeignException {
        log.info("Получен запрос на изменение неуспешного статуса для платежа {}", paymentId);
        paymentService.setPaymentFailed(paymentId);
        log.info("Выполнен запрос на изменение неуспешного статуса для платежа {}", paymentId);
    }
}
