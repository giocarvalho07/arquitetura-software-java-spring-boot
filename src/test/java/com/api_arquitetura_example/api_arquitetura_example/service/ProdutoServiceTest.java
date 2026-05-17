package com.api_arquitetura_example.api_arquitetura_example.service;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @Test
    void deveSalvarProdutoComSucesso() {
        // Arrange (Preparação)
        Produto produto = new Produto(null, "Monitor 4K", new BigDecimal("1800.00"));
        when(repository.save(any(Produto.class)))
                .thenReturn(new Produto(1L, "Monitor 4K", new BigDecimal("1800.00")));

        // Act (Execução)
        Produto salvo = service.salvar(produto);

        // Assert (Verificação)
        assertNotNull(salvo.getId());
        assertEquals("Monitor 4K", salvo.getNome());
        verify(repository, times(1)).save(produto);
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        // Arrange
        Produto produto = new Produto(1L, "Teclado", new BigDecimal("150.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        // Act
        Produto encontrado = service.buscarPorId(1L);

        // Assert
        assertEquals("Teclado", encontrado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.buscarPorId(99L));
    }
}

