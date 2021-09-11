package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class EncryptedEncMacKeys {

    String encryptionKey;
    String macKey;

}
