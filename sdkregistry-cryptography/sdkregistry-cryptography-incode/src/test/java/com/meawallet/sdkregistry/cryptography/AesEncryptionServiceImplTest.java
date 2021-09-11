package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

class AesEncryptionServiceImplTest {

    AesEncryptionService victim = new AesEncryptionServiceImpl();

    byte[] key = "1234567891234567".getBytes(StandardCharsets.UTF_8);
    String plainData = "plain_data";

    @Test
    void shouldEncryptWithPkcs5Padding() {
        var plainBytes = plainData.getBytes(StandardCharsets.UTF_8);
        var encrypt = victim.encryptEcb(plainBytes, key)
                            .map(Hex::encodeHexString);
        StepVerifier.create(encrypt)
                    .expectNext("9e3e3b3e17e9dcacc252b013cc9564d1")
                    .verifyComplete();
    }

    @Test
    void shouldDecryptWithPkcs5Padding() throws Exception {
        var encryptedData = Hex.decodeHex("9e3e3b3e17e9dcacc252b013cc9564d1");
        var decrypt = victim.decryptEcb(encryptedData, key)
                            .map(String::new);
        StepVerifier.create(decrypt)
                    .expectNext(plainData)
                    .verifyComplete();
    }
}
