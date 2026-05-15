package service;


import entity.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    // Método com transação normal (leitura e escrita)
    @Transactional
    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    // Método APENAS para leitura (mais performático)
    @Transactional(readOnly = true)  // ✅ CORRETO
    public List<Produto> listar() {
        return repository.findAll();
    }

    // Buscar por ID com readOnly
    @Transactional(readOnly = true)  // ✅ CORRETO
    public Produto buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Método de deleção
    @Transactional
    public void deletar(Long id) {
        repository.deleteById(id);
    }
}