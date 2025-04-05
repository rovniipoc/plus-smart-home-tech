package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.NewProductRequest;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDto createNewProduct(NewProductRequest newProductRequest) {
        Product product = ProductMapper.toProduct(newProductRequest);
        return ProductMapper.toProductDto(productRepository.save(product));
    }
}
