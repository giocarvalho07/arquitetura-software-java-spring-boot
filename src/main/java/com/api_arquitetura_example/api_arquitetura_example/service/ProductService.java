package com.api_arquitetura_example.api_arquitetura_example.service;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Observed(name = "product.findAll", contextualName = "findAllProducts")
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Observed(name = "product.findById", contextualName = "findProductById")
    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
    }

    @Transactional
    @Observed(name = "product.create", contextualName = "createProduct")
    public Product create(Product obj) {
        return repository.save(obj);
    }

    @Transactional
    @Observed(name = "product.delete", contextualName = "deleteProduct")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    @Observed(name = "product.update", contextualName = "updateProduct")
    public Product update(Long id, Product obj) {
        Product entity = repository.getReferenceById(id);
        entity.setName(obj.getName());
        entity.setPrice(obj.getPrice());
        return repository.save(entity);
    }
}
