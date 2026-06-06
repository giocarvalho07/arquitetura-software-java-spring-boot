package com.api_arquitetura_example.api_arquitetura_example.service;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import java.util.List;


@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    /**
     * Retorna todos os produtos
     */
    public List<Product> listarTodos() {
        return repository.findAll();
    }

    /**
     * Busca produtos com preço menor ou igual ao valor informado
     */
    public List<Product> buscarPorPrecoMaximo(Double maxPrice) {
        return repository.findByPriceLowerThan(maxPrice);
    }

    /**
     * Busca por ID com Cache
     * Se o ID já estiver no cache 'products', o método nem é executado
     */
    @Cacheable(value = "products", key = "#id")
    public Product buscarPorId(Long id) {
        // Simulação de operação lenta (ex: consulta pesada ou rede)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }

    /**
     * Salva um novo produto
     */
    @Transactional
    public Product salvar(Product product) {
        return repository.save(product);
    }

    /**
     * Atualiza e limpa o cache para garantir que a próxima leitura
     * traga o dado novo
     */
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product atualizar(Long id, Product productAtualizado) {
        Product product = buscarPorId(id); // Pode ler do cache aqui se já existir

        product.setName(productAtualizado.getName());
        product.setPrice(productAtualizado.getPrice());

        return repository.save(product);
    }

    /**
     * Deleta o produto e remove sua entrada do cache
     */
    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Não é possível deletar: Produto não encontrado");
        }
        repository.deleteById(id);
    }
}