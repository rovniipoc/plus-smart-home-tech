package ru.yandex.practicum.feign;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.ServiceNotAvailableException;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class WarehouseClientFallback implements WarehouseClient {
    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart) throws FeignException {
        log.error("Сервис warehouse недоступен. Повторите запрос позже.");
        throw new ServiceNotAvailableException("Сервис warehouse недоступен. Повторить запрос позже");
    }

    @Override
    public AddressDto getWarehouseAddress() throws FeignException {
        log.error("Сервис warehouse недоступен. Повторите запрос позже.");
        throw new ServiceNotAvailableException("Сервис warehouse недоступен. Повторить запрос позже");
    }

    @Override
    public void returnProducts(Map<UUID, Long> products) throws FeignException {
        log.error("Сервис warehouse недоступен. Повторите запрос позже.");
        throw new ServiceNotAvailableException("Сервис warehouse недоступен. Повторить запрос позже");
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderRequest request) throws FeignException {
        log.error("Сервис warehouse недоступен. Повторите запрос позже.");
        throw new ServiceNotAvailableException("Сервис warehouse недоступен. Повторить запрос позже");
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        log.error("Сервис warehouse недоступен. Повторите запрос позже.");
        throw new ServiceNotAvailableException("Сервис warehouse недоступен. Повторить запрос позже");
    }
}
