package com.api_arquitetura_example.api_arquitetura_example.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gerenciamento de Produtos")
                        .version("1.0.0")
                        .description("API REST para CRUD de produtos - Documentação C4 com PlantUML")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu.email@exemplo.com")
                                .url("https://github.com/seu-usuario"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .addTagsItem(new Tag().name("Products").description("Operações CRUD de produtos"));
    }
}