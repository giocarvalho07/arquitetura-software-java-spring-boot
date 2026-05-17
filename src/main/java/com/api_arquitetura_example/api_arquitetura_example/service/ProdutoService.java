package com.api_arquitetura_example.api_arquitetura_example.service;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    // READ (Listar Todos)
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    // READ (Buscar por ID)
    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    // CREATE (Salvar)
    @Transactional
    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    // UPDATE (Atualizar)
    @Transactional
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto existente = buscarPorId(id);
        existente.setNome(produtoAtualizado.getNome());
        existente.setPreco(produtoAtualizado.getPreco());
        return repository.save(existente);
    }

    // DELETE (Deletar)
    @Transactional
    public void deletar(Long id) {
        Produto existente = buscarPorId(id);
        repository.delete(existente);
    }
}
