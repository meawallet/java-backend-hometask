package com.meawallet.sdkregistry.in.http.converters;

import com.meawallet.sdkregistry.api.register.CompleteRegistrationCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

@Component
public class CompleteRegisterCommandConverter implements HttpRequestConverter<CompleteRegistrationCommand> {

    @Override
    public Mono<CompleteRegistrationCommand> apply(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CompleteRegistrationCommand.class);
    }
}
