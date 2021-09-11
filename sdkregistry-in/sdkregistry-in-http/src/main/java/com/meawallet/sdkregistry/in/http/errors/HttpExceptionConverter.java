package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.in.http.api.HttpError;
import org.springframework.http.HttpStatus;
import reactor.util.function.Tuple2;

public interface HttpExceptionConverter {

    Tuple2<HttpStatus, HttpError> convert(Throwable ex);

    Class<? extends Throwable> supports();

}
