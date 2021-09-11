package com.meawallet.sdkregistry.out.cloudmessaging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.meawallet.sdkregistry.out.cloudmessaging.CloudMessagingTestData.getTestSdk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@MockitoSettings
class CloudMessagingRegistrationOutServiceTest {

    @Mock
    ExchangeFunction exchangeFunction;

    @Mock
    ClientResponse clientResponse;

    @Mock
    ClientResponse.Headers headers;

    @Captor
    ArgumentCaptor<ClientRequest> requestArgumentCaptor;

    String registrationUrl = "www.meawallet.com/cloudmessaging/registration";
    Duration timeout = Duration.ofSeconds(2);
    CloudMessagingServiceProperties props = new CloudMessagingServiceProperties(registrationUrl, timeout);

    CloudMessagingRegistrationOutService victim;

    @BeforeEach
    void setUp() {
        var webClient = WebClient.builder()
                                 .exchangeFunction(exchangeFunction)
                                 .build();
        victim = new CloudMessagingRegistrationOutService(props, webClient);
    }

    @Test
    void shouldSendRegistration() {
        given(exchangeFunction.exchange(any(ClientRequest.class)))
                .willReturn(Mono.just(clientResponse));
        given(clientResponse.rawStatusCode())
                .willReturn(204);
        given(clientResponse.releaseBody()).willReturn(Mono.empty());
        given(headers.asHttpHeaders()).willReturn(new HttpHeaders());
        given(clientResponse.headers()).willReturn(headers);
        var sdk = getTestSdk();
        StepVerifier.create(victim.register(sdk))
                    .verifyComplete();
        then(exchangeFunction).should().exchange(requestArgumentCaptor.capture());
        var request = requestArgumentCaptor.getValue();
        assertThat(request.url().toString()).isEqualTo(registrationUrl);
    }

    @Test
    void shouldFailAny4xxResponse() {
        given(exchangeFunction.exchange(any(ClientRequest.class)))
                .willReturn(Mono.just(clientResponse));
        given(clientResponse.rawStatusCode())
                .willReturn(400);
        given(clientResponse.releaseBody()).willReturn(Mono.empty());
        var sdk = getTestSdk();
        StepVerifier.create(victim.register(sdk))
                    .expectErrorSatisfies(t -> assertThat(t).hasMessageContaining("Registration failed in CloudMessaging Service"))
                    .verify();
    }

    @Test
    void shouldFailAny5xxResponse() {
        given(exchangeFunction.exchange(any(ClientRequest.class)))
                .willReturn(Mono.just(clientResponse));
        given(clientResponse.rawStatusCode())
                .willReturn(500);
        given(clientResponse.releaseBody()).willReturn(Mono.empty());
        var sdk = getTestSdk();
        StepVerifier.create(victim.register(sdk))
                    .expectErrorSatisfies(t -> assertThat(t).hasMessageContaining("Registration failed in CloudMessaging Service"))
                    .verify();
    }

    @Test
    void shouldFailByTimeout() {
        given(exchangeFunction.exchange(any(ClientRequest.class)))
                .willReturn(Mono.never());
        var sdk = getTestSdk();
        StepVerifier.withVirtualTime(() -> victim.register(sdk))
                    .thenAwait(Duration.ofSeconds(3))
                    .expectErrorSatisfies(t -> assertThat(t).hasMessageContaining("Registration failed in CloudMessaging Service by timeout"))
                    .verify();

    }
}
