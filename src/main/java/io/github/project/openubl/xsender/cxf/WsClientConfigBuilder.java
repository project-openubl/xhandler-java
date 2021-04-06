package io.github.project.openubl.xsender.cxf;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.Interceptor;

import java.util.List;

public final class WsClientConfigBuilder {
    private Long connectionTimeout;
    private Long receiveTimeout;
    private List<Feature> features;
    private List<Interceptor<?>> inInterceptors;
    private List<Interceptor<?>> outInterceptors;
    private List<Interceptor<?>> inFaultInterceptors;
    private List<Interceptor<?>> outFaultInterceptors;

    private WsClientConfigBuilder() {
    }

    public static WsClientConfigBuilder aWsClientConfig() {
        return new WsClientConfigBuilder();
    }

    public WsClientConfigBuilder withConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public WsClientConfigBuilder withReceiveTimeout(Long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
        return this;
    }

    public WsClientConfigBuilder withFeatures(List<Feature> features) {
        this.features = features;
        return this;
    }

    public WsClientConfigBuilder withInInterceptors(List<Interceptor<?>> inInterceptors) {
        this.inInterceptors = inInterceptors;
        return this;
    }

    public WsClientConfigBuilder withOutInterceptors(List<Interceptor<?>> outInterceptors) {
        this.outInterceptors = outInterceptors;
        return this;
    }

    public WsClientConfigBuilder withInFaultInterceptors(List<Interceptor<?>> inFaultInterceptors) {
        this.inFaultInterceptors = inFaultInterceptors;
        return this;
    }

    public WsClientConfigBuilder withOutFaultInterceptors(List<Interceptor<?>> outFaultInterceptors) {
        this.outFaultInterceptors = outFaultInterceptors;
        return this;
    }

    public WsClientConfig build() {
        WsClientConfig wsClientConfig = new WsClientConfig();
        wsClientConfig.setConnectionTimeout(connectionTimeout);
        wsClientConfig.setReceiveTimeout(receiveTimeout);
        wsClientConfig.setFeatures(features);
        wsClientConfig.setInInterceptors(inInterceptors);
        wsClientConfig.setOutInterceptors(outInterceptors);
        wsClientConfig.setInFaultInterceptors(inFaultInterceptors);
        wsClientConfig.setOutFaultInterceptors(outFaultInterceptors);
        return wsClientConfig;
    }
}
