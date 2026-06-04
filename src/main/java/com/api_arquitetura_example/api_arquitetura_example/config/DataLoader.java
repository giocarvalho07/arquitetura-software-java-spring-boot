package com.api_arquitetura_example.api_arquitetura_example.config;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Limpa o banco antes de carregar (opcional)
        productRepository.deleteAll();

        // Cria produtos de exemplo
        List<Product> products = Arrays.asList(
                createProduct("Notebook Dell", 3500.00),
                createProduct("Mouse Gamer", 150.00),
                createProduct("Teclado Mecânico", 250.00),
                createProduct("Monitor 24 polegadas", 1200.00),
                createProduct("SSD 1TB", 500.00),
                createProduct("Memória RAM 16GB", 400.00),
                createProduct("Webcam HD", 200.00),
                createProduct("Headset Gaming", 180.00)
        );

        // Salva todos no banco
        productRepository.saveAll(products);

        System.out.println("=== DADOS CARREGADOS COM SUCESSO ===");
        System.out.println("Total de produtos: " + productRepository.count());
        System.out.println("====================================");
    }

    private Product createProduct(String name, Double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}
