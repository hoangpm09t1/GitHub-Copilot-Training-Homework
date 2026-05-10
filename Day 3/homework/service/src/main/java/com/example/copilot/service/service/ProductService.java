package com.example.copilot.service.service;

import com.example.copilot.core.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProduct(Long id);

    ProductDTO getProductById(Long id);

    List<ProductDTO> getAllProducts();

    Page<ProductDTO> searchProducts(String keyword, Long categoryId,
                                    BigDecimal minPrice, BigDecimal maxPrice,
                                    Pageable pageable);
}
