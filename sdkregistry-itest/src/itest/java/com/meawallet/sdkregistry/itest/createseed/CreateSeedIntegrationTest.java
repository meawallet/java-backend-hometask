package com.meawallet.sdkregistry.itest.createseed;

import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import com.meawallet.sdkregistry.itest.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static com.meawallet.sdkregistry.itest.utils.JsonAssertion.json;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;

class CreateSeedIntegrationTest extends BaseIntegrationTest {

    @Autowired
    RandomGenerator randomGenerator;

    @BeforeEach
    void setUp() {
        Mockito.reset(randomGenerator);
    }

    @Test
    void shouldCreateSeed() {
        doAnswer(invocation -> {
            return Mono.fromSupplier(() -> {
                int length = invocation.getArgument(0);
                var bytes = new byte[length];
                Arrays.fill(bytes, (byte) 0x01);
                return bytes;
            });
        }).when(randomGenerator).generate(anyInt());
        webClient()
                .get()
                .uri("/sdkregistry/v1/seed?length=20")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(json(readFile("api/jsons/createseed/createSeedOkResponse.json")));
    }

    @Test
    void shouldFailWhenLengthIsNotProvided() {
        webClient()
                .get()
                .uri("/sdkregistry/v1/seed")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(json(readFile("api/jsons/createseed/createSeedValidationFailedResponse.json")));
    }
}
