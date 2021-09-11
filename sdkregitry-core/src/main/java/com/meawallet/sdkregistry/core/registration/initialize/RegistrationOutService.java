package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import reactor.core.publisher.Mono;

public interface RegistrationOutService {

    Mono<Void> register(Sdk sdk);
}
