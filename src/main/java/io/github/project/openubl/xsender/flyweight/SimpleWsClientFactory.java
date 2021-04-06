package io.github.project.openubl.xsender.flyweight;

import io.github.project.openubl.xmlsenderws.webservices.wrappers.CacheLinkedHashMap;
import io.github.project.openubl.xsender.cxf.WsClientAuth;
import io.github.project.openubl.xsender.cxf.WsClientConfig;
import io.github.project.openubl.xsender.cxf.ProxyClientServiceFactory;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleWsClientFactory implements WsClientFactory {

    private final Map<WsClientAuth, Map<Class<?>, Object>> instances = Collections.synchronizedMap(new CacheLinkedHashMap<>());

    private final ProxyClientServiceFactory factory;
    private final WsClientConfig config;

    public SimpleWsClientFactory(ProxyClientServiceFactory factory, WsClientConfig config) {
        this.factory = factory;
        this.config = config;
    }

    @Override
    public <T> T getInstance(Class<T> tClass, WsClientAuth auth) {
        instances.putIfAbsent(auth, new ConcurrentHashMap<>());

        if (!instances.get(auth).containsKey(tClass)) {
            T t = factory.create(tClass, auth, config);
            instances.get(auth).putIfAbsent(tClass, t);
        }

        return (T) instances.get(auth).get(tClass);
    }
}
