package com.api_arquitetura_example.api_arquitetura_example.repository;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
public class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    private Produto produtoTeste;
    private Produto produtoCaro;
    private Produto produtoBarato;

    @BeforeEach
    void setUp() {
        produtoTeste = repository.save(new Produto(null, "Produto Teste", BigDecimal.valueOf(100.0)));
        produtoCaro = repository.save(new Produto(null, "Produto Caro", BigDecimal.valueOf(500.0)));
        produtoBarato = repository.save(new Produto(null, "Produto Barato", BigDecimal.valueOf(50.0)));
    }

    @Test
    void testFindAll() {
        List<Produto> produtos = repository.findAll();
        assertThat(produtos).hasSize(3);
    }

    @Test
    void testFindByPriceLowerThan() {
        List<Produto> produtos = repository.findByPriceLowerThan(200.0);  // ← Fechou o parênteses
        assertThat(produtos).hasSize(2);
        assertThat(produtos.get(0).getPreco()).isLessThanOrEqualTo(BigDecimal.valueOf(200.0));
    }

    @Test
    void testSaveProduct() {
        Produto produto = new Produto(null, "Novo Produto", BigDecimal.valueOf(75.0));
        Produto saved = repository.save(produto);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNome()).isEqualTo("Novo Produto");
        assertThat(saved.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(75.0));  // ← Adicionado teste do preço
    }

    @Test
    void testFindById() {
        Produto produto = repository.findById(produtoTeste.getId()).orElse(null);
        assertThat(produto).isNotNull();
        assertThat(produto.getId()).isEqualTo(produtoTeste.getId());
        assertThat(produto.getNome()).isEqualTo("Produto Teste");
        assertThat(produto.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(100.0));
    }

    @Test
    void testDeleteById() {
        repository.deleteById(produtoTeste.getId());

        assertThat(repository.findById(produtoTeste.getId())).isEmpty();
        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    void testUpdateProduct() {
        Produto produto = repository.findById(produtoTeste.getId()).orElse(null);
        assertThat(produto).isNotNull();

        produto.setNome("Produto Atualizado");
        produto.setPreco(BigDecimal.valueOf(150.0));
        repository.save(produto);

        Produto updated = repository.findById(produtoTeste.getId()).orElse(null);
        assertThat(updated.getNome()).isEqualTo("Produto Atualizado");
        assertThat(updated.getPreco()).isEqualByComparingTo(BigDecimal.valueOf(150.0));
    }

}
