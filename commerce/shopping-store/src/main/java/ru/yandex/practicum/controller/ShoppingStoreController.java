package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.NewProductRequest;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.UpdateProductRequest;
import ru.yandex.practicum.service.ProductService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${ShoppingStore.api.prefix}")
public class ShoppingStoreController {

    private final ProductService productService;

    @Value("${ShoppingStore.api.prefix}")
    private String prefix;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createNewProduct(@Valid @RequestBody NewProductRequest newProductRequest) {
        log.info("Поступил запрос Put {} на создание Product с телом {}", prefix, newProductRequest);
        ProductDto response = productService.createNewProduct(newProductRequest);
        log.info("Сформирован ответ Put {} с телом: {}", prefix, response);
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProduct(@RequestParam(required = true) ProductCategory category,
                                 @RequestParam(required = true) Pageable pageable) {
        log.info("Поступил запрос Get {} на получение List<ProductDto> с параметрами category = {}, pageable = {}", prefix, category, pageable);
        List<ProductDto> response = productService.getProductsByParams(category, pageable);
        log.info("Сформирован ответ Get {} с телом: {}", prefix, response);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody UpdateProductRequest updateProductRequest) {
        log.info("Поступил запрос Post {} на обновление Product с телом {}", prefix, updateProductRequest);
        ProductDto response = productService.updateProduct(updateProductRequest);
        log.info("Сформирован ответ Post {} с телом: {}", prefix, response);
        return response;
    }
}
