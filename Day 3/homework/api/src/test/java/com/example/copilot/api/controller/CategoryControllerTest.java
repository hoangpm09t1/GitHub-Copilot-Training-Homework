package com.example.copilot.api.controller;

import com.example.copilot.core.dto.CategoryDTO;
import com.example.copilot.service.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CategoryControllerTest {

    @Autowired private WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean private CategoryService categoryService;

    private MockMvc mockMvc;
    private CategoryDTO sampleCategory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        sampleCategory = new CategoryDTO();
        sampleCategory.setId(1L);
        sampleCategory.setName("Electronics");
    }

    @Test
    @WithMockUser
    void createCategory_shouldReturn201() throws Exception {
        when(categoryService.addCategory(any())).thenReturn(sampleCategory);

        CategoryDTO request = new CategoryDTO();
        request.setName("Electronics");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @WithMockUser
    void getAllCategories_shouldReturn200() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of(sampleCategory));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    @WithMockUser
    void getCategoryById_shouldReturn200() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(sampleCategory);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
