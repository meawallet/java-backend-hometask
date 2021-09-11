package com.meawallet.sdkregistry.in.http.converters;

import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface HttpResponseConverter extends Function<Object, Mono<ServerResponse>> {
}
