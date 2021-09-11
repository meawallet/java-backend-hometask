package com.meawallet.sdkregistry.api.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteRegistrationCommand {

    @NotBlank
    @Pattern(regexp = "[a-fA-F0-9]{8,256}")
    public String keysetId;

    @NotBlank
    @Pattern(regexp = "[a-fA-F0-9]{8,256}")
    public String secretValue;

}
