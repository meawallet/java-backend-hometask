package com.meawallet.sdkregistry.core.registration.complete;

import com.meawallet.sdkregistry.api.register.CompleteRegistrationCommand;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.model.repositories.SdkInstanceKeysRepository;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.meawallet.sdkregistry.core.TestData.KEYSET_ID;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_SECRET_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@MockitoSettings
class CompleteRegistrationServiceTest {

    @Mock
    AesEncryptionService aesEncryptionService;
    @Mock
    SdkInstanceKeysRepository sdkInstanceKeysRepository;
    @InjectMocks
    CompleteRegistrationService victim;

    String secretValue = "12205e4db13e8472c381bfb36bf2c45bfed95b86dabe542286c71589a66d52e1";

    @Test
    void shouldFailWhenKeysNotFound() {
        given(sdkInstanceKeysRepository.findSecretKeyByKeysetId(KEYSET_ID))
                .willReturn(Mono.empty());
        StepVerifier.create(victim.complete(new CompleteRegistrationCommand(KEYSET_ID, secretValue)))
                    .expectErrorSatisfies(t -> assertThat(t).hasMessageContaining("KEYSET_NOT_FOUND"))
                    .verify();
        then(sdkInstanceKeysRepository).should(never()).activateByKeysetId(any());
    }

    @Test
    void shouldFailWhenSecretValueDoesNotMatch() throws Exception {
        given(sdkInstanceKeysRepository.findSecretKeyByKeysetId(KEYSET_ID))
                .willReturn(Mono.just(PLAIN_SECRET_KEY));
        var keysetIdBytes = Hex.decodeHex(KEYSET_ID);
        var secretKeyBytes = Hex.decodeHex(PLAIN_SECRET_KEY);
        given(aesEncryptionService.encryptEcb(eq(keysetIdBytes), eq(secretKeyBytes)))
                .willReturn(Mono.just(new byte[16]));
        StepVerifier.create(victim.complete(new CompleteRegistrationCommand(KEYSET_ID, secretValue)))
                    .expectErrorSatisfies(t -> assertThat(t).hasMessageContaining("INVALID_SECRET_VALUE"))
                    .verify();
        then(sdkInstanceKeysRepository).should(never()).activateByKeysetId(any());
    }

    @Test
    void shouldActivateKeysOnSuccessfulComplete() throws Exception {
        given(sdkInstanceKeysRepository.findSecretKeyByKeysetId(KEYSET_ID))
                .willReturn(Mono.just(PLAIN_SECRET_KEY));
        var keysetIdBytes = Hex.decodeHex(KEYSET_ID);
        var secretKeyBytes = Hex.decodeHex(PLAIN_SECRET_KEY);
        given(aesEncryptionService.encryptEcb(eq(keysetIdBytes), eq(secretKeyBytes)))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(secretValue)));
        given(sdkInstanceKeysRepository.activateByKeysetId(KEYSET_ID))
                .willReturn(Mono.empty());
        StepVerifier.create(victim.complete(new CompleteRegistrationCommand(KEYSET_ID, secretValue)))
                    .expectNextCount(1)
                    .verifyComplete();
    }
}
