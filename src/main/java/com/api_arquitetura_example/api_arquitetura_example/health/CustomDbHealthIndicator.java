package com.api_arquitetura_example.api_arquitetura_example.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomDbHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Em um cenário real, você executaria um "SELECT 1" aqui
        boolean bancoOk = true;

        if (bancoOk) {
            return Health.up()
                    .withDetail("banco", "H2 In-Memory")
                    .withDetail("schema", "delivery_db")
                    .withDetail("status", "Conectado")
                    .build();
        }
        return Health.down().withDetail("erro", "Banco de dados indisponível").build();
    }
}
