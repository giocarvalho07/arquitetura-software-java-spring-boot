package repository;

import entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface que fornece os métodos CRUD básicos (save, findAll, findById, delete)
 * e permite a criação de consultas customizadas caso necessário.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}

