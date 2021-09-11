package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import com.meawallet.sdkregistry.model.entities.SdkInstanceEntity;
import com.meawallet.sdkregistry.model.repositories.SdkInstanceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.meawallet.sdkregistry.api.errors.SdkRegistryException.sdkInstanceAlreadyExists;

@Service
@AllArgsConstructor
@Slf4j
class SdkInstancePersistenceService {

    private final SdkInstanceRepository repository;

    public Mono<SdkInstanceEntity> create(Sdk sdk) {
        return repository.existsBySdkInstanceId(sdk.getSdkInstanceId())
                         .flatMap(exists -> exists
                                            ? failWithAlreadyExists(sdk)
                                            : save(sdk));
    }

    private Mono<SdkInstanceEntity> failWithAlreadyExists(Sdk sdk) {
        return Mono.fromRunnable(() -> log.debug("Unable to save sdk instance as it already exists : {}", sdk.getSdkInstanceId()))
                   .then(sdkInstanceAlreadyExists(sdk.getSdkInstanceId()));
    }

    private Mono<SdkInstanceEntity> save(Sdk sdk) {
        return Mono.fromSupplier(() -> convert(sdk))
                   .flatMap(repository::save)
                   .doOnNext(saved -> log.debug("Saved new sdk instance : {}", saved));
    }

    private SdkInstanceEntity convert(Sdk sdk) {
        return new SdkInstanceEntity(
                sdk.getSdkInstanceId(),
                sdk.getSdkId(),
                sdk.getFcmRegistrationId(),
                sdk.getGpsLocation().getLatitude(),
                sdk.getGpsLocation().getLongitude()
        );
    }

}
