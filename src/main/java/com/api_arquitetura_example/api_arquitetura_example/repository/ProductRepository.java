package com.api_arquitetura_example.api_arquitetura_example.repository;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
