package com.api_arquitetura_example.api_arquitetura_example.controller;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import com.api_arquitetura_example.api_arquitetura_example.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setPrice(99.90);
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void shouldGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(product);
        when(service.findAll()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Produto Teste"))
                .andExpect(jsonPath("$[0].price").value(99.90));
    }

    @Test
    @DisplayName("Deve buscar produto por ID")
    void shouldGetProductById() throws Exception {
        when(service.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Teste"))
                .andExpect(jsonPath("$.price").value(99.90));
    }

    @Test
    @DisplayName("Deve criar novo produto")
    void shouldCreateProduct() throws Exception {
        when(service.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Produto Teste"));
    }

    @Test
    @DisplayName("Deve atualizar produto")
    void shouldUpdateProduct() throws Exception {
        when(service.update(eq(1L), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Teste"));
    }

    @Test
    @DisplayName("Deve deletar produto")
    void shouldDeleteProduct() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
    }
}