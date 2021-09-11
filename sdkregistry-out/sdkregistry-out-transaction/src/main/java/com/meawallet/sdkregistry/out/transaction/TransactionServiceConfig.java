package com.meawallet.sdkregistry.out.transaction;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TransactionServiceConfig {

    @Bean
    @Validated
    @ConfigurationProperties(prefix = "sdkregistry.transaction-service")
    public TransactionServiceProperties transactionServiceProperties() {
        return new TransactionServiceProperties();
    }

    @Bean
    @ConditionalOnMissingBean(WebClient.class)
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
