package com.meawallet.sdkregistry.in.http.properties;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
public class HttpProperties {

    @NotNull
    private Duration globalTimeout;

}
