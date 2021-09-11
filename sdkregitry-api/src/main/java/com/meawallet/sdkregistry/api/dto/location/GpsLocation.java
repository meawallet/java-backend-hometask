package com.meawallet.sdkregistry.api.dto.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GpsLocation {

    @NotNull
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @Digits(integer = 3, fraction = 8)
    public BigDecimal latitude;

    @NotNull
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @Digits(integer = 3, fraction = 8)
    public BigDecimal longitude;

}
