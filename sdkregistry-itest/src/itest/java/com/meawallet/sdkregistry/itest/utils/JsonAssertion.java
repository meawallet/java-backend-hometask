package com.meawallet.sdkregistry.itest.utils;

import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

public class JsonAssertion implements Consumer<EntityExchangeResult<byte[]>> {

    private final String expectedJson;

    private JsonAssertion(String expectedJson) {
        this.expectedJson = expectedJson;
    }

    public static JsonAssertion json(String json) {
        return new JsonAssertion(json);
    }

    @Override
    public void accept(EntityExchangeResult<byte[]> entityExchangeResult) {
        entityExchangeResult.assertWithDiagnostics(() -> {
            var json = Optional.ofNullable(entityExchangeResult.getResponseBody())
                               .map(body -> new String(body, StandardCharsets.UTF_8))
                               .orElse("");
            assertThatJson(json)
                    .isEqualTo(expectedJson);
        });
    }
}
