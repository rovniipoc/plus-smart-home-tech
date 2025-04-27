package ru.yandex.practicum.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    PaymentDto createPayment(@RequestBody @Valid OrderDto order) throws FeignException;

    @PostMapping("/totalCost")
    double calculateTotalCost(@RequestBody @Valid OrderDto order) throws FeignException;

    @PostMapping("/productCost")
    double calculateProductCost(@RequestBody @Valid OrderDto order) throws FeignException;

    @PostMapping("/refund")
    void setPaymentSuccessful(@RequestBody UUID paymentId) throws FeignException;

    @PostMapping("/failed")
    void setPaymentFailed(@RequestBody UUID paymentId) throws FeignException;

}
