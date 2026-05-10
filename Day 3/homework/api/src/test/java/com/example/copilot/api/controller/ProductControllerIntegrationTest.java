package com.example.copilot.api.controller;

import com.example.copilot.core.dto.ProductDTO;
import com.example.copilot.service.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProductControllerIntegrationTest {

    @Autowired private WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean private ProductService productService;

    private MockMvc mockMvc;
    private ProductDTO sampleProduct;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        sampleProduct = ProductDTO.builder()
                .id(1L)
                .name("iPhone 15")
                .description("Latest Apple smartphone")
                .price(BigDecimal.valueOf(999.99))
                .stockQuantity(50)
                .build();
    }

    @Test
    void getProductById_shouldReturn200() throws Exception {
        when(productService.getProductById(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    void searchProducts_shouldReturn200_withPagination() throws Exception {
        when(productService.searchProducts(any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(sampleProduct), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("iPhone 15"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void searchProducts_shouldReturn200_withKeyword() throws Exception {
        when(productService.searchProducts(eq("iphone"), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(sampleProduct), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/products?keyword=iphone&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("iPhone 15"));
    }

    @Test
    void createProduct_shouldReturn401_whenNoAuth() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isUnauthorized());
    }
}
