package com.meawallet.sdkregistry.out.cloudmessaging;

import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import com.meawallet.sdkregistry.core.registration.initialize.RegistrationOutService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

import static com.meawallet.sdkregistry.api.errors.SdkRegistryException.internalError;

@Service
@AllArgsConstructor
class CloudMessagingRegistrationOutService implements RegistrationOutService {

    private final CloudMessagingServiceProperties properties;
    private final WebClient webClient;

    @Override
    public Mono<Void> register(Sdk sdk) {
        return Mono.fromSupplier(() -> convert(sdk))
                   .flatMap(this::send)
                   .timeout(properties.getTimeout())
                   .onErrorResume(
                           TimeoutException.class,
                           e -> internalError("Registration failed in CloudMessaging Service by timeout", e)
                   );
    }

    private CloudMessagingServiceRegistrationCommand convert(Sdk sdk) {
        return new CloudMessagingServiceRegistrationCommand(
                sdk.getSdkInstanceId(),
                sdk.getFcmRegistrationId(),
                sdk.getPlainSdkKeyset().getPush().getDataEncryptionKey(),
                sdk.getPlainSdkKeyset().getPush().getDataMacKey(),
                sdk.getPlainSdkKeyset().getPush().getMessageMacKey()
        );
    }

    private Mono<Void> send(CloudMessagingServiceRegistrationCommand command) {
        return webClient.post()
                        .uri(properties.getRegistrationUrl())
                        .bodyValue(command)
                        .retrieve()
                        .onStatus(
                                status -> !status.is2xxSuccessful(),
                                response -> internalError("Registration failed in CloudMessaging Service")
                        )
                        .toBodilessEntity()
                        .then();
    }
}
