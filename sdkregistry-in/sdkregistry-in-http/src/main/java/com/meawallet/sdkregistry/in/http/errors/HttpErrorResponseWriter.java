package com.meawallet.sdkregistry.in.http.errors;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class HttpErrorResponseWriter {

    private final List<HttpMessageWriter<?>> messageWriters;

    public Mono<Void> write(Mono<ServerResponse> serverResponse, ServerWebExchange exchange) {
        return serverResponse.flatMap(response -> {
            setContentType(exchange);
            return response.writeTo(exchange, new ResponseContext());
        });
    }

    private void setContentType(ServerWebExchange exchange) {
        // force content-type since writeTo won't overwrite response header values
        exchange.getResponse()
                .getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);
    }

    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return Collections.emptyList();
        }
    }
}
