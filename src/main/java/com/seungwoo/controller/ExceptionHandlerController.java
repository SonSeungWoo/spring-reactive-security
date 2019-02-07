package com.seungwoo.controller;

import com.seungwoo.dto.ParamDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.core.ResolvableType.forClass;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-01-23
 * Time: 09:13
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity> handleException(Exception e, ServerWebExchange exchange) {
        Mono<List<Mono<ParamDto>>> dw = exchange.getRequest()
                .getBody()
                .map(Flux::just)
                .map(body -> {
                    Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
                    ResolvableType elementType = forClass(ParamDto.class);
                    return decoder.decodeToMono(body, elementType, MediaType.APPLICATION_JSON, Collections.emptyMap()).cast(ParamDto.class);
                }).collect(Collectors.toList());
        return Mono.just(ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
