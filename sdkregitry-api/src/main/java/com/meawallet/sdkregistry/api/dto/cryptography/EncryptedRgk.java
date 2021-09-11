package com.meawallet.sdkregistry.api.dto.cryptography;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncryptedRgk {
    @NotNull
    @Pattern(regexp = "[a-fA-F0-9]{2,768}")
    private String encryptedEncryptionKey;

    @NotNull
    @Pattern(regexp = "[a-fA-F0-9]{2,768}")
    private String encryptedMacKey;
}
