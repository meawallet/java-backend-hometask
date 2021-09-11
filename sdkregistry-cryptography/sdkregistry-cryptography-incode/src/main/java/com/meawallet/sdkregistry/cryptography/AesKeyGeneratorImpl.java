package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.AesKeyGenerator;
import com.meawallet.sdkregistry.core.cryptography.KeyParameters;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class AesKeyGeneratorImpl implements AesKeyGenerator {

    private final RandomGenerator randomGenerator;

    @Override
    public Mono<byte[]> generate(KeyParameters params) {
        return Mono.fromSupplier(() -> params.getKeyType().getSize() / 8)
                   .flatMap(randomGenerator::generate);
    }
}
