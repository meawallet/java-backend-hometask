package com.meawallet.sdkregistry.itest.registration.complete;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.meawallet.sdkregistry.itest.AuthIntegrationTest;
import org.junit.jupiter.api.Test;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT;

class CompleteRegistrationIntegrationTest extends AuthIntegrationTest {

    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/completeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldActivateKeysOnSuccess() {
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/complete")
                .bodyValue(readFile("api/jsons/registration/complete/completeRegistrationRequestOk.json"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/authentication/merchantKeys.xml")
    @ExpectedDatabase(value = "classpath:dbunit/authentication/merchantKeys.xml", assertionMode = NON_STRICT)
    void shouldFailWhenKeysetNotExists() {
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/complete")
                .bodyValue(readFile("api/jsons/registration/complete/completeRegistrationRequestWrongKeyset.json"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json(readFile("api/jsons/registration/complete/completeRegistrationResponseWrongKeyset.json"));
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldFailWhenSecretValueIsNotCorrect() {
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/complete")
                .bodyValue(readFile("api/jsons/registration/complete/completeRegistrationRequestWrongSecret.json"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json(readFile("api/jsons/registration/complete/completeRegistrationResponseWrongSecret.json"));
    }
}
