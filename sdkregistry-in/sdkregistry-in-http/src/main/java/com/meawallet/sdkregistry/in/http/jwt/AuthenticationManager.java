package com.meawallet.sdkregistry.in.http.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        return jwtUtil.validateToken(token)
                      .switchIfEmpty(Mono.empty())
                      .map(valid -> new UsernamePasswordAuthenticationToken(
                              "USER",
                              null,
                              Set.of(new SimpleGrantedAuthority("ROLE"))
                      ));
    }
}
