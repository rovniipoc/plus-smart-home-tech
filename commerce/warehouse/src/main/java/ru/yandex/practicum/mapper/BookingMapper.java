package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.model.Booking;

public class BookingMapper {

    public static Booking toBooking(BookedProductsDto productsParams, AssemblyProductsForOrderRequest request) {
        Booking booking = new Booking();
        booking.setFragile(productsParams.isFragile());
        booking.setProducts(request.getProducts());
        booking.setDeliveryWeight(productsParams.getDeliveryWeight());
        booking.setDeliveryVolume(productsParams.getDeliveryVolume());
        booking.setDeliveryId(productsParams.getDeliveryId());
        booking.setOrderId(request.getOrderId());

        return booking;
    }

    public static BookedProductsDto toBookedProductsDto(Booking booking) {
        BookedProductsDto bookedProductsDto = new BookedProductsDto();
        bookedProductsDto.setFragile(booking.isFragile());
        bookedProductsDto.setProducts(booking.getProducts());
        bookedProductsDto.setBookingId(booking.getBookingId());
        bookedProductsDto.setDeliveryWeight(booking.getDeliveryWeight());
        bookedProductsDto.setDeliveryVolume(booking.getDeliveryVolume());
        bookedProductsDto.setDeliveryId(booking.getDeliveryId());
        bookedProductsDto.setOrderId(booking.getOrderId());

        return bookedProductsDto;
    }

}
