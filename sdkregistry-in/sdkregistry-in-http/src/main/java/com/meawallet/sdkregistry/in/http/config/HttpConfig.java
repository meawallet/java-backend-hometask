package com.meawallet.sdkregistry.in.http.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meawallet.sdkregistry.api.register.CompleteRegistrationCommand;
import com.meawallet.sdkregistry.api.register.CompleteRegistrationResult;
import com.meawallet.sdkregistry.api.register.InitializeRegistrationCommand;
import com.meawallet.sdkregistry.api.register.InitializeRegistrationResult;
import com.meawallet.sdkregistry.api.seed.CreateSeedCommand;
import com.meawallet.sdkregistry.api.seed.CreateSeedResult;
import com.meawallet.sdkregistry.core.createseed.SeedService;
import com.meawallet.sdkregistry.core.registration.complete.CompleteRegistrationService;
import com.meawallet.sdkregistry.core.registration.initialize.InitializeRegistrationService;
import com.meawallet.sdkregistry.in.http.converters.BodyfullResultHttpResponseConverter;
import com.meawallet.sdkregistry.in.http.converters.BodylessResultHttpResponseConverter;
import com.meawallet.sdkregistry.in.http.converters.CompleteRegisterCommandConverter;
import com.meawallet.sdkregistry.in.http.converters.CreateSeedCommandConverter;
import com.meawallet.sdkregistry.in.http.converters.InitializeRegisterCommandConverter;
import com.meawallet.sdkregistry.in.http.errors.HttpErrorResponseWriter;
import com.meawallet.sdkregistry.in.http.handling.HttpRequestHandler;
import com.meawallet.sdkregistry.in.http.properties.HttpProperties;
import com.meawallet.sdkregistry.in.http.validation.ReactiveValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class HttpConfig {

    private final ReactiveValidator reactiveValidator;
    private final CreateSeedCommandConverter createSeedCommandConverter;
    private final BodyfullResultHttpResponseConverter bodyfullResultHttpResponseConverter;
    private final BodylessResultHttpResponseConverter bodylessResultHttpResponseConverter;
    private final CompleteRegisterCommandConverter completeRegisterCommandConverter;
    private final InitializeRegisterCommandConverter initializeRegisterCommandConverter;
    private final SeedService seedService;
    private final InitializeRegistrationService initializeRegistrationService;
    private final CompleteRegistrationService completeRegistrationService;

    @Bean
    @Validated
    @ConfigurationProperties("sdkregistry.http-in")
    public HttpProperties httpProperties() {
        return new HttpProperties();
    }

    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.ALWAYS));
        return objectMapper;
    }

    @Bean
    public Jackson2JsonEncoder jackson2JsonEncoder(@Qualifier("objectMapper") ObjectMapper objectMapper) {
        return new Jackson2JsonEncoder(objectMapper);
    }

    @Bean
    public Jackson2JsonDecoder jackson2JsonDecoder(@Qualifier("objectMapper") ObjectMapper objectMapper) {
        return new Jackson2JsonDecoder(objectMapper);
    }

    @Bean
    public WebFluxConfigurer webFluxConfigurer(Jackson2JsonEncoder jackson2JsonEncoder,
                                               Jackson2JsonDecoder jackson2JsonDecoder) {
        return new WebFluxConfigurer() {
            @Override
            public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
                configurer.defaultCodecs().jackson2JsonEncoder(jackson2JsonEncoder);
                configurer.defaultCodecs().jackson2JsonDecoder(jackson2JsonDecoder);
            }
        };
    }

    @Bean
    public HttpErrorResponseWriter httpErrorResponseWriter(Jackson2JsonEncoder jackson2JsonEncoder) {
        var writer = new EncoderHttpMessageWriter<>(jackson2JsonEncoder);
        return new HttpErrorResponseWriter(List.of(writer));
    }

    @Bean
    public RouterFunction<ServerResponse> testRouter() {
        return route()
                .path("/sdkregistry/v1", builder -> builder
                        .GET("/seed", createSeed())
                        .POST("/registration/initialize", initializeRegistration())
                        .POST("/registration/complete", completeRegistration())
                ).build();
    }

    private HttpRequestHandler<CreateSeedCommand, CreateSeedResult> createSeed() {
        return new HttpRequestHandler<>(
                reactiveValidator,
                createSeedCommandConverter,
                bodyfullResultHttpResponseConverter,
                seedService::create
        );
    }

    private HttpRequestHandler<InitializeRegistrationCommand, InitializeRegistrationResult> initializeRegistration() {
        return new HttpRequestHandler<>(
                reactiveValidator,
                initializeRegisterCommandConverter,
                bodyfullResultHttpResponseConverter,
                initializeRegistrationService::initialize
        );
    }

    private HttpRequestHandler<CompleteRegistrationCommand, CompleteRegistrationResult> completeRegistration() {
        return new HttpRequestHandler<>(
                reactiveValidator,
                completeRegisterCommandConverter,
                bodylessResultHttpResponseConverter,
                completeRegistrationService::complete
        );
    }
}
