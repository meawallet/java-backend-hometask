package com.meawallet.sdkregistry.cryptography;

import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptionServiceImpl implements AesEncryptionService {

    @Override
    public Mono<byte[]> decryptEcb(byte[] data, byte[] key) {
        return doCipher(Cipher.DECRYPT_MODE, data, key);
    }

    @Override
    public Mono<byte[]> encryptEcb(byte[] data, byte[] key) {
        return doCipher(Cipher.ENCRYPT_MODE, data, key);
    }

    private Mono<byte[]> doCipher(int mode, byte[] data, byte[] key) {
        return Mono.fromCallable(() -> {
            var aesKey = new SecretKeySpec(key, "AES");
            var cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(mode, aesKey);
            return cipher.doFinal(data);
        });
    }
}
