package com.api_arquitetura_example.api_arquitetura_example.test;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import com.api_arquitetura_example.api_arquitetura_example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class ProductCacheTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CacheManager cacheManager;

    private Long productId;

    @BeforeEach
    void setup() {
        // Limpa o cache e o banco antes de cada teste
        Objects.requireNonNull(cacheManager.getCache("products")).clear();
        repository.deleteAll();

        Product p = repository.save(new Product(null, "Cadeira Gamer", 1200.0));
        this.productId = p.getId();
    }

    @Test
    @DisplayName("Deve validar que a segunda chamada é significativamente mais rápida que a primeira")
    void deveValidarPerformanceDoCache() {
        // 1ª Chamada: Deve demorar ~2000ms devido ao Thread.sleep no Service
        long inicioPrimeira = System.currentTimeMillis();
        productService.buscarPorId(productId);
        long fimPrimeira = System.currentTimeMillis();
        long tempoPrimeira = fimPrimeira - inicioPrimeira;

        // 2ª Chamada: Deve ser quase instantânea (< 100ms)
        long inicioSegunda = System.currentTimeMillis();
        productService.buscarPorId(productId);
        long fimSegunda = System.currentTimeMillis();
        long tempoSegunda = fimSegunda - inicioSegunda;

        System.out.println("\n>>> PERFORMANCE TEST <<<");
        System.out.println("Tempo 1ª chamada (sem cache): " + tempoPrimeira + "ms");
        System.out.println("Tempo 2ª chamada (com cache): " + tempoSegunda + "ms");

        // Asserção: A segunda chamada deve ser pelo menos 10x mais rápida
        assertTrue(tempoSegunda < (tempoPrimeira / 10),
                "O cache não parece estar funcionando. A segunda chamada demorou demais!");
    }

    @Test
    @DisplayName("Deve validar que o cache é limpo após atualização")
    void deveValidarCacheEvictAoAtualizar() {
        // Primeira chamada - vai para o cache
        long inicioPrimeira = System.currentTimeMillis();
        Product product1 = productService.buscarPorId(productId);
        long fimPrimeira = System.currentTimeMillis();
        long tempoPrimeira = fimPrimeira - inicioPrimeira;

        System.out.println("\n>>> CACHE EVICT TEST (UPDATE) <<<");
        System.out.println("Tempo 1ª chamada (sem cache): " + tempoPrimeira + "ms");

        // Atualiza o produto - deve limpar o cache
        Product productAtualizado = new Product(null, "Cadeira Gamer Deluxe", 1500.0);
        productService.atualizar(productId, productAtualizado);

        // Segunda chamada após update - deve vir do banco novamente
        long inicioSegunda = System.currentTimeMillis();
        Product product2 = productService.buscarPorId(productId);
        long fimSegunda = System.currentTimeMillis();
        long tempoSegunda = fimSegunda - inicioSegunda;

        System.out.println("Tempo após update (cache limpo): " + tempoSegunda + "ms");

        // Verifica se os dados foram atualizados
        assertTrue(product2.getPrice() == 1500.0, "Produto não foi atualizado corretamente");
        assertTrue(tempoSegunda >= 1000, "O cache não foi limpo após atualização");
    }

    @Test
    @DisplayName("Deve validar que o cache é limpo após deleção")
    void deveValidarCacheEvictAoDeletar() {
        // Primeira chamada - vai para o cache
        productService.buscarPorId(productId);

        // Verifica se está no cache
        Object cachedProduct = Objects.requireNonNull(cacheManager.getCache("products")).get(productId);
        assertTrue(cachedProduct != null, "Produto deveria estar no cache");

        // Deleta o produto - deve limpar o cache
        productService.deletar(productId);

        // Verifica se foi removido do cache
        cachedProduct = Objects.requireNonNull(cacheManager.getCache("products")).get(productId);
        assertTrue(cachedProduct == null, "Produto não deveria estar no cache após deleção");

        System.out.println("\n>>> CACHE EVICT TEST (DELETE) <<<");
        System.out.println("Cache limpo com sucesso após deleção!");
    }
}
