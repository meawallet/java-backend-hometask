package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class EncryptedSdkKeyset {

    String keySetId;
    EncryptedPushKeys push;
    EncryptedEncMacKeys attestation;
    EncryptedEncMacKeys transaction;
    EncryptedSecretKey secretKey;

}
