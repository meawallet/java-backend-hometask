package com.meawallet.sdkregistry.core.merchantkey;

import com.meawallet.sdkregistry.api.dto.merchantKey.MerchantKey;
import com.meawallet.sdkregistry.model.repositories.MerchantKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MerchantKeyService {

    private MerchantKeyRepository merchantKeyRepository;

    public Mono<MerchantKey> findByKid(String kid) {
        return merchantKeyRepository
                .findByKid(kid)
                .map(entity -> new MerchantKey(
                        entity.getId(),
                        entity.getMerchantName(),
                        entity.getPublicKey(),
                        entity.getKid()
                ));
    }
}
