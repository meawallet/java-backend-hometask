package com.meawallet.sdkregistry.api.dto.sdk;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedSdkKeyset;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSdkKeyset;
import com.meawallet.sdkregistry.api.dto.location.GpsLocation;
import lombok.Value;

@Value
public class Sdk {

    String sdkId;
    String sdkInstanceId;
    String publicKeyDer;
    String fcmRegistrationId;
    GpsLocation gpsLocation;
    PlainSdkKeyset plainSdkKeyset;
    EncryptedSdkKeyset encryptedSdkKeyset;

}
