package io.will.webfluxdemo.controller;

import io.will.webfluxdemo.service.SomeHttpsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SomeHttpsServiceController {

    private final SomeHttpsService someHttpsService;

    public SomeHttpsServiceController(SomeHttpsService someHttpsService) {
        this.someHttpsService = someHttpsService;
    }

    @GetMapping("/api/ping")
    public Mono<String> ping() {
        return someHttpsService.ping();
    }
}