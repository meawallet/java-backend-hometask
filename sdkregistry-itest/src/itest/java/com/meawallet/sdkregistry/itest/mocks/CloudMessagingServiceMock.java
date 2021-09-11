package com.meawallet.sdkregistry.itest.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.meawallet.sdkregistry.out.cloudmessaging.CloudMessagingServiceProperties;
import org.springframework.context.SmartLifecycle;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class CloudMessagingServiceMock implements SmartLifecycle {

    private final URI uri;
    private final WireMockServer wireMockServer;

    public CloudMessagingServiceMock(CloudMessagingServiceProperties properties) {
        this.uri = URI.create(properties.getRegistrationUrl());
        this.wireMockServer = new WireMockServer(options().port(this.uri.getPort()));
    }

    public void hasInteractions(int count) {
        wireMockServer.verify(
                count,
                postRequestedFor(urlPathEqualTo(uri.getPath()))
        );
    }

    public void stub(String request, String response) {
        reset();
        var requestStub = StubMapping.buildFrom(request);
        var responseStub = StubMapping.buildFrom(response);
        requestStub.setResponse(responseStub.getResponse());
        wireMockServer.addStubMapping(requestStub);
    }

    public void reset() {
        wireMockServer.resetAll();
    }

    @Override
    public void start() {
        wireMockServer.start();
    }

    @Override
    public void stop() {
        wireMockServer.stop();
    }

    @Override
    public boolean isRunning() {
        return wireMockServer.isRunning();
    }
}
