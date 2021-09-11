package com.meawallet.sdkregistry.out.transaction;

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
class TransactionRegistrationOutService implements RegistrationOutService {

    private final TransactionServiceProperties properties;
    private final WebClient webClient;

    @Override
    public Mono<Void> register(Sdk sdk) {
        return Mono.fromSupplier(() -> convert(sdk))
                   .flatMap(this::send)
                   .timeout(properties.getTimeout())
                   .onErrorResume(
                           TimeoutException.class,
                           e -> internalError("Registration failed in Transaction Service by timeout", e)
                   );
    }

    private TransactionServiceRegistrationCommand convert(Sdk sdk) {
        return new TransactionServiceRegistrationCommand(
                sdk.getSdkInstanceId(),
                sdk.getPlainSdkKeyset().getTransaction().getEncryptionKey(),
                sdk.getPlainSdkKeyset().getTransaction().getMacKey()
        );
    }

    private Mono<Void> send(TransactionServiceRegistrationCommand command) {
        return webClient.post()
                        .uri(properties.getRegistrationUrl())
                        .bodyValue(command)
                        .retrieve()
                        .onStatus(
                                status -> !status.is2xxSuccessful(),
                                response -> internalError("Registration failed in Transaction Service")
                        )
                        .toBodilessEntity()
                        .then();
    }
}
