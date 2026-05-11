package com.example.copilot.api.repository;

import com.example.copilot.core.entity.Category;
import com.example.copilot.core.entity.Product;
import com.example.copilot.service.repository.CategoryRepository;
import com.example.copilot.service.repository.OrderRepository;
import com.example.copilot.service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private OrderRepository orderRepository;

    private Category electronics;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        electronics = new Category();
        electronics.setName("Electronics");
        electronics = categoryRepository.save(electronics);

        Product p1 = new Product();
        p1.setName("iPhone 15");
        p1.setDescription("Apple smartphone");
        p1.setPrice(BigDecimal.valueOf(999.99));
        p1.setStockQuantity(50);
        p1.setCategory(electronics);
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Samsung Galaxy");
        p2.setDescription("Android smartphone");
        p2.setPrice(BigDecimal.valueOf(799.99));
        p2.setStockQuantity(30);
        p2.setCategory(electronics);
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("Laptop Pro");
        p3.setDescription("High-performance laptop");
        p3.setPrice(BigDecimal.valueOf(1499.99));
        p3.setStockQuantity(10);
        productRepository.save(p3);
    }

    @Test
    void searchProducts_byKeyword_shouldReturnMatchingProducts() {
        Page<Product> result = productRepository.searchProducts(
                "iphone", null, null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("iPhone 15");
    }

    @Test
    void searchProducts_byCategoryId_shouldReturnCategoryProducts() {
        Page<Product> result = productRepository.searchProducts(
                null, electronics.getId(), null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void searchProducts_byPriceRange_shouldReturnProductsInRange() {
        Page<Product> result = productRepository.searchProducts(
                null, null, BigDecimal.valueOf(700), BigDecimal.valueOf(1000), PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
        result.getContent().forEach(p ->
                assertThat(p.getPrice()).isBetween(BigDecimal.valueOf(700), BigDecimal.valueOf(1000)));
    }

    @Test
    void searchProducts_noFilters_shouldReturnAllProducts() {
        Page<Product> result = productRepository.searchProducts(
                null, null, null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void searchProducts_byKeywordInDescription_shouldMatch() {
        Page<Product> result = productRepository.searchProducts(
                "high-performance", null, null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Laptop Pro");
    }
}