package com.api_arquitetura_example.api_arquitetura_example;

import com.api_arquitetura_example.api_arquitetura_example.entity.Produto;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProdutoRepository repository;

    @Override
    public void run(String... args) {
        // Limpa o banco e insere dados iniciais
        repository.deleteAll();

        repository.save(new Produto(null, "Smartphone G5", new BigDecimal("2500.00")));
        repository.save(new Produto(null, "Notebook Pro", new BigDecimal("5500.00")));
        repository.save(new Produto(null, "Teclado Mecânico", new BigDecimal("350.00")));
        repository.save(new Produto(null, "Monitor UltraWide", new BigDecimal("2100.00")));

        System.out.println(">>> 4 Produtos iniciais carregados com sucesso!");
    }
}
