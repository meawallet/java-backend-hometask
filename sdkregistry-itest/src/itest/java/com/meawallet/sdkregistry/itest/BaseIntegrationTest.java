package com.meawallet.sdkregistry.itest;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.meawallet.sdkregistry.itest.config.MocksConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = {
        TestApplication.class,
        MocksConfig.class
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)

@ActiveProfiles("itest")
public abstract class BaseIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @LocalServerPort
    private Integer serverPort;

    public WebTestClient webClient() {
        return webClient(Duration.ofSeconds(10));
    }

    public WebTestClient webClient(Duration timeout) {
        return WebTestClient.bindToApplicationContext(applicationContext)
                            .configureClient()
                            .responseTimeout(timeout)
                            .baseUrl("http://localhost:" + serverPort)
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();
    }

    public String readFile(String fileName) {
        try {
            var is = BaseIntegrationTest.class.getClassLoader().getResourceAsStream(fileName);
            assertNotNull(String.format("Test resource file '%s' not found.", fileName), is);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
