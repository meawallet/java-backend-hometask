package com.meawallet.sdkregistry.core.cryptography;

import reactor.core.publisher.Mono;

public interface RandomGenerator {

    Mono<byte[]> generate(int length);

}
