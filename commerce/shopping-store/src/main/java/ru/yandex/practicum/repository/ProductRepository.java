package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
