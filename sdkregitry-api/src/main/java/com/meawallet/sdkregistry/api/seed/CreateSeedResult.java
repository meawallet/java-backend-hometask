package com.meawallet.sdkregistry.api.seed;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CreateSeedResult {
    @NotBlank
    String seed1;
    @NotBlank
    String seed2;
}
