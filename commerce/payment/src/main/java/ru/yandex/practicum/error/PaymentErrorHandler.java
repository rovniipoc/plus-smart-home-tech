package ru.yandex.practicum.error;

import feign.RetryableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.ApiError;
import ru.yandex.practicum.exception.NotEnoughInfoException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.UnauthorizedUserException;

@RestControllerAdvice
public class PaymentErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, e, "Сущность не найдена");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleUnauthorizedUserException(UnauthorizedUserException e) {
        return new ApiError(HttpStatus.UNAUTHORIZED, e, "Пользователь не авторизован");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotEnoughInfoException(NotEnoughInfoException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, e, "Недостаточно информации в заказе для подсчета");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiError handleRetryableException(RetryableException e) {
        return new ApiError(HttpStatus.SERVICE_UNAVAILABLE, e, "Сервис не доступен");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e, "Внутренняя ошибка сервера");
    }
}
