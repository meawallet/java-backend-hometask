package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.cryptography.KeyType;
import com.meawallet.sdkregistry.core.cryptography.KeyParameters;
import lombok.Getter;

@Getter
public enum SdkKey {

    PUSH_DATA_ENCRYPTION_KEY(KeyType.AES_128, "P1"),
    PUSH_DATA_MAC_KEY(KeyType.AES_128, "P2"),
    PUSH_MESSAGE_MAC_KEY(KeyType.AES_128, "P3"),
    ATTESTATION_ENCRYPTION_KEY(KeyType.AES_128, "A1"),
    ATTESTATION_MAC_KEY(KeyType.AES_128, "A2"),
    TRANSACTION_ENCRYPTION_KEY(KeyType.AES_128, "T1"),
    TRANSACTION_MAC_KEY(KeyType.AES_128, "T2"),
    SECRET_KEY(KeyType.AES_128, "S1"),
    ;

    private final KeyParameters params;

    SdkKey(KeyType keyType, String keyName) {
        this.params = new KeyParameters(keyType, keyName);
    }
}
