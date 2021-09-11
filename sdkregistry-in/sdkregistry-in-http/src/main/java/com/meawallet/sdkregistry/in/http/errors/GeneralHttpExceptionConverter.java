package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.api.errors.SdkRegistryErrorCode;
import com.meawallet.sdkregistry.in.http.api.HttpError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GeneralHttpExceptionConverter {

    private final Map<Class<? extends Throwable>, HttpExceptionConverter> converters;
    private final ThrowableHttpExceptionConverter throwableExceptionConverter;

    public GeneralHttpExceptionConverter(Collection<HttpExceptionConverter> converters) {
        this.converters = converters.stream()
                                    .collect(Collectors.toUnmodifiableMap(HttpExceptionConverter::supports, Function.identity()));
        this.throwableExceptionConverter = new ThrowableHttpExceptionConverter();
    }

    public Tuple2<HttpStatus, HttpError> convert(Throwable ex) {
        return findConverter(ex).convert(ex);
    }

    private HttpExceptionConverter findConverter(Throwable ex) {
        Class<?> clazz = ex.getClass();
        while (clazz != Throwable.class) {
            var converter = converters.get(clazz);
            if (converter != null) {
                return converter;
            }
            clazz = clazz.getSuperclass();
        }
        return throwableExceptionConverter;
    }

    private static class ThrowableHttpExceptionConverter implements HttpExceptionConverter {

        @Override
        public Tuple2<HttpStatus, HttpError> convert(Throwable ex) {
            return Tuples.of(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new HttpError(
                            SdkRegistryErrorCode.INTERNAL.name(),
                            "Unexpected internal error"
                    ));
        }

        @Override
        public Class<? extends Throwable> supports() {
            return Throwable.class;
        }
    }
}
