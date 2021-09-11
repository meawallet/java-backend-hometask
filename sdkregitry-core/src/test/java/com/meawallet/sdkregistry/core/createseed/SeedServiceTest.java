package com.meawallet.sdkregistry.core.createseed;

import com.meawallet.sdkregistry.api.seed.CreateSeedCommand;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class SeedServiceTest {

    @Mock
    RandomGenerator generator;

    @InjectMocks
    SeedService victim;

    byte[] seed1 = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    byte[] seed2 = new byte[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    @Test
    void shouldGenerateTwoDifferentSeeds() {
        given(generator.generate(10)).willReturn(
                Mono.just(seed1),
                Mono.just(seed2)
        );
        StepVerifier.create(victim.create(new CreateSeedCommand(10)))
                    .assertNext(result -> {
                        assertThat(result.getSeed1()).isEqualTo("0102030405060708090a");
                        assertThat(result.getSeed2()).isEqualTo("0a090807060504030201");
                    }).verifyComplete();
    }

    @Test
    void should() {
    }
}
