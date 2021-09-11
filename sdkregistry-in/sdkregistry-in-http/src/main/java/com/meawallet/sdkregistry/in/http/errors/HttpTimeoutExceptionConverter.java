package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode;
import com.meawallet.sdkregistry.in.http.api.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.concurrent.TimeoutException;

@Component
public class HttpTimeoutExceptionConverter implements HttpExceptionConverter {

    @Override
    public Tuple2<HttpStatus, HttpError> convert(Throwable ex) {
        return Tuples.of(
                HttpStatus.GATEWAY_TIMEOUT,
                new HttpError(
                        SdkRegistryErrorCode.GATEWAY_TIMEOUT.name(),
                        "Internal timeout"
                ));
    }

    @Override
    public Class<? extends Throwable> supports() {
        return TimeoutException.class;
    }
}
