package com.api_arquitetura_example.api_arquitetura_example.controller;

import com.api_arquitetura_example.api_arquitetura_example.apiSucesso.ApiSucesso;
import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import jakarta.validation.Valid;
import com.api_arquitetura_example.api_arquitetura_example.mapper.ProdutoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.api_arquitetura_example.api_arquitetura_example.request.ProdutoRequestDTO;
import com.api_arquitetura_example.api_arquitetura_example.response.ProdutoResponseDTO;
import com.api_arquitetura_example.api_arquitetura_example.service.ProdutoService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Autowired
    private ProdutoMapper mapper;

    @PostMapping
    public ResponseEntity<ApiSucesso<ProdutoResponseDTO>> create(@RequestBody @Valid ProdutoRequestDTO req) {
        Produto salvo = service.salvar(mapper.toEntity(req));
        ProdutoResponseDTO res = mapper.toResponse(salvo);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(res.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new ApiSucesso<>("Produto criado com sucesso", res));
    }

    @GetMapping
    public ResponseEntity<ApiSucesso<List<ProdutoResponseDTO>>> getAll() {
        List<ProdutoResponseDTO> lista = service.listar().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiSucesso<>("Lista de produtos recuperada", lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSucesso<ProdutoResponseDTO>> getById(@PathVariable Long id) {
        Produto p = service.buscarPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        ProdutoResponseDTO res = mapper.toResponse(p);
        return ResponseEntity.ok(new ApiSucesso<>("Produto encontrado", res));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiSucesso<ProdutoResponseDTO>> update(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO req) {
        Produto p = service.buscarPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        p.setNome(req.getNome());
        p.setPreco(req.getPreco());

        ProdutoResponseDTO res = mapper.toResponse(service.salvar(p));
        return ResponseEntity.ok(new ApiSucesso<>("Produto atualizado com sucesso", res));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
