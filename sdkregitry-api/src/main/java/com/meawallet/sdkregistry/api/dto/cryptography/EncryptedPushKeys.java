package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class EncryptedPushKeys {

    String dataEncryptionKey;
    String dataMacKey;
    String messageMacKey;

}
