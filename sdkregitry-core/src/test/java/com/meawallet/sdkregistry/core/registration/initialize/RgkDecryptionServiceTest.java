package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedRgk;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainRgk;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.model.entities.SdkSharedKeyEntity;
import com.meawallet.sdkregistry.model.repositories.SdkSharedKeyRepository;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.meawallet.sdkregistry.core.TestData.ENC_RGK_ENC_KEY_UNDER_SHARED;
import static com.meawallet.sdkregistry.core.TestData.ENC_RGK_MAC_KEY_UNDER_SHARED;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_RGK_ENC_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_RGK_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.SHARED_KEY;
import static com.meawallet.sdkregistry.core.TestData.SHARED_KEY_FINGERPRINT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class RgkDecryptionServiceTest {

    @Mock
    SdkSharedKeyRepository sdkSharedKeyRepository;
    @Mock
    AesEncryptionService aesEncryptionService;

    @InjectMocks
    RgkDecryptionService victim;

    EncryptedRgk encryptedRgk = new EncryptedRgk(ENC_RGK_ENC_KEY_UNDER_SHARED, ENC_RGK_MAC_KEY_UNDER_SHARED);

    @Test
    void shouldFailWhenSharedKeyNotFound() {
        given(sdkSharedKeyRepository.findByFingerprint(SHARED_KEY_FINGERPRINT))
                .willReturn(Mono.empty());
        StepVerifier.create(victim.decrypt(encryptedRgk, SHARED_KEY_FINGERPRINT))
                    .expectError()
                    .verify();
    }

    @Test
    void shouldDecryptRgk() throws Exception {
        given(sdkSharedKeyRepository.findByFingerprint(SHARED_KEY_FINGERPRINT))
                .willReturn(Mono.just(new SdkSharedKeyEntity(1L, SHARED_KEY_FINGERPRINT, SHARED_KEY)));
        var plainEncKey = Hex.decodeHex(PLAIN_RGK_ENC_KEY);
        var plainMacKey = Hex.decodeHex(PLAIN_RGK_MAC_KEY);
        var encryptedEncKeyBytes = Hex.decodeHex(ENC_RGK_ENC_KEY_UNDER_SHARED);
        var encryptedMacKeyBytes = Hex.decodeHex(ENC_RGK_MAC_KEY_UNDER_SHARED);
        var sharedKeyBytes = Hex.decodeHex(SHARED_KEY);

        given(aesEncryptionService.decryptEcb(eq(encryptedEncKeyBytes), eq(sharedKeyBytes)))
                .willReturn(Mono.just(plainEncKey));

        given(aesEncryptionService.decryptEcb(eq(encryptedMacKeyBytes), eq(sharedKeyBytes)))
                .willReturn(Mono.just(plainMacKey));

        StepVerifier.create(victim.decrypt(encryptedRgk, SHARED_KEY_FINGERPRINT))
                    .expectNext(new PlainRgk(plainEncKey, plainMacKey))
                    .verifyComplete();
    }
}
