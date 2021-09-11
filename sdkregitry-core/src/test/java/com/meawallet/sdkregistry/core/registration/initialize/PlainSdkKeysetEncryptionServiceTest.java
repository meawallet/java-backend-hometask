package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.cryptography.PlainRgk;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.meawallet.sdkregistry.core.TestData.*;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class PlainSdkKeysetEncryptionServiceTest {

    @Mock
    AesEncryptionService aesEncryptionService;

    @InjectMocks
    PlainSdkKeysetEncryptionService victim;

    @Test
    void shouldEncryptAllKeys() throws Exception {
        mockEncryption(PLAIN_PUSH_DATA_ENCRYPTION_KEY, ENC_PUSH_DATA_ENCRYPTION_KEY_UNDER_RGK);
        mockEncryption(PLAIN_PUSH_DATA_MAC_KEY, ENC_PUSH_DATA_MAC_KEY_UNDER_RGK);
        mockEncryption(PLAIN_PUSH_MESSAGE_MAC_KEY, ENC_PUSH_MESSAGE_MAC_KEY_UNDER_RGK);
        mockEncryption(PLAIN_ATTESTATION_ENCRYPTION_KEY, ENC_ATTESTATION_ENCRYPTION_KEY_UNDER_RGK);
        mockEncryption(PLAIN_ATTESTATION_MAC_KEY, ENC_ATTESTATION_MAC_KEY_UNDER_RGK);
        mockEncryption(PLAIN_TRANSACTION_ENCRYPTION_KEY, ENC_TRANSACTION_ENCRYPTION_KEY_UNDER_RGK);
        mockEncryption(PLAIN_TRANSACTION_MAC_KEY, ENC_TRANSACTION_MAC_KEY_UNDER_RGK);
        mockEncryption(PLAIN_SECRET_KEY, ENC_SECRET_KEY_UNDER_RGK);

        var rgk = new PlainRgk(Hex.decodeHex(PLAIN_RGK_ENC_KEY), Hex.decodeHex(PLAIN_RGK_MAC_KEY));
        var plainKeyset = getTestPlainKeyset();
        StepVerifier.create(victim.encrypt(plainKeyset, rgk))
                    .expectNext(getTestEncryptedKeyset())
                    .verifyComplete();
    }

    private void mockEncryption(String inputKeyHex, String outputKeyHex) throws Exception {
        var inKey = Hex.decodeHex(inputKeyHex);
        var sharedKey = Hex.decodeHex(PLAIN_RGK_ENC_KEY);
        var outKey = Hex.decodeHex(outputKeyHex);
        given(aesEncryptionService.encryptEcb(inKey, sharedKey))
                .willReturn(Mono.just(outKey));
    }
}
