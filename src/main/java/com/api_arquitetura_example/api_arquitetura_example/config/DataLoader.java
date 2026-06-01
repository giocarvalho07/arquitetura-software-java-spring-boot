package com.api_arquitetura_example.api_arquitetura_example.config;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;

import com.api_arquitetura_example.api_arquitetura_example.entity.User;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import com.api_arquitetura_example.api_arquitetura_example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Iniciando carga de dados iniciais...");

        // Carregar usuários
        loadUsers();

        // Carregar produtos
        loadProducts();

        System.out.println("✅ Carga de dados concluída com sucesso!");
    }

    private void loadUsers() {
        // Verificar se já existem usuários
        if (userRepository.count() == 0) {
            System.out.println("📝 Criando usuários padrão...");

            List<User> users = Arrays.asList(
                    createUser("joao", "joao@email.com", "123456"),
                    createUser("maria", "maria@email.com", "123456"),
                    createUser("pedro", "pedro@email.com", "123456"),
                    createUser("ana", "ana@email.com", "123456")
            );

            userRepository.saveAll(users);
            System.out.println("✅ " + users.size() + " usuários criados!");
        } else {
            System.out.println("ℹ️ Usuários já existem no banco de dados. Pulando carga...");
        }
    }

    private void loadProducts() {
        // Verificar se já existem produtos
        if (productRepository.count() == 0) {
            System.out.println("📦 Criando produtos padrão...");

            List<Product> products = Arrays.asList(
                    createProduct("Notebook Dell XPS", 5999.99),
                    createProduct("MacBook Pro 14", 15999.99),
                    createProduct("iPhone 15 Pro", 7999.99),
                    createProduct("Samsung Galaxy S24", 5999.99),
                    createProduct("Mouse Logitech MX Master", 399.99),
                    createProduct("Teclado Mecânico Keychron", 899.99),
                    createProduct("Monitor 4K LG 27'", 2499.99),
                    createProduct("SSD NVMe 1TB", 499.99),
                    createProduct("Cadeira Gamer", 1299.99),
                    createProduct("Fone de Ouvido Sony", 1599.99)
            );

            productRepository.saveAll(products);
            System.out.println("✅ " + products.size() + " produtos criados!");
        } else {
            System.out.println("ℹ️ Produtos já existem no banco de dados. Pulando carga...");
        }
    }

    private User createUser(String username, String email, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        return user;
    }

    private Product createProduct(String name, Double price) {
        return new Product(null, name, price);
    }
}