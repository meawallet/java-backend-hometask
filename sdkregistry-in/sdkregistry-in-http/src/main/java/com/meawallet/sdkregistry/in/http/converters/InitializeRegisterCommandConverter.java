package com.meawallet.sdkregistry.in.http.converters;

import com.meawallet.sdkregistry.api.register.InitializeRegistrationCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Component
public class InitializeRegisterCommandConverter implements HttpRequestConverter<InitializeRegistrationCommand> {

    @Override
    public Mono<InitializeRegistrationCommand> apply(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(InitializeRegistrationCommand.class);
    }
}
