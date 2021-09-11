package com.meawallet.sdkregistry.cryptography;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.test.StepVerifier;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willAnswer;

@MockitoSettings
class RandomGeneratorImplTest {

    @Mock
    SecureRandom secureRandom;

    @InjectMocks
    RandomGeneratorImpl victim;

    @Test
    void shouldGenerateRandom() {
        willAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            Arrays.fill(bytes, (byte) 1);
            return null;
        }).given(secureRandom).nextBytes(any());
        StepVerifier.create(victim.generate(3))
                    .assertNext(bytes -> {
                        assertThat(bytes).isEqualTo(new byte[]{1, 1, 1});
                    }).verifyComplete();
    }
}
