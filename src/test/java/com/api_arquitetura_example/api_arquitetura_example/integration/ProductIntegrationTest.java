package com.api_arquitetura_example.api_arquitetura_example.integration;

import com.api_arquitetura_example.api_arquitetura_example.entity.Product;
import com.api_arquitetura_example.api_arquitetura_example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar e recuperar produto via API")
    void shouldCreateAndRetrieveProduct() {
        Product product = new Product();
        product.setName("Produto Integração");
        product.setPrice(199.99);

        ResponseEntity<Product> postResponse = restTemplate.postForEntity("/products", product, Product.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getBody()).isNotNull();
        assertThat(postResponse.getBody().getId()).isNotNull();

        Long productId = postResponse.getBody().getId();

        ResponseEntity<Product> getResponse = restTemplate.getForEntity("/products/" + productId, Product.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getName()).isEqualTo("Produto Integração");
    }
}