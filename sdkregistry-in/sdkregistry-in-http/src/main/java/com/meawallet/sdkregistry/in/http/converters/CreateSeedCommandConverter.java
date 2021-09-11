package com.meawallet.sdkregistry.in.http.converters;

import com.meawallet.sdkregistry.api.seed.CreateSeedCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static reactor.core.publisher.Mono.justOrEmpty;

@Component
public class CreateSeedCommandConverter implements HttpRequestConverter<CreateSeedCommand> {
    @Override
    public Mono<CreateSeedCommand> apply(ServerRequest serverRequest) {
        return Mono.defer(() -> justOrEmpty(parseLength(serverRequest)))
                   .map(CreateSeedCommand::new)
                   .defaultIfEmpty(new CreateSeedCommand());
    }

    private Optional<Integer> parseLength(ServerRequest request) {
        return request.queryParam("length")
                      .filter(param -> param.matches("[0-9]{1,}"))
                      .map(Integer::parseInt);
    }
}
