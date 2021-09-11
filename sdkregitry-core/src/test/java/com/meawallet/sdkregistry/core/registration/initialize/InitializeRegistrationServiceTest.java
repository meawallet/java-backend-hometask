package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.location.GpsLocation;
import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import com.meawallet.sdkregistry.api.register.InitializeRegistrationCommand;
import com.meawallet.sdkregistry.api.register.InitializeRegistrationResult;
import com.meawallet.sdkregistry.model.entities.SdkInstanceEntity;
import com.meawallet.sdkregistry.model.entities.SdkInstanceKeysEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.meawallet.sdkregistry.core.TestData.FCM_REGISTRATION_ID;
import static com.meawallet.sdkregistry.core.TestData.LATITUDE;
import static com.meawallet.sdkregistry.core.TestData.LONGITUDE;
import static com.meawallet.sdkregistry.core.TestData.PUBLIC_KEY;
import static com.meawallet.sdkregistry.core.TestData.SDK_ID;
import static com.meawallet.sdkregistry.core.TestData.SDK_INSTANCE_ID;
import static com.meawallet.sdkregistry.core.TestData.SHARED_KEY_FINGERPRINT;
import static com.meawallet.sdkregistry.core.TestData.getEncryptedRgk;
import static com.meawallet.sdkregistry.core.TestData.getPlainRgk;
import static com.meawallet.sdkregistry.core.TestData.getTestEncryptedKeyset;
import static com.meawallet.sdkregistry.core.TestData.getTestPlainKeyset;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@MockitoSettings
class InitializeRegistrationServiceTest {

    @Mock
    PlainSdkKeysetGenerationService plainSdkKeysetGenerationService;
    @Mock
    RgkDecryptionService rgkDecryptionService;
    @Mock
    PlainSdkKeysetEncryptionService plainSdkKeysetEncryptionService;
    @Mock
    SdkInstancePersistenceService sdkInstancePersistenceService;
    @Mock
    SdkInstanceKeysPersistenceService sdkInstanceKeysPersistenceService;
    @Mock
    RegistrationOutService attestationService;
    @Mock
    RegistrationOutService transactionService;

    InitializeRegistrationService victim;

    @BeforeEach
    void setUp() {
        victim = new InitializeRegistrationService(
                plainSdkKeysetGenerationService,
                rgkDecryptionService,
                plainSdkKeysetEncryptionService,
                sdkInstancePersistenceService,
                sdkInstanceKeysPersistenceService,
                List.of(attestationService, transactionService)
        );
    }

    @Test
    void shouldCreateSdkInstanceWithKeysAndRegisterInOutServices() {
        var plainKeyset = getTestPlainKeyset();
        var encryptedKeyset = getTestEncryptedKeyset();
        var encryptedRgk = getEncryptedRgk();
        var plainRgk = getPlainRgk();
        given(plainSdkKeysetGenerationService.generate())
                .willReturn(Mono.just(plainKeyset));
        given(rgkDecryptionService.decrypt(encryptedRgk, SHARED_KEY_FINGERPRINT))
                .willReturn(Mono.just(plainRgk));
        given(plainSdkKeysetEncryptionService.encrypt(plainKeyset, plainRgk))
                .willReturn(Mono.just(encryptedKeyset));
        var expectedSdk = new Sdk(
                SDK_ID,
                SDK_INSTANCE_ID,
                PUBLIC_KEY,
                FCM_REGISTRATION_ID,
                new GpsLocation(LATITUDE, LONGITUDE),
                plainKeyset,
                encryptedKeyset
        );
        given(sdkInstancePersistenceService.create(eq(expectedSdk)))
                .willReturn(Mono.just(new SdkInstanceEntity()));
        given(sdkInstanceKeysPersistenceService.create(eq(expectedSdk)))
                .willReturn(Mono.just(new SdkInstanceKeysEntity()));
        given(attestationService.register(eq(expectedSdk)))
                .willReturn(Mono.empty());
        given(transactionService.register(eq(expectedSdk)))
                .willReturn(Mono.empty());

        var command = new InitializeRegistrationCommand(
                SDK_ID,
                SDK_INSTANCE_ID,
                PUBLIC_KEY,
                SHARED_KEY_FINGERPRINT,
                FCM_REGISTRATION_ID,
                encryptedRgk,
                new GpsLocation(LATITUDE, LONGITUDE)
        );
        StepVerifier.create(victim.initialize(command))
                    .expectNext(new InitializeRegistrationResult(encryptedKeyset))
                    .verifyComplete();

        then(plainSdkKeysetGenerationService).should(times(1)).generate();
        then(rgkDecryptionService).should(times(1)).decrypt(any(), any());
        then(plainSdkKeysetEncryptionService).should(times(1)).encrypt(any(), any());
        then(sdkInstancePersistenceService).should(times(1)).create(any());
        then(sdkInstanceKeysPersistenceService).should(times(1)).create(any());
        then(attestationService).should(times(1)).register(any());
        then(transactionService).should(times(1)).register(any());
    }
}
