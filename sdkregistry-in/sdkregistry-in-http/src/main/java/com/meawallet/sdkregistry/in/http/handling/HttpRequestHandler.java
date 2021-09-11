package com.meawallet.sdkregistry.in.http.handling;

import com.meawallet.sdkregistry.in.http.converters.HttpRequestConverter;
import com.meawallet.sdkregistry.in.http.converters.HttpResponseConverter;
import com.meawallet.sdkregistry.in.http.validation.ReactiveValidator;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@AllArgsConstructor
public class HttpRequestHandler<T, R> implements HandlerFunction<ServerResponse> {

    private final ReactiveValidator reactiveValidator;
    private final HttpRequestConverter<T> httpRequestConverter;
    private final HttpResponseConverter httpResponseConverter;
    private final Function<T, Mono<R>> operation;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return Mono.just(request)
                   .flatMap(httpRequestConverter)
                   .flatMap(reactiveValidator::validate)
                   .flatMap(operation)
                   .flatMap(reactiveValidator::validate)
                   .flatMap(httpResponseConverter);
    }
}
