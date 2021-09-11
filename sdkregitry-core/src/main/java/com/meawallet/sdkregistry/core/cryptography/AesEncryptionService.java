package com.meawallet.sdkregistry.core.cryptography;

import reactor.core.publisher.Mono;

public interface AesEncryptionService {

    Mono<byte[]> decryptEcb(byte[] data, byte[] key);

    Mono<byte[]> encryptEcb(byte[] data, byte[] key);
}
