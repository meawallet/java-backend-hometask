package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.core.cryptography.AesKeyGenerator;
import com.meawallet.sdkregistry.core.cryptography.FingerprintCalculator;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.meawallet.sdkregistry.core.TestData.KEYSET_ID;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_ATTESTATION_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_ATTESTATION_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_PUSH_DATA_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_PUSH_DATA_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_PUSH_MESSAGE_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_SECRET_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_TRANSACTION_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_TRANSACTION_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.SECRETKEY_FINGERPRINT;
import static com.meawallet.sdkregistry.core.TestData.getTestPlainKeyset;
import static com.meawallet.sdkregistry.core.registration.initialize.PlainSdkKeysetGenerationService.KEY_SET_ID_LENGTH;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class PlainSdkKeysetGenerationServiceTest {

    @Mock
    RandomGenerator randomGenerator;
    @Mock
    AesKeyGenerator aesKeyGenerator;
    @Mock
    FingerprintCalculator fingerprintCalculator;

    @InjectMocks
    PlainSdkKeysetGenerationService victim;

    @Test
    void shouldGenerateAllKeys() throws Exception {
        given(randomGenerator.generate(KEY_SET_ID_LENGTH))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(KEYSET_ID)));
        given(aesKeyGenerator.generate(SdkKey.PUSH_DATA_ENCRYPTION_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_PUSH_DATA_ENCRYPTION_KEY)));
        given(aesKeyGenerator.generate(SdkKey.PUSH_DATA_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_PUSH_DATA_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.PUSH_MESSAGE_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_PUSH_MESSAGE_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.ATTESTATION_ENCRYPTION_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_ATTESTATION_ENCRYPTION_KEY)));
        given(aesKeyGenerator.generate(SdkKey.ATTESTATION_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_ATTESTATION_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.TRANSACTION_ENCRYPTION_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_TRANSACTION_ENCRYPTION_KEY)));
        given(aesKeyGenerator.generate(SdkKey.TRANSACTION_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_TRANSACTION_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.SECRET_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_SECRET_KEY)));
        given(fingerprintCalculator.calculate(eq(Hex.decodeHex(PLAIN_SECRET_KEY))))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(SECRETKEY_FINGERPRINT)));

        StepVerifier.create(victim.generate())
                    .expectNext(getTestPlainKeyset())
                    .verifyComplete();
    }
}
