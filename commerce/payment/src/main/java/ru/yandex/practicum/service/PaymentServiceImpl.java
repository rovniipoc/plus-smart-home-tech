package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.PaymentState;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NotEnoughInfoException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.feign.OrderClient;
import ru.yandex.practicum.feign.ShoppingStoreClient;
import ru.yandex.practicum.mapper.PaymentMapper;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;
    private static final double VAT_RATE = 0.20;

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto order) {
        validatePaymentInfo(order.getProductPrice(), order.getDeliveryPrice(), order.getTotalPrice());
        Payment payment = PaymentMapper.toPayment(order);
        payment = paymentRepository.save(payment);
        log.info("Платеж создан: {}", payment);
        return PaymentMapper.toPaymentDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateProductCost(OrderDto order) {
        List<Double> pricesList = new ArrayList<>();
        Map<UUID, Long> orderProducts = order.getProducts();

        orderProducts.forEach((id, quantity) -> {
            ProductDto product = shoppingStoreClient.getProducts(id);
            double totalProductPrice = product.getPrice() * quantity;
            pricesList.add(totalProductPrice);
        });

        double totalProductCost = pricesList.stream().mapToDouble(Double::doubleValue).sum();
        log.info("Подсчитана общая стоимость товаров: {}", totalProductCost);
        return totalProductCost;
    }

    @Override
    public double calculateTotalCost(OrderDto order) {
        validatePaymentInfo(order.getProductPrice(), order.getDeliveryPrice());
        double productsPrice = order.getProductPrice();
        double deliveryPrice = order.getDeliveryPrice();
        double totalCost = deliveryPrice + productsPrice + (productsPrice * VAT_RATE);
        log.info("Подсчитана общая стоимость: {}", totalCost);
        return totalCost;
    }

    @Override
    @Transactional
    public void setPaymentSuccessful(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setPaymentState(PaymentState.SUCCESS);
        orderClient.paymentSuccessful(payment.getOrderId());
        paymentRepository.save(payment);
        log.info("Статус платежа {} успешен", paymentId);
    }

    @Override
    @Transactional
    public void setPaymentFailed(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        payment.setPaymentState(PaymentState.FAILED);
        orderClient.paymentFailed(payment.getOrderId());
        paymentRepository.save(payment);
        log.info("Статус платежа {} неуспешен", paymentId);
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Платеж c id = " + paymentId + " не найден"));
    }

    private void validatePaymentInfo(Double... prices) {
        for (Double price : prices) {
            if (price == null || price == 0) {
                log.warn("Одна или несколько статей расходов отсутствует");
                throw new NotEnoughInfoException("Одна или несколько статей расходов отсутствует");
            }
        }
    }
}
