package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import com.example.demo.config.RouteValidator;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator; // checks if route is secured or not

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
        	String path = exchange.getRequest().getURI().getPath();
            // Skip authentication for open routes (e.g., /auth/register, /auth/login)
        	if (path.contains("/auth/register") || path.contains("/auth/login")) {
                return chain.filter(exchange);
            }
            if (!routeValidator.isSecured.test(exchange.getRequest())) {
                return chain.filter(exchange);
            }

            // Check for Authorization header
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            // Extract Bearer token
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7); // remove 'Bearer '
            System.out.println("token");
            // Call Auth-Service for token validation
            return webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8083/auth/validate")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            Mono.error(new RuntimeException("Unauthorized")))
                    .bodyToMono(AuthResponse.class)
                    .flatMap(authResponse -> {
                        if (!authResponse.isValid()) {
                            return onError(exchange, "Invalid JWT token", HttpStatus.FORBIDDEN);
                        }

                        // Inject user info into request headers for downstream services
                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-User-Name", authResponse.getUsername())
                                .header("X-User-Roles", authResponse.getRole())
                                .build();

                        ServerWebExchange newExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(newExchange);
                    })
                    .onErrorResume(e -> onError(exchange, "Token validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // can be extended for dynamic config in the future
    }

    // ✅ Inner static DTO for Auth-Service response
    static class AuthResponse {
        private boolean valid;
        private String username;
        private String role;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
