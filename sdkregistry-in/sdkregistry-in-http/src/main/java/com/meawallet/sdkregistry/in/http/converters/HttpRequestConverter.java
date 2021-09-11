package com.meawallet.sdkregistry.in.http.converters;

import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface HttpRequestConverter<T> extends Function<ServerRequest, Mono<T>> {
}
