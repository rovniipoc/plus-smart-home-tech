package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.UnauthorizedUserException;
import ru.yandex.practicum.feign.DeliveryClient;
import ru.yandex.practicum.feign.PaymentClient;
import ru.yandex.practicum.feign.WarehouseClient;
import ru.yandex.practicum.mapper.OrderMapper;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    @Transactional
    public OrderDto createOrder(CreateNewOrderRequest request) {
        BookedProductsDto bookedProducts = warehouseClient.checkProductQuantityEnoughForShoppingCart(request.getShoppingCart());
        Order order = OrderMapper.toOrder(request, bookedProducts);
        order = orderRepository.save(order);

        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress();
        DeliveryDto newDelivery = DeliveryDto.builder()
                .fromAddress(warehouseAddress)
                .toAddress(request.getDeliveryAddress())
                .orderId(order.getOrderId())
                .deliveryState(DeliveryState.CREATED)
                .build();
        newDelivery = deliveryClient.planDelivery(newDelivery);
        order.setDeliveryId(newDelivery.getDeliveryId());

        order = orderRepository.save(order);
        log.info("Новый заказ создан: {}", order);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<OrderDto> getUsersOrders(String username) {
        validateUsername(username);
        List<Order> orders = orderRepository.findByUsername(username);
        log.info("Сформированы заказы пользователя {}", username);
        return OrderMapper.toListOrderDto(orders);
    }

    @Override
    @Transactional
    public OrderDto returnOrderProducts(ProductReturnRequest request) {
        Order order = getOrder(request.getOrderId());
        warehouseClient.returnProducts(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        orderRepository.save(order);
        log.info("Товары заказа с id = {} возвращены", request.getOrderId());
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto setOrderDeliverySuccessful(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);
        log.info("Заказ с id = {} успешно доставлен", orderId);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto setOrderDeliveryFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);
        log.info("Доставка заказ с id = {} не удалась", orderId);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto setOrderDeliveryInProgress(UUID orderId) {
        Order order = getOrder(orderId);

        AssemblyProductsForOrderRequest request = AssemblyProductsForOrderRequest.builder()
                .orderId(order.getOrderId())
                .products(order.getProducts())
                .build();
        warehouseClient.assemblyProductsForOrder(request);

        order.setState(OrderState.ASSEMBLED);
        order = orderRepository.save(order);
        log.info("Заказ c id = {} собран", orderId);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto setOrderDeliveryAssemblyFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        order = orderRepository.save(order);
        log.info("Сборка заказа с id = {} не удалась", orderId);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto createOrderPayment(UUID orderId) {
        Order order = getOrder(orderId);
        PaymentDto payment = paymentClient.createPayment(OrderMapper.toOrderDto(order));
        order.setPaymentId(payment.getPaymentId());
        order.setState(OrderState.ON_PAYMENT);
        order = orderRepository.save(order);
        log.info("Платеж для заказа с id = {} создан", orderId);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto setOrderPaid(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAID);
        order = orderRepository.save(order);
        log.info("Заказ с id = {} успешно оплачен", orderId);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto setOrderPaymentFailed(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        order = orderRepository.save(order);
        log.info("Оплата заказа с id = {} не удалась", orderId);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto calculateProductCost(UUID orderId) {
        Order order = getOrder(orderId);
        double productPrice = paymentClient.calculateProductCost(OrderMapper.toOrderDto(order));
        order.setProductPrice(productPrice);
        order = orderRepository.save(order);
        log.info("Стоимость товаров заказа с id = {} равна: {}", orderId, productPrice);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = getOrder(orderId);
        double deliveryPrice = deliveryClient.calculateDeliveryCost(OrderMapper.toOrderDto(order));
        order.setDeliveryPrice(deliveryPrice);
        order = orderRepository.save(order);
        log.info("Стоимость доставки заказа с id = {} равна: {}", orderId, deliveryPrice);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = getOrder(orderId);
        double totalPrice = paymentClient.calculateTotalCost(OrderMapper.toOrderDto(order));
        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);
        log.info("Итоговая стоимость заказа с id = {} равна: {}", orderId, totalPrice);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public OrderDto completeOrder(UUID orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.COMPLETED);
        order = orderRepository.save(order);
        log.info("Заказ с id = {} выполнен", orderId);
        return OrderMapper.toOrderDto(order);
    }

    private Order getOrder(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Заказ " + id + " не найден"));
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new UnauthorizedUserException("Пользователь " + username + " не авторизован");
        }
    }
}
