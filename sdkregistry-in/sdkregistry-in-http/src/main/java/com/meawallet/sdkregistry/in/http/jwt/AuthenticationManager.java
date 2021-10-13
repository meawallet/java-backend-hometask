package com.meawallet.sdkregistry.in.http.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        Mono.just(jwtUtil.validateToken(token))
//            .filter(valid -> valid)
            .switchIfEmpty(Mono.empty());

        return Mono.just(authentication);
    }
}
