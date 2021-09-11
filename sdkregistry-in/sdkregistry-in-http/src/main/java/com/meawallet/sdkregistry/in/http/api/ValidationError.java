package com.meawallet.sdkregistry.in.http.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationError implements Comparable<ValidationError> {

    @NonNull
    private String field;
    private String message;

    @Override
    public int compareTo(ValidationError o) {
        return field.compareTo(o.getField());
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }
}
