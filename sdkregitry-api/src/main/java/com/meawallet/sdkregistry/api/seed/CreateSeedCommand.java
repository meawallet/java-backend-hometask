package com.meawallet.sdkregistry.api.seed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeedCommand {

    @NotNull
    @Range(min = 10, max = 64)
    public Integer length;

}
