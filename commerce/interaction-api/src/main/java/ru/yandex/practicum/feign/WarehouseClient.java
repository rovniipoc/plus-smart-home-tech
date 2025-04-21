package ru.yandex.practicum.feign;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(
        path = "/api/v1/warehouse",
        name = "warehouse",
        fallback = WarehouseClientFallback.class
)
public interface WarehouseClient {
    @PostMapping("/check")
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(@RequestBody ShoppingCartDto cart) throws FeignException;

    @GetMapping("/address")
    AddressDto getWarehouseAddress() throws FeignException;

    @PostMapping("/return")
    void returnProducts(@RequestBody Map<UUID, Long> products) throws FeignException;

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductsForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest request) throws FeignException;

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request);

}