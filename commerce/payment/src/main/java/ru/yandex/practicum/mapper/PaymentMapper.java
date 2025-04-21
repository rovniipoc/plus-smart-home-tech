package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.model.Payment;

public class PaymentMapper {

    private static final double VAT_RATE = 0.20;

    public static Payment toPayment(OrderDto order) {
        Payment payment = new Payment();
        payment.setTotalPayment(order.getTotalPrice());
        payment.setDeliveryTotal(order.getDeliveryPrice());
        payment.setFeeTotal(order.getProductPrice() * VAT_RATE);
        payment.setPaymentState(PaymentState.PENDING);

        return payment;
    }

    public static PaymentDto toPaymentDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(payment.getPaymentId());
        paymentDto.setTotalPayment(payment.getTotalPayment());
        paymentDto.setOrderId(payment.getOrderId());
        paymentDto.setPaymentState(payment.getPaymentState());
        paymentDto.setFeeTotal(payment.getFeeTotal());
        paymentDto.setDeliveryTotal(payment.getDeliveryTotal());

        return paymentDto;
    }

}
