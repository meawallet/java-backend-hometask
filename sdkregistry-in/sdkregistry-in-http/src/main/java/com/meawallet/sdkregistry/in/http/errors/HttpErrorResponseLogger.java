package com.meawallet.sdkregistry.in.http.errors;

import com.meawallet.sdkregistry.in.http.api.HttpError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HttpErrorResponseLogger {

    public void log(Throwable ex, HttpStatus httpStatus, HttpError error) {
        if (httpStatus.is5xxServerError()) {
            log5xx(ex, httpStatus, error);
        } else {
            logXxx(ex, httpStatus, error);
        }
    }

    private void logXxx(Throwable ex, HttpStatus httpStatus, HttpError error) {
        log.debug("exception message : {}, response status : {}, response error : {}", ex.getMessage(), httpStatus, error);
    }

    private void log5xx(Throwable ex, HttpStatus httpStatus, HttpError error) {
        log.error("internal error response status : {}, response error : {}, caused by", httpStatus, error, ex);
    }

}
