package com.api_arquitetura_example.api_arquitetura_example.repository;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Consulta customizada com JPQL
    @Query("SELECT p FROM Product p WHERE p.price <= :maxPrice")
    List<Product> findByPriceLowerThan(Double maxPrice);
}
