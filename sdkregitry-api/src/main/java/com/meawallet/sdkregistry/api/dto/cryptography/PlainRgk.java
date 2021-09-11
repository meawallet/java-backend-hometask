package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class PlainRgk {

    byte[] encryptionKey;
    byte[] macKey;
}
