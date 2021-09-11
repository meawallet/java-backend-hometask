package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode;
import com.meawallet.sdkregistry.in.http.api.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Component
public class HttpResponseStatusExceptionConverter implements HttpExceptionConverter {

    @Override
    public Tuple2<HttpStatus, HttpError> convert(Throwable ex) {
        var exception = (ResponseStatusException) ex;
        return Tuples.of(
                exception.getStatus(),
                new HttpError(
                        convertStatusToCode(exception.getStatus()),
                        exception.getMessage()
                )
        );
    }

    @Override
    public Class<? extends Throwable> supports() {
        return ResponseStatusException.class;
    }

    private String convertStatusToCode(HttpStatus httpStatus) {
        if (httpStatus.is4xxClientError()) {
            return SdkRegistryErrorCode.UNPROCESSABLE.name();
        }
        return SdkRegistryErrorCode.INTERNAL.name();
    }
}
