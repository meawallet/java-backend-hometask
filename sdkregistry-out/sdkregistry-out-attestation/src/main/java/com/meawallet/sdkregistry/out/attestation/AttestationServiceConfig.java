package com.meawallet.sdkregistry.out.attestation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AttestationServiceConfig {

    @Bean
    @Validated
    @ConfigurationProperties(prefix = "sdkregistry.attestation-service")
    public AttestationServiceProperties attestationServiceProperties() {
        return new AttestationServiceProperties();
    }

    @Bean
    @ConditionalOnMissingBean(WebClient.class)
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
