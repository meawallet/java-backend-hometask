package com.meawallet.sdkregistry.api.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SdkRegistryErrorCode {

    SDK_INSTANCE_ALREADY_EXISTS(Category.VALIDATION),
    SHARED_KEY_NOT_FOUND(Category.VALIDATION),
    KEYSET_NOT_FOUND(Category.VALIDATION),
    INVALID_SECRET_VALUE(Category.VALIDATION),
    PAYLOAD_VALIDATION_FAILED(Category.VALIDATION),
    UNPROCESSABLE(Category.VALIDATION),
    INTERNAL(Category.INTERNAL),
    INTERNAL_RETRYABLE(Category.INTERNAL),
    GATEWAY_TIMEOUT(Category.INTERNAL),
    ;
    private final Category category;

    public enum Category {
        VALIDATION,
        INTERNAL;
    }

}
