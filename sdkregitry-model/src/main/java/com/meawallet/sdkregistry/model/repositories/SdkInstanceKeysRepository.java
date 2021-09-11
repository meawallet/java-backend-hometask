package com.meawallet.sdkregistry.model.repositories;

import com.meawallet.sdkregistry.model.Tables;
import com.meawallet.sdkregistry.model.entities.SdkInstanceKeysEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SdkInstanceKeysRepository extends ReactiveCrudRepository<SdkInstanceKeysEntity, Long> {

    @Query("select secret_key from " + Tables.SDK_INSTANCE_KEYS
            + " where keyset_id = :keysetId")
    Mono<String> findSecretKeyByKeysetId(String keysetId);

    @Modifying
    @Query("update " + Tables.SDK_INSTANCE_KEYS
            + " set is_active = true"
            + " where keyset_id = :keysetId")
    Mono<Void> activateByKeysetId(String keysetId);

    Mono<SdkInstanceKeysEntity> findBySdkInstanceId(String sdkInstanceId);
}
