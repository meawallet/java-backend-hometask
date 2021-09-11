package com.meawallet.sdkregistry.in.http.validation;

import com.meawallet.sdkregistry.in.http.api.ValidationError;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class ReactiveValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T object) {
        return Mono.fromSupplier(() -> validateSync(object));
    }

    private <T> T validateSync(T object) {
        var bindingResult = new DirectFieldBindingResult(object, "object");
        validator.validate(object, bindingResult);
        if (bindingResult.hasErrors()) {
            var errors = convert(bindingResult.getFieldErrors());
            throw new ValidationException(errors);
        }
        return object;
    }

    private List<ValidationError> convert(Collection<FieldError> fieldErrors) {
        return fieldErrors
                .stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .sorted()
                .collect(toList());
    }
}
