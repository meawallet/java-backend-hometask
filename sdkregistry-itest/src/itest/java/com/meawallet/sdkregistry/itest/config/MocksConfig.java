package com.meawallet.sdkregistry.itest.config;

import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.core.cryptography.AesKeyGenerator;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import com.meawallet.sdkregistry.cryptography.AesEncryptionServiceImpl;
import com.meawallet.sdkregistry.itest.mocks.AttestationServiceMock;
import com.meawallet.sdkregistry.itest.mocks.CloudMessagingServiceMock;
import com.meawallet.sdkregistry.itest.mocks.TransactionServiceMock;
import com.meawallet.sdkregistry.out.attestation.AttestationServiceProperties;
import com.meawallet.sdkregistry.out.cloudmessaging.CloudMessagingServiceProperties;
import com.meawallet.sdkregistry.out.transaction.TransactionServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@Configuration
public class MocksConfig {

    @Bean
    @Primary
    public AesKeyGenerator aesKeyGenerator() {
        return mock(AesKeyGenerator.class);
    }

    @Bean
    @Primary
    public RandomGenerator randomGenerator() {
        return mock(RandomGenerator.class);
    }

    @Bean
    @Primary
    public AesEncryptionService aesEncryptionService() {
        return spy(new AesEncryptionServiceImpl());
    }

    @Bean
    public AttestationServiceMock attestationServiceMock(AttestationServiceProperties properties) {
        return new AttestationServiceMock(properties);
    }

    @Bean
    public CloudMessagingServiceMock cloudMessagingServiceMock(CloudMessagingServiceProperties properties) {
        return new CloudMessagingServiceMock(properties);
    }

    @Bean
    public TransactionServiceMock transactionServiceMock(TransactionServiceProperties properties) {
        return new TransactionServiceMock(properties);
    }

}
