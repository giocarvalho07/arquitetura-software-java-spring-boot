package com.api_arquitetura_example.api_arquitetura_example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
// para o cache 'simple', o @EnableCaching já é suficiente.
@EnableCaching
public class ApiArquiteturaExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiArquiteturaExampleApplication.class, args);
	}

}
