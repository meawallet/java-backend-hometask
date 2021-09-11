package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class PlainEncMacKeys {

    String encryptionKey;
    String macKey;

}
