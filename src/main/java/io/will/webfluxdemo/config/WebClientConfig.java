package io.will.webfluxdemo.config;

import io.will.webfluxdemo.client.SomeHttpsServiceClient;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Bean
    public SomeHttpsServiceClient someHttpsServiceClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://example.com")
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(
                        reactor.netty.http.client.HttpClient.create()
                                .secure(sslSpec -> sslSpec.sslContext(createTrustAllSslContext()))
                ))
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(adapter)
                .build();
        return factory.createClient(SomeHttpsServiceClient.class);
    }

    private SslContext createTrustAllSslContext() {
        try {
            return SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL context that trusts all certificates", e);
        }
    }
}