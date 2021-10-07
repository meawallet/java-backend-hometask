package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.FingerprintCalculator;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;

import static reactor.core.scheduler.Schedulers.boundedElastic;

public class FingerprintCalculatorSha256 implements FingerprintCalculator {

    @Override
    public Mono<byte[]> calculate(byte[] key) {
        return Mono.fromCallable(() -> {
            var digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(key);
        }).subscribeOn(boundedElastic());
    }
}
