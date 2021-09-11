package com.meawallet.sdkregistry.out.cloudmessaging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CloudMessagingServiceConfig {

    @Bean
    @Validated
    @ConfigurationProperties(prefix = "sdkregistry.cloud-messaging-service")
    public CloudMessagingServiceProperties cloudMessagingServiceProperties() {
        return new CloudMessagingServiceProperties();
    }

    @Bean
    @ConditionalOnMissingBean(WebClient.class)
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
