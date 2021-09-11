package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import com.meawallet.sdkregistry.model.entities.SdkInstanceEntity;
import com.meawallet.sdkregistry.model.repositories.SdkInstanceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.meawallet.sdkregistry.core.TestData.FCM_REGISTRATION_ID;
import static com.meawallet.sdkregistry.core.TestData.LATITUDE;
import static com.meawallet.sdkregistry.core.TestData.LONGITUDE;
import static com.meawallet.sdkregistry.core.TestData.SDK_ID;
import static com.meawallet.sdkregistry.core.TestData.SDK_INSTANCE_ID;
import static com.meawallet.sdkregistry.core.TestData.getTestSdk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;

@MockitoSettings
class SdkInstancePersistenceServiceTest {

    @Mock
    SdkInstanceRepository repository;

    @InjectMocks
    SdkInstancePersistenceService victim;

    Sdk sdk = getTestSdk();

    @Test
    void shouldFailWhenSdkInstanceAlreadyExists() {
        given(repository.existsBySdkInstanceId(SDK_INSTANCE_ID))
                .willReturn(Mono.just(true));
        StepVerifier.create(victim.create(sdk))
                    .expectError()
                    .verify();
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldSaveNewSdkInstance() {
        given(repository.existsBySdkInstanceId(SDK_INSTANCE_ID))
                .willReturn(Mono.just(false));
        willAnswer(invocation -> Mono.just(invocation.getArgument(0)))
                .given(repository).save(any(SdkInstanceEntity.class));
        StepVerifier.create(victim.create(sdk))
                    .assertNext(entity -> {
                        assertThat(entity.getSdkId()).isSameAs(SDK_ID);
                        assertThat(entity.getSdkInstanceId()).isSameAs(SDK_INSTANCE_ID);
                        assertThat(entity.getLatitude()).isSameAs(LATITUDE);
                        assertThat(entity.getLongitude()).isSameAs(LONGITUDE);
                        assertThat(entity.getFcmRegistrationId()).isSameAs(FCM_REGISTRATION_ID);
                        assertThat(entity.getId()).isNull();
                        assertThat(entity.getCreatedAt()).isNull();
                    }).verifyComplete();
    }
}
