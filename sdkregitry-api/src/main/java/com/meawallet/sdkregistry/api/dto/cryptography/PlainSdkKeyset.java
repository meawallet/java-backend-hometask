package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.Value;

@Value
public class PlainSdkKeyset {

    String keySetId;
    PlainPushKeys push;
    PlainEncMacKeys attestation;
    PlainEncMacKeys transaction;
    PlainSecretKey secretKey;

}
