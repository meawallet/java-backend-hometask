package com.meawallet.sdkregistry.in.http.converters;

import com.meawallet.sdkregistry.in.http.api.HttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class BodyfullResultHttpResponseConverter implements HttpResponseConverter {

    @Override
    public Mono<ServerResponse> apply(Object o) {
        return Mono.fromSupplier(() -> new HttpResponse(o))
                   .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}
