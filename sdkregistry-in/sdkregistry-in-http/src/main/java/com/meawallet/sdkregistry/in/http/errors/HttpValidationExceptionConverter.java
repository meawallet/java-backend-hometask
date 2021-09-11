package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode;
import com.meawallet.sdkregistry.in.http.api.HttpError;
import com.meawallet.sdkregistry.in.http.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Component
public class HttpValidationExceptionConverter implements HttpExceptionConverter {

    @Override
    public Tuple2<HttpStatus, HttpError> convert(Throwable ex) {
        return Tuples.of(
                HttpStatus.BAD_REQUEST,
                new HttpError(
                        SdkRegistryErrorCode.PAYLOAD_VALIDATION_FAILED.name(),
                        "Validation failed",
                        ((ValidationException) ex).getErrors()
                ));
    }

    @Override
    public Class<? extends Throwable> supports() {
        return ValidationException.class;
    }
}
