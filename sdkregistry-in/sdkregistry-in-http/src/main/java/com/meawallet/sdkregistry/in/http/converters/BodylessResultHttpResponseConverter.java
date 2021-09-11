package com.meawallet.sdkregistry.in.http.converters;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class BodylessResultHttpResponseConverter implements HttpResponseConverter {

    @Override
    public Mono<ServerResponse> apply(Object o) {
        return ServerResponse.noContent().build();
    }
}
