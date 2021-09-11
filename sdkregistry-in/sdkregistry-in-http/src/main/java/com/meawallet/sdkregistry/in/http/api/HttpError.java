package com.meawallet.sdkregistry.in.http.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpError {

    private String code;
    private String description;
    private List<ValidationError> fieldErrors;

    public HttpError(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
