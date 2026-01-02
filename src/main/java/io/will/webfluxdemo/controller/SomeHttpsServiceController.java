package io.will.webfluxdemo.controller;

import io.will.webfluxdemo.client.SomeHttpsServiceClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SomeHttpsServiceController {

    private final SomeHttpsServiceClient someHttpsServiceClient;

    public SomeHttpsServiceController(SomeHttpsServiceClient someHttpsServiceClient) {
        this.someHttpsServiceClient = someHttpsServiceClient;
    }

    @GetMapping("/api/ping")
    public Mono<String> ping() {
        return someHttpsServiceClient.ping();
    }
}