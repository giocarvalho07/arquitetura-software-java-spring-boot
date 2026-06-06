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
        // Verifica se já existem produtos no banco
        if (productRepository.count() == 0) {
            System.out.println("=== Carregando dados iniciais ===");

            // Criando produtos de exemplo
            List<Product> products = Arrays.asList(
                    createProduct("Notebook Dell XPS", 5999.99),
                    createProduct("Mouse Logitech MX Master", 349.90),
                    createProduct("Teclado Mecânico Keychron", 899.99),
                    createProduct("Monitor LG 27' 4K", 2499.00),
                    createProduct("SSD Samsung 1TB", 599.99),
                    createProduct("Cadeira Gamer DX Racer", 1899.00),
                    createProduct("Webcam Logitech C920", 499.90),
                    createProduct("Headset HyperX Cloud", 399.99),
                    createProduct("iPhone 15 Pro", 7999.00),
                    createProduct("iPad Air", 4999.00)
            );

            productRepository.saveAll(products);

            System.out.println("✅ " + products.size() + " produtos carregados com sucesso!");
            System.out.println("=== Dados iniciais carregados ===");
        } else {
            System.out.println("ℹ️ Banco de dados já contém " + productRepository.count() + " produtos.");
        }

        // Listar todos os produtos para debug
        System.out.println("\n📦 Produtos disponíveis no banco:");
        productRepository.findAll().forEach(product ->
                System.out.println("  - ID: " + product.getId() +
                        ", Nome: " + product.getName() +
                        ", Preço: R$ " + product.getPrice())
        );
    }

    private Product createProduct(String name, Double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}