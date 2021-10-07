package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;

import static reactor.core.scheduler.Schedulers.boundedElastic;

@AllArgsConstructor
public class RandomGeneratorImpl implements RandomGenerator {

    private final SecureRandom secureRandom;

    @Override
    public Mono<byte[]> generate(int length) {
        return Mono.fromCallable(() -> {
            var bytes = new byte[length];
            secureRandom.nextBytes(bytes);
            return bytes;
        }).subscribeOn(boundedElastic());
    }
}
