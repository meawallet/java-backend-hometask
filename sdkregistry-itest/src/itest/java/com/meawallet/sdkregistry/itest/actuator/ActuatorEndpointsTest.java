package com.meawallet.sdkregistry.itest.actuator;

import com.meawallet.sdkregistry.itest.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ActuatorEndpointsTest extends BaseIntegrationTest {

    @Test
    void verifyHealthAvailable() {
        testActuatorEndpoint("/health");
    }

    @Test
    void verifyMetricsAvailable() {
        testActuatorEndpoint("/metrics");
    }

    @Test
    void verifyPrometheusAvailable() {
        testActuatorEndpoint("/prometheus");
    }

    private void testActuatorEndpoint(String uri) {
        webClient().get()
                   .uri(uri)
                   .exchange()
                   .expectStatus().isOk()
                   .expectBody().consumeWith(body -> log.info("{} : {}", uri, body));
    }
}
