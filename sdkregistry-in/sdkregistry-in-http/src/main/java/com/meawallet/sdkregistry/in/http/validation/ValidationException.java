package com.meawallet.sdkregistry.in.http.validation;

import com.meawallet.sdkregistry.in.http.api.ValidationError;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
