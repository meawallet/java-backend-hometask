package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KeyType {

    AES_128(128),
    AES_256(256),
    ;

    private final int size;
}
