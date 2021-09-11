package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode;
import com.meawallet.sdkregistry.api.errors.SdkRegistryException;
import com.meawallet.sdkregistry.in.http.api.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Component
public class SdkRegistryExceptionConverter implements HttpExceptionConverter {

    @Override
    public Tuple2<HttpStatus, HttpError> convert(Throwable ex) {
        var error = ((SdkRegistryException) ex).getError();
        return Tuples.of(
                convertStatus(error.getCode().getCategory()),
                new HttpError(
                        error.getCode().name(),
                        error.getDescription()
                ));
    }

    @Override
    public Class<? extends Throwable> supports() {
        return SdkRegistryException.class;
    }

    private HttpStatus convertStatus(SdkRegistryErrorCode.Category category) {
        switch (category) {
            case VALIDATION:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
