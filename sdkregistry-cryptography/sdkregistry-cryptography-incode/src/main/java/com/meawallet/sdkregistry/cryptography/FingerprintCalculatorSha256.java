package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.FingerprintCalculator;
import org.apache.commons.codec.binary.Hex;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;

public class FingerprintCalculatorSha256 implements FingerprintCalculator {

    public static void main(String[] args) throws Exception {
        var key = Hex.decodeHex("7ba90d2e4d5e40341edfe5302aa9bfd5");
        var digest = MessageDigest.getInstance("SHA-256");
        var fp = Hex.encodeHexString(digest.digest(key));
        System.out.println(fp);
    }

    @Override
    public Mono<byte[]> calculate(byte[] key) {
        return Mono.fromCallable(() -> {
            var digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(key);
        });
    }
}
