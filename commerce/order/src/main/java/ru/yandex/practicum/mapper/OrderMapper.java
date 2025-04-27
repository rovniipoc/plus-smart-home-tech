package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static Order toOrder(CreateNewOrderRequest request, BookedProductsDto bookedProducts) {
        Order order = new Order();
        order.setState(OrderState.NEW);
        order.setFragile(bookedProducts.isFragile());
        order.setProducts(request.getShoppingCart().getProducts());
        order.setDeliveryId(bookedProducts.getDeliveryId());
        order.setShoppingCartId(request.getShoppingCart().getShoppingCartId());
        order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        order.setDeliveryWeight(bookedProducts.getDeliveryWeight());

        return order;
    }

    public static OrderDto toOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setDeliveryId(order.getDeliveryId());
        orderDto.setFragile(order.getFragile());
        orderDto.setState(order.getState());
        orderDto.setDeliveryWeight(order.getDeliveryWeight());
        orderDto.setDeliveryVolume(order.getDeliveryVolume());
        orderDto.setOrderId(order.getOrderId());
        orderDto.setDeliveryPrice(order.getDeliveryPrice());
        orderDto.setPaymentId(order.getPaymentId());
        orderDto.setProductPrice(order.getProductPrice());
        orderDto.setProducts(order.getProducts());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setShoppingCartId(order.getShoppingCartId());

        return orderDto;
    }

    public static List<OrderDto> toListOrderDto(Iterable<Order> orders) {
        List<OrderDto> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(toOrderDto(order));
        }
        return result;
    }

}
