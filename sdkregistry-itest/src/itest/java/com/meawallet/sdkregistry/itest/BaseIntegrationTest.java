package com.meawallet.sdkregistry.itest;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.meawallet.sdkregistry.itest.config.MocksConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.metrics.AutoConfigureMetrics;
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
import java.util.function.Consumer;

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
@AutoConfigureMetrics
public abstract class BaseIntegrationTest {

    Consumer<HttpHeaders> tokenHeader = http -> http.setBearerAuth("eyJraWQiOiJBMDVBQkU2OEY1MzlDMTQ2MUM3MjA4QjBCQzI2QTFERDZENTZEQkI4QTNDQzU4NTkxOEUzOTY3RUQ2NUJGM0Y2IiwiYWxnIjoiUlMyNTYifQ\n"
        + ".eyJpc3MiOiJtZWFsYXVuY2giLCJpaWQiOiI3NzFFNzkyOTUzNUNBMERCMEE2M0Y0NTE5NEI4NDVDRCIsImlhdCI6MTYzMzU5ODIxNywiZXhwIjoxNjM2Mjc2NjE3fQ.lK2ZfIOIgJIkDXA-pmgfzsvVG2NurJlpiJ9C56PiO1A4xOW84ZwdwiivVFKw1vAPqYQUsWhfIxgVPM9QZp10wgnqM0ah3-ItDqcXR9yxRqRpYD669viJMnqF8rCpJZ6RIRqWvt1e1asTx8r0QLCAImzdZi9L7K3vBdKoBeIcHx_hiKg01ms1y0NFSGfv-bSYfVMrm9b54kDtJi7T-dCps2Eet8B5acSh6KLGmUR0I3DP6_IZ0RH9eQZ0ZSX7gcLdK_chTko2fXoJ7G6itI8RgFb46XbH20X4gKsR3IEG6sAVzAeEc_HQ3RXHFTH9namRDRXgXLPkh8mExUE5o_GRG4y_-HxlzOWWGfQ5K5j44KG8GyMG9g4bviYtL4IuIZKZtPbkJA_bQsb-Jl53U6nbg4wF3Qgx6f9S29iGTOIv_kNSmB9zABLrBObklbsQ7IrjqLowndmxQqxkv9wY-Gw0SWLMWlmSf_sbq9Pos9UaGKMHfuJX8cKe4IxoDkErE-3LDjEd4yzYvizsx1vOtoqPQebh8nzEzgocpmXrTLQdhmG34jKLMaPeGUw_JHIvLXey70FBS7InocHZikl2bt_vJQ1HQB8yknzORFlBusVKMa_OfNCdRtOd6rrEOZUN0x6NqkDlV_8JHHeYh_nvtGHAVAaOxhCsxVwrOldWP5xgfX0");

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
                            .defaultHeaders(tokenHeader)
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
