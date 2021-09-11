package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class PlainSecretKey {

    String key;
    String fingerprint;

}
