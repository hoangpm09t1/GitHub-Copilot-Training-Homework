package com.example.copilot.api.service;

import com.example.copilot.core.dto.ProductDTO;
import com.example.copilot.core.entity.Category;
import com.example.copilot.core.entity.Product;
import com.example.copilot.service.implementation.ProductServiceImpl;
import com.example.copilot.service.repository.CategoryRepository;
import com.example.copilot.service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("iPhone 15");
        product.setDescription("Latest Apple smartphone");
        product.setPrice(BigDecimal.valueOf(999.99));
        product.setStockQuantity(50);

        productDTO = ProductDTO.builder()
                .name("iPhone 15")
                .description("Latest Apple smartphone")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(50)
                .build();
    }

    @Test
    void addProduct_shouldReturnProductDTO() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.addProduct(productDTO);

        assertNotNull(result);
        assertEquals("iPhone 15", result.getName());
        assertEquals(BigDecimal.valueOf(999.99), result.getPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_shouldReturnProductDTO_whenFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("iPhone 15", result.getName());
    }

    @Test
    void getProductById_shouldThrowException_whenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(99L));
    }

    @Test
    void getAllProducts_shouldReturnList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDTO> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("iPhone 15", result.get(0).getName());
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO updated = ProductDTO.builder()
                .name("iPhone 15 Pro")
                .price(BigDecimal.valueOf(1199.99))
                .stockQuantity(30)
                .build();

        ProductDTO result = productService.updateProduct(1L, updated);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_shouldCallDelete_whenExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProduct_shouldThrowException_whenNotFound() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(99L));
    }

    @Test
    void searchProducts_shouldReturnPage() {
        Page<Product> page = new PageImpl<>(List.of(product));
        when(productRepository.searchProducts(any(), any(), any(), any(), any())).thenReturn(page);

        Page<ProductDTO> result = productService.searchProducts("iphone", null, null, null, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void addProduct_withCategory_shouldSetCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        productDTO.setCategoryId(1L);
        product.setCategory(category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.addProduct(productDTO);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals("Electronics", result.getCategoryName());
    }
}
