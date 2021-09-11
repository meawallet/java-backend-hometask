package com.meawallet.sdkregistry.core.cryptography;

import reactor.core.publisher.Mono;

public interface FingerprintCalculator {

    Mono<byte[]> calculate(byte[] key);

}
