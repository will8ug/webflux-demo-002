package io.will.webfluxdemo.client;

import org.springframework.web.service.annotation.GetExchange;

import reactor.core.publisher.Mono;

public interface SomeHttpsServiceClient {
    @GetExchange("/ping")
    Mono<String> ping();
}
