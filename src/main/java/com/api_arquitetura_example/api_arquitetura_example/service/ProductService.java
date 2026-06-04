package com.api_arquitetura_example.api_arquitetura_example.service;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import com.api_arquitetura_example.api_arquitetura_example.exception.BusinessException;
import com.api_arquitetura_example.api_arquitetura_example.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public Product create(Product obj) {
        // Validações de negócio
        validateProduct(obj);

        // Verifica se já existe produto com mesmo nome (exemplo de regra de negócio)
        if (repository.findAll().stream().anyMatch(p -> p.getName().equalsIgnoreCase(obj.getName()))) {
            throw new BusinessException("Product with name '" + obj.getName() + "' already exists");
        }

        // Valida preço positivo
        if (obj.getPrice() <= 0) {
            throw new BusinessException("Product price must be greater than zero");
        }

        return repository.save(obj);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Cannot delete. Product not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    public Product update(Long id, Product obj) {
        // Verifica se o produto existe
        Product entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Validações de negócio
        validateProduct(obj);

        // Valida preço positivo
        if (obj.getPrice() <= 0) {
            throw new BusinessException("Product price must be greater than zero");
        }

        // Verifica se o novo nome já está sendo usado por outro produto
        if (!entity.getName().equalsIgnoreCase(obj.getName()) &&
                repository.findAll().stream().anyMatch(p -> p.getName().equalsIgnoreCase(obj.getName()))) {
            throw new BusinessException("Product with name '" + obj.getName() + "' already exists");
        }

        entity.setName(obj.getName());
        entity.setPrice(obj.getPrice());

        return repository.save(entity);
    }

    // Método auxiliar para validações comuns
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new BusinessException("Product name cannot be null or empty");
        }

        if (product.getPrice() == null) {
            throw new BusinessException("Product price cannot be null");
        }
    }
}