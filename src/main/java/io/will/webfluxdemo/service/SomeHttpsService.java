package io.will.webfluxdemo.service;

import io.will.webfluxdemo.client.SomeHttpsServiceClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SomeHttpsService {

    private final SomeHttpsServiceClient someHttpsServiceClient;

    public SomeHttpsService(SomeHttpsServiceClient someHttpsServiceClient) {
        this.someHttpsServiceClient = someHttpsServiceClient;
    }

    public Mono<String> ping() {
        return someHttpsServiceClient.ping();
    }
}