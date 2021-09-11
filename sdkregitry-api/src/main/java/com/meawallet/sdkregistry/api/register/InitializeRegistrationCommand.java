package com.meawallet.sdkregistry.api.register;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedRgk;
import com.meawallet.sdkregistry.api.dto.location.GpsLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitializeRegistrationCommand {

    @NotBlank
    private String sdkId;

    @NotNull
    @Pattern(regexp = "[a-fA-F0-9]{32,64}")
    private String sdkInstanceId;

    @NotBlank
    @Pattern(regexp = "[a-fA-F0-9]{588}")
    private String devicePublicKey;

    @NotBlank
    @Size(max = 64)
    private String shredKeyFingerprint;

    @NotEmpty
    @Size(max = 2000)
    private String fcmRegistrationId;

    @NotNull
    @Valid
    private EncryptedRgk rgk;

    @NotNull
    @Valid
    private GpsLocation gpsLocation;
}
