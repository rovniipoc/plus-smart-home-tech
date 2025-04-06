package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

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

    public List<ProductDto> getProductsByParams(ProductCategory category, Pageable pageable) {
        List<Product> products = productRepository.findAllByProductCategory(category, pageable);
        return ProductMapper.toProductDto(products);
    }

    @Transactional
    public ProductDto updateProduct(UpdateProductRequest updateProductRequest) {
        UUID id = updateProductRequest.getProductId();
        Product existProduct = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продукта с id = " + id + " не существует"));

        if (updateProductRequest.getImageSrc() == null || updateProductRequest.getImageSrc().isBlank()) {
            updateProductRequest.setImageSrc(existProduct.getImageSrc());
        }

        if (updateProductRequest.getProductCategory() == null) {
            updateProductRequest.setProductCategory(existProduct.getProductCategory());
        }

        Product updatedProduct = ProductMapper.toProduct(updateProductRequest);

        return ProductMapper.toProductDto(productRepository.save(updatedProduct));
    }

    @Transactional
    public boolean removeProductFromStore(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продукта с id = " + id + " не существует"));
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }
}
