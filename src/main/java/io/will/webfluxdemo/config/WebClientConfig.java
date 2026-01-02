package io.will.webfluxdemo.config;

import io.will.webfluxdemo.client.SomeHttpsServiceClient;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;

@Configuration
public class WebClientConfig {

    @Bean
    public SomeHttpsServiceClient someHttpsServiceClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://example.com")
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .secure(sslSpec -> sslSpec.sslContext(createTrustSpecificSslContext()))
                ))
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder()
                .exchangeAdapter(adapter)
                .build();
        return factory.createClient(SomeHttpsServiceClient.class);
    }

    private SslContext createTrustSpecificSslContext() {
        try {
            ClassPathResource resource = new ClassPathResource("trusted-certs.pem");
            
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            try (InputStream certInputStream = resource.getInputStream()) {
                Collection<? extends Certificate> certificates = certFactory.generateCertificates(certInputStream);
                
                int certIndex = 0;
                for (Certificate cert : certificates) {
                    keyStore.setCertificateEntry("cert-" + certIndex++, cert);
                }
            }
            
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            
            return SslContextBuilder.forClient()
                    .trustManager(trustManagerFactory)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL context with specific certificate", e);
        }
    }
}