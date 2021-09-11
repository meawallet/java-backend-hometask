package com.meawallet.sdkregistry.api.errors;

import lombok.Getter;
import reactor.core.publisher.Mono;

import static com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode.INTERNAL;
import static com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode.INTERNAL_RETRYABLE;
import static com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode.INVALID_SECRET_VALUE;
import static com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode.KEYSET_NOT_FOUND;
import static com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode.SDK_INSTANCE_ALREADY_EXISTS;
import static com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode.SHARED_KEY_NOT_FOUND;

@Getter
public final class SdkRegistryException extends RuntimeException {

    private final SdkRegistryError error;

    private SdkRegistryException(SdkRegistryError error, Throwable cause) {
        super(String.format("Failed with : %s : %s", error.getCode(), error.getDescription()), cause);
        this.error = error;
    }

    private SdkRegistryException(SdkRegistryError error) {
        this(error, null);
    }

    public static <T> Mono<T> sdkInstanceAlreadyExists(String sdkInstance) {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    SDK_INSTANCE_ALREADY_EXISTS,
                    String.format("Sdk instance id '%s' is already registered. "
                            + "Generate another sdk instance id and start registration again", sdkInstance)
            );
            return new SdkRegistryException(error);
        });
    }

    public static <T> Mono<T> sharedKeyNotFound(String fingerprint) {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    SHARED_KEY_NOT_FOUND,
                    "Private key not found by fingerprint : " + fingerprint
            );
            return new SdkRegistryException(error);
        });
    }

    public static <T> Mono<T> keysetNotFound(String keysetId) {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    KEYSET_NOT_FOUND,
                    "Keyset not found by : " + keysetId
            );
            return new SdkRegistryException(error);
        });
    }

    public static <T> Mono<T> invalidSecretValue() {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    INVALID_SECRET_VALUE,
                    "Calculated secret value does not match"
            );
            return new SdkRegistryException(error);
        });
    }

    public static <T> Mono<T> internalError(String message) {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    INTERNAL,
                    message
            );
            return new SdkRegistryException(error);
        });
    }

    public static <T> Mono<T> internalError(String message, Throwable cause) {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    INTERNAL,
                    message
            );
            return new SdkRegistryException(error, cause);
        });
    }

    public static <T> Mono<T> internalRetryableError(String message) {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    INTERNAL_RETRYABLE,
                    message
            );
            return new SdkRegistryException(error);
        });
    }

    public static <T> Mono<T> internalRetryableError(String message, Throwable cause) {
        return Mono.error(() -> {
            var error = new SdkRegistryError(
                    INTERNAL_RETRYABLE,
                    message
            );
            return new SdkRegistryException(error, cause);
        });
    }
}
