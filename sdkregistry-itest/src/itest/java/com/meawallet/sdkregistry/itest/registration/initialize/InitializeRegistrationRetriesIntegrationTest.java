package com.meawallet.sdkregistry.itest.registration.initialize;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.meawallet.sdkregistry.core.cryptography.AesEncryptionService;
import com.meawallet.sdkregistry.core.cryptography.AesKeyGenerator;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import com.meawallet.sdkregistry.core.registration.initialize.SdkKey;
import com.meawallet.sdkregistry.itest.BaseIntegrationTest;
import com.meawallet.sdkregistry.itest.mocks.AttestationServiceMock;
import com.meawallet.sdkregistry.itest.mocks.CloudMessagingServiceMock;
import com.meawallet.sdkregistry.itest.mocks.TransactionServiceMock;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.Duration;

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
import static com.meawallet.sdkregistry.itest.utils.JsonAssertion.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class InitializeRegistrationRetriesIntegrationTest extends BaseIntegrationTest {

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
    void shouldSuccessfullyPassFrom1stAttempt() {
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
    /**
     * todo : remove this test once retires are implemented.
     * Validation will be different within retries flow.
     * The correct test for retries will be : InitializeRegistrationRetriesIntegrationTest # shouldFailWhenRegistrationWasCompleted
     * */
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

    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void verifyParallelExecution() {
        mockKeyGenerationOk();
        given(aesEncryptionService.encryptEcb(any(), any())).willAnswer(invocation -> {
            return ((Mono) invocation.callRealMethod()).delayElement(Duration.ofMillis(400));
        });
        attestationServiceMock.stub(
                readFile("stubs/attestation/attestationRegistrationRequestMatcher.json"),
                readFile("stubs/attestation/attestationRegistrationOkResponseDelayed.json")
        );
        transactionServiceMock.stub(
                readFile("stubs/transaction/transactionRegistrationRequestMatcher.json"),
                readFile("stubs/transaction/transactionRegistrationOkResponseDelayed.json")
        );
        cloudMessagingServiceMock.stub(
                readFile("stubs/cloudmessaging/cloudMessagingRegistrationRequestMatcher.json"),
                readFile("stubs/cloudmessaging/cloudMessagingRegistrationOkResponseDelayed.json")
        );
        webClient(Duration.ofSeconds(6))
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest1stAttempt.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponse1stAttemptOk.json"));
    }

    /**
     * Complete Registration activates SDKInstanceKeysEntity
     * If the Mobile SDK has successfully completed registration, no more initialization is allowed.
     */
    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/completeRegistrationCompleted.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/completeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldFailWhenRegistrationWasCompleted() {
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
        then(aesKeyGenerator).shouldHaveNoInteractions();
        then(aesEncryptionService).shouldHaveNoInteractions();

    }

    /**
     * On every attempt Mobile SDK generates new RGK,
     * step "Encrypt generated keys" cannot be skipped.
     * 1st attempt :
     * - Key generation - OK
     * - Attestation Service - OK
     * - Cloud Messaging Service - OK
     * - Transaction Service - OK
     * - Key encryption - OK
     * Result : OK
     * <p>
     * 2nd attempt :
     * - Key generation - SKIPPED
     * - Attestation Service - SKIPPED
     * - Cloud Messaging Service - SKIPPED
     * - Transaction Service - SKIPPED
     * - Key encryption - OK
     * Result : OK
     */
    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldEncryptKeysAndSkipAllOtherStepsWhen1stAttemptWasSuccessful() {
        // successful initialize on 1st attempt
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

        resetMocks();

        // should skip all steps on return result on 2nd attempt
        mockKeyGenerationOk();
        mockTransactionServiceOk();
        mockAttestationServiceOk();
        mockCloudMessagingServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest2ndAttempt.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponse2ndAttemptOk.json"));
        attestationServiceMock.hasInteractions(0);
        cloudMessagingServiceMock.hasInteractions(0);
        transactionServiceMock.hasInteractions(0);
        then(aesKeyGenerator).shouldHaveNoInteractions();
    }

    /**
     * 1st attempt :
     * - Key generation - OK
     * - Attestation Service - OK
     * - Cloud Messaging Service - OK
     * - Transaction Service - FAILURE
     * - Key encryption - OK
     * Result : INTERNAL_RETRYABLE
     * <p>
     * 2nd attempt :
     * - Key generation - SKIPPED
     * - Attestation Service - SKIPPED
     * - Cloud Messaging Service - SKIPPED
     * - Transaction Service - OK
     * - Key encryption - OK
     * Result : OK
     */
    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldRetryAfterFailedTransactionRegistration() {

        mockTransactionServiceFailure();

        mockKeyGenerationOk();
        mockCloudMessagingServiceOk();
        mockAttestationServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest1stAttempt.json"))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .consumeWith(json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponseStepFailed.json")));
        attestationServiceMock.hasInteractions(1);
        cloudMessagingServiceMock.hasInteractions(1);
        transactionServiceMock.hasInteractions(1);

        resetMocks();

        // should skip all steps on return result on 2nd attempt
        mockTransactionServiceOk();
        mockKeyGenerationOk();
        mockCloudMessagingServiceOk();
        mockAttestationServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest2ndAttempt.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponse2ndAttemptOk.json"));
        then(aesKeyGenerator).shouldHaveNoInteractions();
        attestationServiceMock.hasInteractions(0);
        cloudMessagingServiceMock.hasInteractions(0);
        transactionServiceMock.hasInteractions(1);
    }

    /**
     * 1st attempt :
     * - Key generation - OK
     * - Attestation Service - FAILURE
     * - Cloud Messaging Service - OK
     * - Transaction Service - OK
     * - Key encryption - OK
     * Result : INTERNAL_RETRYABLE
     * <p>
     * 2nd attempt :
     * - Key generation - SKIPPED
     * - Attestation Service - OK
     * - Cloud Messaging Service - SKIPPED
     * - Transaction Service - SKIPPED
     * - Key encryption - OK
     * Result : OK
     */
    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldRetryAfterFailedAttestationRegistration() {

        mockAttestationServiceFailure();

        mockKeyGenerationOk();
        mockCloudMessagingServiceOk();
        mockTransactionServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest1stAttempt.json"))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .consumeWith(json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponseStepFailed.json")));
        attestationServiceMock.hasInteractions(1);
        cloudMessagingServiceMock.hasInteractions(1);
        transactionServiceMock.hasInteractions(1);

        resetMocks();

        // should skip all steps on return result on 2nd attempt
        mockTransactionServiceOk();
        mockKeyGenerationOk();
        mockCloudMessagingServiceOk();
        mockAttestationServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest2ndAttempt.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponse2ndAttemptOk.json"));
        then(aesKeyGenerator).shouldHaveNoInteractions();
        attestationServiceMock.hasInteractions(1);
        cloudMessagingServiceMock.hasInteractions(0);
        transactionServiceMock.hasInteractions(0);
    }

    /**
     * 1st attempt :
     * - Key generation - OK
     * - Attestation Service - OK
     * - Cloud Messaging Service - FAILURE
     * - Transaction Service - OK
     * - Key encryption - OK
     * Result : INTERNAL_RETRYABLE
     * <p>
     * 2nd attempt :
     * - Key generation - SKIPPED
     * - Attestation Service - SKIPPED
     * - Cloud Messaging Service - OK
     * - Transaction Service - SKIPPED
     * - Key encryption - OK
     * Result : OK
     */
    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldRetryAfterFailedCloudMessagingRegistration() {

        mockCloudMessagingServiceFailure();

        mockKeyGenerationOk();
        mockTransactionServiceOk();
        mockAttestationServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest1stAttempt.json"))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .consumeWith(json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponseStepFailed.json")));
        attestationServiceMock.hasInteractions(1);
        cloudMessagingServiceMock.hasInteractions(1);
        transactionServiceMock.hasInteractions(1);

        resetMocks();

        // should skip all steps on return result on 2nd attempt
        mockTransactionServiceOk();
        mockKeyGenerationOk();
        mockCloudMessagingServiceOk();
        mockAttestationServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest2ndAttempt.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponse2ndAttemptOk.json"));
        then(aesKeyGenerator).shouldHaveNoInteractions();
        attestationServiceMock.hasInteractions(0);
        cloudMessagingServiceMock.hasInteractions(1);
        transactionServiceMock.hasInteractions(0);
    }

    /**
     * 1st attempt :
     * - Key generation - OK
     * - Attestation Service - OK
     * - Cloud Messaging Service - FAILURE
     * - Transaction Service - FAILURE
     * - Key encryption - OK
     * Result : INTERNAL_RETRYABLE
     * <p>
     * 2nd attempt :
     * - Key generation - SKIPPED
     * - Attestation Service - SKIPPED
     * - Cloud Messaging Service - OK
     * - Transaction Service - OK
     * - Key encryption - OK
     * Result : OK
     */
    @Test
    @DatabaseSetup(value = "classpath:dbunit/registration/noRegistrations.xml")
    @ExpectedDatabase(value = "classpath:dbunit/registration/initializeRegistrationCompleted.xml", assertionMode = NON_STRICT)
    @DatabaseTearDown(value = "classpath:dbunit/emptyDb.xml", type = DELETE_ALL)
    void shouldRetryAfterFailedMultipleRegistrations() {

        mockCloudMessagingServiceFailure();
        mockTransactionServiceFailure();

        mockKeyGenerationOk();
        mockAttestationServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest1stAttempt.json"))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .consumeWith(json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponseStepFailed.json")));
        attestationServiceMock.hasInteractions(1);
        cloudMessagingServiceMock.hasInteractions(1);
        transactionServiceMock.hasInteractions(1);

        resetMocks();

        // should skip all steps on return result on 2nd attempt
        mockTransactionServiceOk();
        mockKeyGenerationOk();
        mockCloudMessagingServiceOk();
        mockAttestationServiceOk();
        webClient()
                .post()
                .uri("/sdkregistry/v1/registration/initialize")
                .bodyValue(readFile("api/jsons/registration/initializeretries/initializeRegistrationRequest2ndAttempt.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(readFile("api/jsons/registration/initializeretries/initializeRegistrationResponse2ndAttemptOk.json"));
        then(aesKeyGenerator).shouldHaveNoInteractions();
        attestationServiceMock.hasInteractions(0);
        cloudMessagingServiceMock.hasInteractions(1);
        transactionServiceMock.hasInteractions(1);
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
        ;
        cloudMessagingServiceMock.reset();
        transactionServiceMock.reset();
        ;
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
