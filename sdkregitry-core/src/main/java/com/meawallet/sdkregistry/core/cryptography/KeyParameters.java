package com.meawallet.sdkregistry.core.cryptography;

import com.meawallet.sdkregistry.api.dto.cryptography.KeyType;
import lombok.Value;

@Value
public class KeyParameters {

    KeyType keyType;
    String keyName;

}
