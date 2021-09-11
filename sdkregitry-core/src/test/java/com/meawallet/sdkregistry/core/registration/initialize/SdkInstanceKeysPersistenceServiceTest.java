package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import com.meawallet.sdkregistry.model.entities.SdkInstanceKeysEntity;
import com.meawallet.sdkregistry.model.repositories.SdkInstanceKeysRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.meawallet.sdkregistry.core.TestData.KEYSET_ID;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_ATTESTATION_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_ATTESTATION_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_PUSH_DATA_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_PUSH_DATA_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_PUSH_MESSAGE_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_SECRET_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_TRANSACTION_ENCRYPTION_KEY;
import static com.meawallet.sdkregistry.core.TestData.PLAIN_TRANSACTION_MAC_KEY;
import static com.meawallet.sdkregistry.core.TestData.SDK_INSTANCE_ID;
import static com.meawallet.sdkregistry.core.TestData.SECRETKEY_FINGERPRINT;
import static com.meawallet.sdkregistry.core.TestData.getTestSdk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willAnswer;

@MockitoSettings
class SdkInstanceKeysPersistenceServiceTest {

    @Mock
    SdkInstanceKeysRepository sdkInstanceKeysRepository;

    @InjectMocks
    SdkInstanceKeysPersistenceService victim;

    Sdk sdk = getTestSdk();

    @Test
    void shouldSaveNewKeys() {
        willAnswer(invocation -> Mono.just(invocation.getArgument(0)))
                .given(sdkInstanceKeysRepository).save(any(SdkInstanceKeysEntity.class));
        StepVerifier.create(victim.create(sdk))
                    .assertNext(entity -> {
                        assertThat(entity.getSdkInstanceId()).isSameAs(SDK_INSTANCE_ID);
                        assertThat(entity.getKeySetId()).isSameAs(KEYSET_ID);
                        assertThat(entity.getPushDataEncKey()).isSameAs(PLAIN_PUSH_DATA_ENCRYPTION_KEY);
                        assertThat(entity.getPushDataMacKey()).isSameAs(PLAIN_PUSH_DATA_MAC_KEY);
                        assertThat(entity.getPushMessageMacKey()).isSameAs(PLAIN_PUSH_MESSAGE_MAC_KEY);
                        assertThat(entity.getAttestationEncKey()).isSameAs(PLAIN_ATTESTATION_ENCRYPTION_KEY);
                        assertThat(entity.getAttestationMacKey()).isSameAs(PLAIN_ATTESTATION_MAC_KEY);
                        assertThat(entity.getTransactionEncKey()).isSameAs(PLAIN_TRANSACTION_ENCRYPTION_KEY);
                        assertThat(entity.getTransactionMacKey()).isSameAs(PLAIN_TRANSACTION_MAC_KEY);
                        assertThat(entity.getSecretKey()).isSameAs(PLAIN_SECRET_KEY);
                        assertThat(entity.getSecretKeyFingerprint()).isSameAs(SECRETKEY_FINGERPRINT);
                        assertThat(entity.getId()).isNull();
                        assertThat(entity.getCreatedAt()).isNull();
                    }).verifyComplete();
    }
}
