package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedRgk;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainRgk;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.model.entities.SdkSharedKeyEntity;
import com.meawallet.sdkregistry.model.repositories.SdkSharedKeyRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import static com.meawallet.sdkregistry.api.errors.SdkRegistryException.sharedKeyNotFound;

@Service
@AllArgsConstructor
class RgkDecryptionService {

    private final SdkSharedKeyRepository sdkSharedKeyRepository;
    private final AesEncryptionService aesEncryptionService;

    public Mono<PlainRgk> decrypt(EncryptedRgk rgk, String fingerprint) {
        return findSharedKey(fingerprint)
                .flatMap(privateKey -> {
                    var encKey = decryptOne(rgk.getEncryptedEncryptionKey(), privateKey);
                    var macKey = decryptOne(rgk.getEncryptedMacKey(), privateKey);
                    return encKey.zipWith(macKey)
                                 .map(TupleUtils.function(PlainRgk::new));
                });
    }

    private Mono<byte[]> findSharedKey(String fingerprint) {
        return sdkSharedKeyRepository.findByFingerprint(fingerprint)
                                     .map(SdkSharedKeyEntity::getSharedAesKeyHex)
                                     .map(this::decode)
                                     .switchIfEmpty(sharedKeyNotFound(fingerprint));
    }

    private Mono<byte[]> decryptOne(String encryptedKey, byte[] sharedKey) {
        return Mono.fromSupplier(() -> decode(encryptedKey))
                   .flatMap(bytes -> aesEncryptionService.decryptEcb(bytes, sharedKey));
    }

    @SneakyThrows
    private byte[] decode(String hex) {
        return Hex.decodeHex(hex);
    }

}
