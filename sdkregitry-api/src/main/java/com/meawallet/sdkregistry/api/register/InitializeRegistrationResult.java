package com.meawallet.sdkregistry.api.register;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedSdkKeyset;
import lombok.Value;

@Value
public class InitializeRegistrationResult {

    EncryptedSdkKeyset keyset;
}
