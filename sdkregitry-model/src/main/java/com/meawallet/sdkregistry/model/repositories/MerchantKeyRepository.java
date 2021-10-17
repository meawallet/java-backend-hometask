package com.meawallet.sdkregistry.model.repositories;

import com.meawallet.sdkregistry.model.entities.SdkMerchantKey;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface MerchantKeyRepository extends ReactiveCrudRepository<SdkMerchantKey, String> {

    Mono<SdkMerchantKey> findByKid(String kid);
}
