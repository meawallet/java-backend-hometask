package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.cryptography.PlainEncMacKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainPushKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSdkKeyset;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSecretKey;
import com.meawallet.sdkregistry.core.cryptography.AesKeyGenerator;
import com.meawallet.sdkregistry.core.cryptography.FingerprintCalculator;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import lombok.AllArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

@Service
@AllArgsConstructor
class PlainSdkKeysetGenerationService {

    public static final int KEY_SET_ID_LENGTH = 16;
    private final RandomGenerator randomGenerator;
    private final AesKeyGenerator aesKeyGenerator;
    private final FingerprintCalculator fingerprintCalculator;

    public Mono<PlainSdkKeyset> generate() {
        return Mono.zip(keySetId(), pushKeys(), attestation(), transaction(), secretKey())
                   .map(TupleUtils.function(PlainSdkKeyset::new));
    }

    private Mono<String> keySetId() {
        return Mono.defer(() -> randomGenerator.generate(KEY_SET_ID_LENGTH))
                   .map(Hex::encodeHexString);
    }

    private Mono<PlainPushKeys> pushKeys() {
        var dataEncKey = generateAes128Key(SdkKey.PUSH_DATA_ENCRYPTION_KEY);
        var dataMacKey = generateAes128Key(SdkKey.PUSH_DATA_MAC_KEY);
        var messageMacKey = generateAes128Key(SdkKey.PUSH_MESSAGE_MAC_KEY);
        return Mono.zip(dataEncKey, dataMacKey, messageMacKey)
                   .map(TupleUtils.function(PlainPushKeys::new));
    }

    private Mono<PlainEncMacKeys> attestation() {
        var encKey = generateAes128Key(SdkKey.ATTESTATION_ENCRYPTION_KEY);
        var macKey = generateAes128Key(SdkKey.ATTESTATION_MAC_KEY);
        return Mono.zip(encKey, macKey)
                   .map(TupleUtils.function(PlainEncMacKeys::new));
    }

    private Mono<PlainEncMacKeys> transaction() {
        var encKey = generateAes128Key(SdkKey.TRANSACTION_ENCRYPTION_KEY);
        var macKey = generateAes128Key(SdkKey.TRANSACTION_MAC_KEY);
        return Mono.zip(encKey, macKey)
                   .map(TupleUtils.function(PlainEncMacKeys::new));
    }

    private Mono<PlainSecretKey> secretKey() {
        return Mono.fromSupplier(SdkKey.SECRET_KEY::getParams)
                   .flatMap(aesKeyGenerator::generate)
                   .flatMap(key -> calculateFingerprint(key)
                           .map(fp -> {
                               var keyHex = Hex.encodeHexString(key);
                               return new PlainSecretKey(keyHex, fp);
                           }));
    }

    private Mono<String> generateAes128Key(SdkKey sdkKey) {
        return Mono.fromSupplier(sdkKey::getParams)
                   .flatMap(aesKeyGenerator::generate)
                   .map(Hex::encodeHexString);
    }

    private Mono<String> calculateFingerprint(byte[] key) {
        return fingerprintCalculator.calculate(key)
                                    .map(Hex::encodeHexString);
    }
}
