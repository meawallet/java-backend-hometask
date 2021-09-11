package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class EncryptedSecretKey {

    String key;
    String fingerprint;

}
