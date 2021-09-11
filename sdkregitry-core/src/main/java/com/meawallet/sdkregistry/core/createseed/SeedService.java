package com.meawallet.sdkregistry.core.createseed;

import com.meawallet.sdkregistry.api.seed.CreateSeedCommand;
import com.meawallet.sdkregistry.api.seed.CreateSeedResult;
import com.meawallet.sdkregistry.core.cryptography.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.fromRunnable;
import static reactor.core.publisher.Mono.zip;

@Service
@AllArgsConstructor
@Slf4j
public class SeedService {

    private final RandomGenerator generator;

    public Mono<CreateSeedResult> create(CreateSeedCommand command) {
        var seed = defer(() -> generator.generate(command.getLength()))
                .map(Hex::encodeHexString);
        return fromRunnable(() -> log.debug("About to create seed with length : {}", command.getLength()))
                .then(zip(seed, seed))
                .map(TupleUtils.function(CreateSeedResult::new))
                .doOnNext(result -> log.debug("Seeds generated : {}, {}", result.getSeed1(), result.getSeed2()));
    }
}
