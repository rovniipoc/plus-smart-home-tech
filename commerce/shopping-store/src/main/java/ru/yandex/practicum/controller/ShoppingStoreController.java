package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.NewProductRequest;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.service.ProductService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${ShoppingStore.api.prefix}")
public class ShoppingStoreController {

    private final ProductService productService;

    @Value("${ShoppingStore.api.prefix}")
    private String prefix;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createNewProduct(@Valid @RequestBody NewProductRequest newProductRequest) {
        log.info("Поступил запрос Post {} на создание Product с телом {}", prefix, newProductRequest);
        ProductDto response = productService.createNewProduct(newProductRequest);
        log.info("Сформирован ответ Post {} с телом: {}", prefix, response);
        return response;
    }
}
