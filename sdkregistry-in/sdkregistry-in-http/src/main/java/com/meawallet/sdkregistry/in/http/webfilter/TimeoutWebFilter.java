package com.meawallet.sdkregistry.in.http.webfilter;

import com.meawallet.sdkregistry.in.http.properties.HttpProperties;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TimeoutWebFilter implements WebFilter, Ordered {

    private final HttpProperties properties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                    .timeout(properties.getGlobalTimeout());
    }

    @Override
    public int getOrder() {
        return WebFilterOrder.TIMEOUT;
    }
}
