package com.api_arquitetura_example.api_arquitetura_example.controller;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api_arquitetura_example.api_arquitetura_example.service.ProductService;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> listarTodos() {
        List<Product> products = productService.listarTodos();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> buscarPorId(@PathVariable Long id) {
        Product product = productService.buscarPorId(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> buscarPorPrecoMaximo(@RequestParam Double maxPrice) {
        List<Product> products = productService.buscarPorPrecoMaximo(maxPrice);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> criar(@RequestBody Product product) {
        Product novoProduct = productService.salvar(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> atualizar(@PathVariable Long id, @RequestBody Product product) {
        Product productAtualizado = productService.atualizar(id, product);
        return ResponseEntity.ok(productAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        productService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}