package com.meawallet.sdkregistry.itest.registration.initialize;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.core.cryptography.AesKeyGenerator;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import com.meawallet.sdkregistry.core.registration.initialize.SdkKey;
import com.meawallet.sdkregistry.itest.AuthIntegrationTest;
import com.meawallet.sdkregistry.itest.mocks.AttestationServiceMock;
import com.meawallet.sdkregistry.itest.mocks.CloudMessagingServiceMock;
import com.meawallet.sdkregistry.itest.mocks.TransactionServiceMock;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE_ALL;
import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.KEYSET_ID;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_ATTESTATION_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_ATTESTATION_MAC_KEY;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_PUSH_DATA_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_PUSH_DATA_MAC_KEY;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_PUSH_MESSAGE_MAC_KEY;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_SECRET_KEY;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_TRANSACTION_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.itest.utils.IntegrationTestData.PLAIN_TRANSACTION_MAC_KEY;
import static org.mockito.BDDMockito.given;

class InitializeRegistrationIntegrationTest extends AuthIntegrationTest {

    @Autowired
    AesKeyGenerator aesKeyGenerator;
    @Autowired
    RandomGenerator randomGenerator;
    @Autowired
    AesEncryptionService aesEncryptionService;
    @Autowired
    AttestationServiceMock attestationServiceMock;
    @Autowired
    CloudMessagingServiceMock cloudMessagingServiceMock;
    @Autowired
    TransactionServiceMock transactionServiceMock;

    @BeforeEach
    void setUp() {
        resetMocks();
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldSuccessfullyInitializeRegistration() {
        mockKeyGenerationOk();
        mockTransactionServiceOk();
        mockAttestationServiceOk();
        mockCloudMessagingServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest1stAttempt.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponse1stAttemptOk.json"));
        attestationServiceMock.hasInteractions(1);
        cloudMessagingServiceMock.hasInteractions(1);
        transactionServiceMock.hasInteractions(1);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/noRegistrations.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldFailWhenSecretKeyNotFound() {
        mockKeyGenerationOk();
        mockTransactionServiceOk();
        mockAttestationServiceOk();
        mockCloudMessagingServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequestWrongSecretKey.json"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponseWrongSecretKey.json"));
        attestationServiceMock.hasInteractions(0);
        cloudMessagingServiceMock.hasInteractions(0);
        transactionServiceMock.hasInteractions(0);
    }

    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldFailWhenSdkAlreadyRegistered() {
        mockKeyGenerationOk();
        mockTransactionServiceOk();
        mockAttestationServiceOk();
        mockCloudMessagingServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest1stAttempt.json"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponseAlreadyExists.json"));
        attestationServiceMock.hasInteractions(0);
        cloudMessagingServiceMock.hasInteractions(0);
        transactionServiceMock.hasInteractions(0);
    }

    private void mockTransactionServiceOk() {
        transactionServiceMock.stub(
                readFile("stubs/transaction/transactionRegistrationRequestMatcher.json"),
                readFile("stubs/transaction/transactionRegistrationOkResponse.json")
        );
    }

    private void mockTransactionServiceFailure() {
        transactionServiceMock.stub(
                readFile("stubs/transaction/transactionRegistrationRequestMatcher.json"),
                readFile("stubs/transaction/transactionRegistration500Response.json")
        );
    }

    private void mockAttestationServiceOk() {
        attestationServiceMock.stub(
                readFile("stubs/attestation/attestationRegistrationRequestMatcher.json"),
                readFile("stubs/attestation/attestationRegistrationOkResponse.json")
        );
    }

    private void mockAttestationServiceFailure() {
        attestationServiceMock.stub(
                readFile("stubs/attestation/attestationRegistrationRequestMatcher.json"),
                readFile("stubs/attestation/attestationRegistration500Response.json")
        );
    }

    private void mockCloudMessagingServiceOk() {
        cloudMessagingServiceMock.stub(
                readFile("stubs/cloudmessaging/cloudMessagingRegistrationRequestMatcher.json"),
                readFile("stubs/cloudmessaging/cloudMessagingRegistrationOkResponse.json")
        );
    }

    private void mockCloudMessagingServiceFailure() {
        cloudMessagingServiceMock.stub(
                readFile("stubs/cloudmessaging/cloudMessagingRegistrationRequestMatcher.json"),
                readFile("stubs/cloudmessaging/cloudMessagingRegistration500Response.json")
        );
    }

    private void resetMocks() {
        Mockito.reset(aesKeyGenerator, randomGenerator, aesEncryptionService);
        attestationServiceMock.reset();
        cloudMessagingServiceMock.reset();
        transactionServiceMock.reset();
    }

    private void mockKeyGenerationOk() {
        given(randomGenerator.generate(16))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(KEYSET_ID)));
        given(aesKeyGenerator.generate(SdkKey.PUSH_DATA_ENCRYPTION_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_PUSH_DATA_ENCRYPTION_KEY)));
        given(aesKeyGenerator.generate(SdkKey.PUSH_DATA_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_PUSH_DATA_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.PUSH_MESSAGE_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_PUSH_MESSAGE_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.ATTESTATION_ENCRYPTION_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_ATTESTATION_ENCRYPTION_KEY)));
        given(aesKeyGenerator.generate(SdkKey.ATTESTATION_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_ATTESTATION_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.TRANSACTION_ENCRYPTION_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_TRANSACTION_ENCRYPTION_KEY)));
        given(aesKeyGenerator.generate(SdkKey.TRANSACTION_MAC_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_TRANSACTION_MAC_KEY)));
        given(aesKeyGenerator.generate(SdkKey.SECRET_KEY.getParams()))
                .willReturn(Mono.fromCallable(() -> Hex.decodeHex(PLAIN_SECRET_KEY)));
    }
}
