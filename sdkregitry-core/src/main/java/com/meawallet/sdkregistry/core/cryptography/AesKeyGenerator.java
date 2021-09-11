package com.meawallet.sdkregistry.core.cryptography;

import reactor.core.publisher.Mono;

public interface AesKeyGenerator {

    Mono<byte[]> generate(KeyParameters params);
}
