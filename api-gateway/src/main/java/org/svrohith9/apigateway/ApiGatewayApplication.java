package org.svrohith9.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient-service", r -> r
                        .path("/api/v1/patients/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .addRequestHeader("X-Gateway", "Patient-API-Gateway")
                                .addResponseHeader("X-Content-Type-Options", "nosniff")
                                .addResponseHeader("X-Frame-Options", "DENY")
                                .addResponseHeader("X-XSS-Protection", "1; mode=block")
                                .circuitBreaker(config -> config
                                        .setName("patientCircuitBreaker")
                                        .setFallbackUri("forward:/fallback")))
                        .uri("http://patient-service:8080"))
                .build();
    }
}
