package com.meawallet.sdkregistry.core.registration.initialize;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedSdkKeyset;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSdkKeyset;
import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import com.meawallet.sdkregistry.api.register.InitializeRegistrationCommand;
import com.meawallet.sdkregistry.api.register.InitializeRegistrationResult;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Service
@AllArgsConstructor
@Slf4j
public class InitializeRegistrationService {

    private final PlainSdkKeysetGenerationService plainSdkKeysetGenerationService;
    private final RgkDecryptionService rgkDecryptionService;
    private final PlainSdkKeysetEncryptionService plainSdkKeysetEncryptionService;
    private final SdkInstancePersistenceService sdkInstancePersistenceService;
    private final SdkInstanceKeysPersistenceService sdkInstanceKeysPersistenceService;
    private final Collection<RegistrationOutService> registrationOutServices;

    public Mono<InitializeRegistrationResult> initialize(InitializeRegistrationCommand command) {
        var registration = createKeys(command)
                .map(keys -> convert(command, keys))
                .flatMap(this::saveAllInDb)
                .flatMap(this::registerInOutServices)
                .map(sdk -> new InitializeRegistrationResult(sdk.getEncryptedSdkKeyset()));

        return Mono.fromRunnable(() -> log.debug("About to initialize registration : {}", command))
                   .then(registration)
                   .doOnNext(result -> log.debug("About to initialize registration result : {}", result));
    }

    private Mono<SdkKeys> createKeys(InitializeRegistrationCommand command) {
        return plainSdkKeysetGenerationService.generate()
                                              .flatMap(plainKeyset -> withEncryptKeys(plainKeyset, command));
    }

    private Mono<SdkKeys> withEncryptKeys(PlainSdkKeyset plainKeyset, InitializeRegistrationCommand command) {
        return rgkDecryptionService.decrypt(command.getRgk(), command.getShredKeyFingerprint())
                                   .flatMap(rgk -> plainSdkKeysetEncryptionService.encrypt(plainKeyset, rgk))
                                   .map(encryptedKeyset -> new SdkKeys(plainKeyset, encryptedKeyset));
    }

    private Sdk convert(InitializeRegistrationCommand command, SdkKeys keys) {
        return new Sdk(
                command.getSdkId(),
                command.getSdkInstanceId(),
                command.getDevicePublicKey(),
                command.getFcmRegistrationId(),
                command.getGpsLocation(),
                keys.plainSdkKeyset,
                keys.encryptedSdkKeyset
        );
    }

    private Mono<Sdk> saveAllInDb(Sdk sdk) {
        return Mono.when(sdkInstancePersistenceService.create(sdk))
                   .then(sdkInstanceKeysPersistenceService.create(sdk))
                   .thenReturn(sdk);
    }

    private Mono<Sdk> registerInOutServices(Sdk sdk) {
        var registerAll = Flux.fromIterable(registrationOutServices)
                              .flatMap(service -> service.register(sdk));
        return Mono.when(registerAll)
                   .thenReturn(sdk);
    }

    @Value
    private static class SdkKeys {
        PlainSdkKeyset plainSdkKeyset;
        EncryptedSdkKeyset encryptedSdkKeyset;
    }
}
