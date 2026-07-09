package com.edu.silva.gateway;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class JwtAuthFilter implements GlobalFilter {


    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    Map<String, String> routeRoles = Map.of(
            "/crm", "ROLE_CRM",
            "/finances","ROLE_FINANCES"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/ping") || path.startsWith("/auth") || path.startsWith("/health") || path.startsWith("/")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        DecodedJWT jwt = jwtUtil.validateToken(token);
        List<String> roles = jwt.getClaim("roles").asList(String.class);

        for (Map.Entry<String, String> entry : routeRoles.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                if (roles == null || !roles.contains(entry.getValue())) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
        }

        String subject = jwt.getSubject();

        exchange = exchange.mutate()
                .request(builder ->
                        builder.header("X-User-ID", subject))
                .build();

        return chain.filter(exchange);
    }
}
