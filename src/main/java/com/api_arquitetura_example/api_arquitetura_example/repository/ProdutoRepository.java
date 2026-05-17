package com.api_arquitetura_example.api_arquitetura_example.repository;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.preco <= :maxPrice")
    List<Produto> findByPriceLowerThan(Double maxPrice);
}
