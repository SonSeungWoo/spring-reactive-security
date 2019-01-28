package com.seungwoo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-25
 * Time: 17:48
 *
 * 비인증 공통처리 AuthenticationException
 */
@Configuration
public class UnauthorizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(final ServerWebExchange exchange, final AuthenticationException e) {
        String result = String.format("{\"code\":\"%s\",\"message\": \"%s\"}", "401", e.getMessage());
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");
        return response.writeWith(Mono.just(response.bufferFactory().allocateBuffer().write(result.getBytes())));
        //return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
    }
}
