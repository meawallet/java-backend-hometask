package com.meawallet.sdkregistry.in.http.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class HttpResponse {

    private Object data;
    private HttpError error;

    public HttpResponse(Object data) {
        this.data = data;
    }

    public HttpResponse(HttpError error) {
        this.error = error;
    }
}
