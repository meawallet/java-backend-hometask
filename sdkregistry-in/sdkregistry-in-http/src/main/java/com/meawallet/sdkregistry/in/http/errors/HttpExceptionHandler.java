package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.in.http.api.HttpError;
import com.meawallet.sdkregistry.in.http.api.HttpResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
public class HttpExceptionHandler implements ErrorWebExceptionHandler {

    private final GeneralHttpExceptionConverter exceptionConverter;
    private final HttpErrorResponseLogger responseLogger;
    private final HttpErrorResponseWriter responseWriter;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.fromSupplier(() -> parseException(ex))
                   .doOnNext(statusWithBody -> log(statusWithBody, ex))
                   .flatMap(statusWithBody -> ServerResponse.status(statusWithBody.getT1())
                                                            .bodyValue(new HttpResponse(statusWithBody.getT2())))
                   .transform(serverResponse -> responseWriter.write(serverResponse, exchange));
    }

    private Tuple2<HttpStatus, HttpError> parseException(Throwable ex) {
        return exceptionConverter.convert(ex);
    }

    private void log(Tuple2<HttpStatus, HttpError> statusWithBody, Throwable ex) {
        responseLogger.log(ex, statusWithBody.getT1(), statusWithBody.getT2());
    }
}
