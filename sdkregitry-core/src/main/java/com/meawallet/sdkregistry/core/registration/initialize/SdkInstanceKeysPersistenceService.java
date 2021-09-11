package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import com.meawallet.sdkregistry.model.entities.SdkInstanceKeysEntity;
import com.meawallet.sdkregistry.model.repositories.SdkInstanceKeysRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
class SdkInstanceKeysPersistenceService {

    private final SdkInstanceKeysRepository sdkInstanceKeysRepository;

    public Mono<SdkInstanceKeysEntity> create(Sdk sdk) {
        return Mono.fromSupplier(() -> convert(sdk))
                   .flatMap(sdkInstanceKeysRepository::save);
    }

    private SdkInstanceKeysEntity convert(Sdk sdk) {
        return new SdkInstanceKeysEntity(
                sdk.getSdkInstanceId(),
                sdk.getPlainSdkKeyset().getKeySetId(),
                sdk.getPlainSdkKeyset().getPush().getDataEncryptionKey(),
                sdk.getPlainSdkKeyset().getPush().getDataMacKey(),
                sdk.getPlainSdkKeyset().getPush().getMessageMacKey(),
                sdk.getPlainSdkKeyset().getAttestation().getEncryptionKey(),
                sdk.getPlainSdkKeyset().getAttestation().getMacKey(),
                sdk.getPlainSdkKeyset().getTransaction().getEncryptionKey(),
                sdk.getPlainSdkKeyset().getTransaction().getMacKey(),
                sdk.getPlainSdkKeyset().getSecretKey().getKey(),
                sdk.getPlainSdkKeyset().getSecretKey().getFingerprint()
        );
    }

}
