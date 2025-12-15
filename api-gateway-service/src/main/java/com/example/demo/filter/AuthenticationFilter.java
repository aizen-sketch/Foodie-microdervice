package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import com.example.demo.config.RouteValidator;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            // 🔓 Allow open endpoints (auth-service login/register)
            if (path.contains("/auth/login") || path.contains("/auth/register")) {
                return chain.filter(exchange);
            }

            // 🚫 Skip validation for unprotected routes
            if (!routeValidator.isSecured.test(exchange.getRequest())) {
                return chain.filter(exchange);
            }

            // 🔍 Check for Authorization header
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            // 🧩 Extract token from "Bearer <token>"
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            // 🧠 Debug log
            System.out.println("🔑 Validating token via Auth-Service...");

            // 🛰️ Call Auth-Service for validation
            return webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8083/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        System.out.println("❌ Auth-Service responded with: " + clientResponse.statusCode());
                        return Mono.error(new RuntimeException("Unauthorized"));
                    })
                    .bodyToMono(AuthResponse.class)
                    .flatMap(authResponse -> {
                        if (!authResponse.isValid()) {
                            return onError(exchange, "Invalid JWT token", HttpStatus.FORBIDDEN);
                        }

                        System.out.println("✅ Token valid for user: " + authResponse.getUsername() +
                                " (Role: " + authResponse.getRole() + ")");

                        // 🧾 Inject user info into request headers for downstream microservices
                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", String.valueOf(authResponse.getUserId()))
                                .header("X-User-Name", authResponse.getUsername())
                                .header("X-User-Role", authResponse.getRole())
                                .build();

                        ServerWebExchange newExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(newExchange);
                    })
                    .onErrorResume(e -> {
                        System.out.println("⚠️ Token validation failed: " + e.getMessage()+"token :-"+token);
                        return onError(exchange, "Token validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
                    });
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        System.out.println("🚫 Request blocked: " + err);
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // Future configuration options can go here
    }

    // ✅ DTO that matches Auth-Service /auth/validate response
    static class AuthResponse {
        private Long id;
        private boolean valid;
        private String username;
        private String role;

        public Long getUserId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
