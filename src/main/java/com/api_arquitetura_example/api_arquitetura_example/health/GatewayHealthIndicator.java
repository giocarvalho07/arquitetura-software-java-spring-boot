package com.api_arquitetura_example.api_arquitetura_example.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class GatewayHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Simula uma verificação com 10% de chance de falha
        boolean servicoOnline = new Random().nextDouble() > 0.1;

        if (servicoOnline) {
            return Health.up()
                    .withDetail("url", "https://api.pagamentos.com")
                    .withDetail("latencia", "45ms")
                    .build();
        }

        return Health.down()
                .withDetail("erro", "Timeout ao conectar com gateway")
                .withDetail("codigo", 504)
                .build();
    }
}