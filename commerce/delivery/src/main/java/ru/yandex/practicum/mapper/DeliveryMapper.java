package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;

public class DeliveryMapper {

    public static DeliveryDto toDeliveryDto(Delivery delivery) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryId(delivery.getDeliveryId());
        deliveryDto.setDeliveryState(delivery.getDeliveryState());
        deliveryDto.setOrderId(delivery.getOrderId());
        deliveryDto.setToAddress(toAddressDto(delivery.getToAddress()));
        deliveryDto.setFromAddress(toAddressDto(delivery.getFromAddress()));

        return deliveryDto;
    }

    public static Delivery toDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = new Delivery();
        delivery.setDeliveryId(deliveryDto.getDeliveryId());
        delivery.setDeliveryState(deliveryDto.getDeliveryState());
        delivery.setOrderId(deliveryDto.getOrderId());
        delivery.setToAddress(toAddress(deliveryDto.getToAddress()));
        delivery.setFromAddress(toAddress(deliveryDto.getFromAddress()));

        return delivery;
    }

    public static AddressDto toAddressDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry(address.getCountry());
        addressDto.setCity(address.getCity());
        addressDto.setStreet(address.getStreet());
        addressDto.setHouse(address.getHouse());
        addressDto.setFlat(address.getFlat());

        return addressDto;
    }

    public static Address toAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setHouse(addressDto.getHouse());
        address.setFlat(addressDto.getFlat());

        return address;
    }
}
