package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void newProductInWarehouse(AddNewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    void returnProductsToWarehouse(Map<UUID, Long> products);

    BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request);

    void shipToDelivery(ShippedToDeliveryRequest request);
}
