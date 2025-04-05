package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.NewProductRequest;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static ProductDto toProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setDescription(product.getDescription());
        productDto.setImageSrc(product.getImageSrc());
        productDto.setQuantityState(product.getQuantityState());
        productDto.setProductCategory(product.getProductCategory());
        productDto.setProductState(product.getProductState());
        productDto.setPrice(product.getPrice());
        return productDto;
    }

    public static List<ProductDto> toProductDto(Iterable<Product> products) {
        List<ProductDto> result = new ArrayList<>();
        for (Product product : products) {
            result.add(toProductDto(product));
        }
        return result;
    }

    public static Product toProduct(NewProductRequest newProductRequest) {
        Product product = new Product();
        product.setProductId(newProductRequest.getProductId());
        product.setProductName(newProductRequest.getProductName());
        product.setDescription(newProductRequest.getDescription());
        product.setImageSrc(newProductRequest.getImageSrc());
        product.setQuantityState(newProductRequest.getQuantityState());
        product.setProductCategory(newProductRequest.getProductCategory());
        product.setProductState(newProductRequest.getProductState());
        product.setPrice(newProductRequest.getPrice());
        return product;
    }

    public static List<Product> toProduct(Iterable<NewProductRequest> products) {
        List<Product> result = new ArrayList<>();
        for (NewProductRequest product : products) {
            result.add(toProduct(product));
        }
        return result;
    }
}
