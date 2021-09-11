package com.meawallet.sdkregistry.api.errors;

import lombok.NonNull;
import lombok.Value;

@Value
public class SdkRegistryError {

    @NonNull
    SdkRegistryErrorCode code;

    @NonNull
    String description;

}
