package com.meawallet.sdkregistry.out.attestation;

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
class AttestationRegistrationOutService implements RegistrationOutService {

    private final AttestationServiceProperties properties;
    private final WebClient webClient;

    @Override
    public Mono<Void> register(Sdk sdk) {
        return Mono.fromSupplier(() -> convert(sdk))
                   .flatMap(this::send)
                   .timeout(properties.getTimeout())
                   .onErrorResume(
                           TimeoutException.class,
                           e -> internalError("Registration failed in Attestation Service by timeout", e)
                   );
    }

    private AttestationServiceRegistrationCommand convert(Sdk sdk) {
        return new AttestationServiceRegistrationCommand(
                sdk.getSdkInstanceId(),
                sdk.getPublicKeyDer(),
                sdk.getPlainSdkKeyset().getAttestation().getEncryptionKey(),
                sdk.getPlainSdkKeyset().getAttestation().getMacKey()
        );
    }

    private Mono<Void> send(AttestationServiceRegistrationCommand command) {
        return webClient.post()
                        .uri(properties.getRegistrationUrl())
                        .bodyValue(command)
                        .retrieve()
                        .onStatus(
                                status -> !status.is2xxSuccessful(),
                                response -> internalError("Registration failed in Attestation Service")
                        )
                        .toBodilessEntity()
                        .then();
    }
}
