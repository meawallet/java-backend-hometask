package com.meawallet.sdkregistry.model.repositories;

import com.meawallet.sdkregistry.model.entities.SdkSharedKeyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SdkSharedKeyRepository extends ReactiveCrudRepository<SdkSharedKeyEntity, Long> {

    Mono<SdkSharedKeyEntity> findByFingerprint(String fingerprint);
}
