package com.seungwoo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Created by Leo.
 * User: sonseungwoo
 * Date: 2019-02-10
 * Time: 13:40
 */
@Slf4j
@Configuration
public class LogFilter implements WebFilter {

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        //web handleInterceptorAdapter preHandle
        log.info("Serving '{}'", path);
        //return handleInterceptoprAdapter postHandle
        return chain.filter(exchange).doAfterTerminate(() -> {
                    exchange.getResponse().getHeaders().entrySet().forEach(e ->
                            log.info("Response header '{}': {}", e.getKey(), e.getValue()));
                    log.info("Served '{}' as {} in {} msec",
                            path,
                            exchange.getResponse().getStatusCode(),
                            System.currentTimeMillis() - startTime);
                }
        );
    }
}
