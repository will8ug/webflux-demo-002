package io.will.webfluxdemo;

import io.will.webfluxdemo.client.SomeHttpsServiceClient;
import io.will.webfluxdemo.controller.SomeHttpsServiceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = SomeHttpsServiceController.class)
public class SomeHttpsServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SomeHttpsServiceClient someHttpsServiceClient;

    @Test
    void testPing() {
        // Mock the service to return a friendly non-empty string
        when(someHttpsServiceClient.ping()).thenReturn(Mono.just("pong"));

        // Test the endpoint
        webTestClient.get()
                .uri("/api/ping")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("pong");
    }
}