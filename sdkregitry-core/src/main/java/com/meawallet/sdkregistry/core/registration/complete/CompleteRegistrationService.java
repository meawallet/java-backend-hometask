package com.meawallet.sdkregistry.core.registration.complete;

import com.meawallet.sdkregistry.api.register.CompleteRegistrationCommand;
import com.meawallet.sdkregistry.api.register.CompleteRegistrationResult;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.model.repositories.SdkInstanceKeysRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.meawallet.sdkregistry.api.errors.SdkRegistryException.invalidSecretValue;
import static com.meawallet.sdkregistry.api.errors.SdkRegistryException.keysetNotFound;
import static reactor.core.publisher.Mono.defer;

@Service
@AllArgsConstructor
@Slf4j
public class CompleteRegistrationService {

    private final AesEncryptionService aesEncryptionService;
    private final SdkInstanceKeysRepository sdkInstanceKeysRepository;

    public Mono<CompleteRegistrationResult> complete(CompleteRegistrationCommand command) {
        var activation = findSecretKey(command.getKeysetId())
                .flatMap(secretKey -> encryptKeysetId(secretKey, command.getKeysetId()))
                .flatMap(secretValue -> assertSecretsEquals(command.getSecretValue(), secretValue))
                .then(activateKeys(command.getKeysetId()))
                .thenReturn(new CompleteRegistrationResult());
        return Mono.fromRunnable(() -> log.debug("About to complete registration : {}", command))
                   .then(activation)
                   .doOnNext(result -> log.debug("Registration completed with keyset id : {}", command.getKeysetId()));
    }

    private Mono<byte[]> findSecretKey(String keysetId) {
        return sdkInstanceKeysRepository.findSecretKeyByKeysetId(keysetId)
                                        .map(this::decode)
                                        .switchIfEmpty(keysetNotFound(keysetId));
    }

    private Mono<String> encryptKeysetId(byte[] secretKey, String keysetId) {
        return Mono.fromCallable(() -> Hex.decodeHex(keysetId))
                   .flatMap(plainData -> aesEncryptionService.encryptEcb(plainData, secretKey))
                   .map(Hex::encodeHexString);
    }

    private Mono<Void> assertSecretsEquals(String actual, String expected) {
        return Mono.fromSupplier(() -> actual.equalsIgnoreCase(expected))
                   .filter(Boolean::booleanValue)
                   .switchIfEmpty(invalidSecretValue())
                   .then();
    }

    private Mono<Void> activateKeys(String keysetId) {
        return defer(() -> sdkInstanceKeysRepository.activateByKeysetId(keysetId));
    }

    @SneakyThrows
    private byte[] decode(String hex) {
        return Hex.decodeHex(hex);
    }
}
