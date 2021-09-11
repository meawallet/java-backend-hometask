package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.FingerprintCalculator;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

class FingerprintCalculatorSha256Test {

    FingerprintCalculator victim = new FingerprintCalculatorSha256();
    byte[] key = "1234567891234567".getBytes(StandardCharsets.UTF_8);

    @Test
    void shouldCalculateFingerprint() {
        var calculation = victim.calculate(key)
                                .map(Hex::encodeHexString);
        StepVerifier.create(calculation)
                    .expectNext("59f271f6309355962f0fec64dca36cff44776ea4e199ed0a350147e15e0f3a6c")
                    .verifyComplete();
    }
}
