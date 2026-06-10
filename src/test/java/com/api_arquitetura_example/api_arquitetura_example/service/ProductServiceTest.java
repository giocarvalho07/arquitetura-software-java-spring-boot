package com.api_arquitetura_example.api_arquitetura_example.service;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Monitor 4K");
        product.setPrice(2500.00);
    }

    @Test
    @DisplayName("Deve criar produto com sucesso")
    void shouldCreateProduct() {
        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = service.create(product);

        assertNotNull(result);
        assertEquals("Monitor 4K", result.getName());
        assertEquals(2500.00, result.getPrice());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void shouldFindAll() {
        when(repository.findAll()).thenReturn(List.of(product));

        List<Product> result = service.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve encontrar produto por ID com sucesso")
    void shouldFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Monitor 4K", result.getName());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado")
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.findById(99L);
        });

        assertEquals("Não encontrado", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void shouldUpdateProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Monitor 4K Ultra");
        updatedProduct.setPrice(2999.99);

        when(repository.getReferenceById(1L)).thenReturn(product);
        when(repository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = service.update(1L, updatedProduct);

        assertNotNull(result);
        assertEquals("Monitor 4K Ultra", result.getName());
        assertEquals(2999.99, result.getPrice());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve deletar produto com sucesso")
    void shouldDeleteProduct() {
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.delete(1L));

        verify(repository, times(1)).deleteById(1L);
    }
}