package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.core.cryptography.AesKeyGenerator;
import com.meawallet.sdkregistry.core.cryptography.FingerprintCalculator;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class CryptographyConfig {

    @Bean
    @ConditionalOnMissingBean(SecureRandom.class)
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    @ConditionalOnMissingBean(AesEncryptionService.class)
    public AesEncryptionService aesEncryptionService() {
        return new AesEncryptionServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(RandomGenerator.class)
    public RandomGenerator randomGenerator(SecureRandom secureRandom) {
        return new RandomGeneratorImpl(secureRandom);
    }

    @Bean
    @ConditionalOnMissingBean(AesKeyGenerator.class)
    public AesKeyGenerator aesKeyGenerator(RandomGenerator randomGenerator) {
        return new AesKeyGeneratorImpl(randomGenerator);
    }

    @Bean
    @ConditionalOnMissingBean(FingerprintCalculator.class)
    public FingerprintCalculator fingerprintCalculator() {
        return new FingerprintCalculatorSha256();
    }

}
