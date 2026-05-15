package service;

import entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ProductRepository;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> findAll() { return repository.findAll(); }

    public Product findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
    }

    @Transactional
    public Product create(Product obj) { return repository.save(obj); }

    @Transactional
    public void delete(Long id) { repository.deleteById(id); }

    @Transactional
    public Product update(Long id, Product obj) {
        Product entity = repository.getReferenceById(id);
        entity.setName(obj.getName());
        entity.setPrice(obj.getPrice());
        return repository.save(entity);
    }
}
