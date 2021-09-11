package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.api.dto.cryptography.KeyType;
import com.meawallet.sdkregistry.core.cryptography.KeyParameters;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

@MockitoSettings
class AesKeyGeneratorImplTest {

    @Mock
    RandomGenerator randomGenerator;

    @InjectMocks
    AesKeyGeneratorImpl victim;

    @Test
    void shouldGenerateAes128Key() {
        var key = new byte[16];
        given(randomGenerator.generate(16)).willReturn(Mono.just(key));
        var params = new KeyParameters(KeyType.AES_128, "key");
        StepVerifier.create(victim.generate(params))
                    .expectNext(key)
                    .verifyComplete();
    }

    @Test
    void shouldGenerateAes256Key() {
        var key = new byte[32];
        given(randomGenerator.generate(32)).willReturn(Mono.just(key));
        var params = new KeyParameters(KeyType.AES_256, "key");
        StepVerifier.create(victim.generate(params))
                    .expectNext(key)
                    .verifyComplete();
    }
}
