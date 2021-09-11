package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedEncMacKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedPushKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedSdkKeyset;
import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedSecretKey;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainEncMacKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainPushKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainRgk;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSdkKeyset;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSecretKey;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

@Service
@AllArgsConstructor
@Slf4j
class PlainSdkKeysetEncryptionService {

    private final AesEncryptionService aesEncryptionService;

    public Mono<EncryptedSdkKeyset> encrypt(PlainSdkKeyset keys, PlainRgk rgk) {
        var keySetId = Mono.just(keys.getKeySetId());
        var push = pushKeys(keys.getPush(), rgk);
        var attestation = encMacKeys(keys.getAttestation(), rgk);
        var transaction = encMacKeys(keys.getTransaction(), rgk);
        var secretKey = secretKey(keys.getSecretKey(), rgk);
        return Mono.zip(keySetId, push, attestation, transaction, secretKey)
                   .map(TupleUtils.function(EncryptedSdkKeyset::new));
    }

    private Mono<EncryptedPushKeys> pushKeys(PlainPushKeys encryptedPushKeys, PlainRgk rgk) {
        var dataEncKey = encryptOne(encryptedPushKeys.getDataEncryptionKey(), rgk);
        var dataMacKey = encryptOne(encryptedPushKeys.getDataMacKey(), rgk);
        var messageMacKey = encryptOne(encryptedPushKeys.getMessageMacKey(), rgk);
        return Mono.zip(dataEncKey, dataMacKey, messageMacKey)
                   .map(TupleUtils.function(EncryptedPushKeys::new));
    }

    private Mono<EncryptedEncMacKeys> encMacKeys(PlainEncMacKeys encryptedEncMacKeys, PlainRgk rgk) {
        var encKey = encryptOne(encryptedEncMacKeys.getEncryptionKey(), rgk);
        var macKey = encryptOne(encryptedEncMacKeys.getMacKey(), rgk);
        return Mono.zip(encKey, macKey)
                   .map(TupleUtils.function(EncryptedEncMacKeys::new));
    }

    private Mono<EncryptedSecretKey> secretKey(PlainSecretKey secretKey, PlainRgk rgk) {
        return encryptOne(secretKey.getKey(), rgk)
                .map(key -> new EncryptedSecretKey(key, secretKey.getFingerprint()));
    }

    private Mono<String> encryptOne(String plainKey, PlainRgk rgk) {
        return Mono.fromCallable(() -> Hex.decodeHex(plainKey))
                   .flatMap(bytes -> aesEncryptionService.encryptEcb(bytes, rgk.getEncryptionKey()))
                   .map(Hex::encodeHexString)
                   .doOnNext(encryptedKey -> log.debug("Key encrypted from : {}->{}", plainKey, encryptedKey));
    }
}
