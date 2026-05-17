package com.api_arquitetura_example.api_arquitetura_example.repository;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface que fornece os métodos CRUD básicos (save, findAll, findById, delete)
 * e permite a criação de consultas customizadas caso necessário.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

