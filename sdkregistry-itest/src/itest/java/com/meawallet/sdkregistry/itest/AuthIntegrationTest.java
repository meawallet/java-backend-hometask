package com.meawallet.sdkregistry.itest;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT;

@DatabaseSetup(value = "classpath:dbunit/authentication/merchantKeys.xml")
@ExpectedDatabase(value = "classpath:dbunit/authentication/merchantKeys.xml", assertionMode = NON_STRICT)
@DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
public abstract class AuthIntegrationTest extends BaseIntegrationTest {

    private String token = "eyJraWQiOiJBMDVBQkU2OEY1MzlDMTQ2MUM3MjA4QjBCQzI2QTFERDZENTZEQkI4QTNDQzU4NTkxOEUzOTY3RUQ2NUJGM0Y2IiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJtZXJjaGFudEEiLCJleHAiOjE2MzcxODU1MTEsImlpZCI6Ijc3MUU3OTI5NTM1Q0EwREIwQTYzRjQ1MTk0Qjg0NUNEIiwiaWF0IjoxNjM0NTAzNTExfQ.mMUO97oQLRgwAMk8pX3fCZqC6Sn3Tjjsr4G-1yQPcJolJY7jUtQXvnuKA7iwOybhGAyXdvjpYZciA3HsFlbevPnKT_bz4G4mcRo7CMj_coc3sMHRtb2wg7ZF4m1pr3i_CHTgpXpG4m9OSPxLCxWnGNPUehWAQZKiCPpaZE0-xhap2F_Bu5j5b8-G3MXGwM9VNB3Q_CZckl2KnSx57n6EaopGptz7o0GOGpTHn1WFjnJaBtNozgImRwCWWMLUmWDKaJ6ZpdPtYGDs2EjDEluFNVRQ4sKJImNNSkRC0yQq_NdYeY4fwvTcWSmzOwzKzdFV2oJgNuraH40eUzOhYhojZg";

    @Override
    public WebTestClient webClient() {
        return super.webClient(headers -> {
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.setBearerAuth(token);
        });
    }
}
