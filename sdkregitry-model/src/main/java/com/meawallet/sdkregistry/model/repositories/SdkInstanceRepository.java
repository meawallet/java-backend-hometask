package com.meawallet.sdkregistry.model.repositories;

import com.meawallet.sdkregistry.model.entities.SdkInstanceEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SdkInstanceRepository extends ReactiveCrudRepository<SdkInstanceEntity, Long> {

    Mono<SdkInstanceEntity> findBySdkInstanceId(String sdkInstanceId);

    Mono<Boolean> existsBySdkInstanceId(String sdkInstanceId);

}
