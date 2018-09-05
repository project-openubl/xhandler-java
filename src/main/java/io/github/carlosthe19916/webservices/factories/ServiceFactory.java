package io.github.carlosthe19916.webservices.factories;

import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceFactory {

    private static final long DEFAULT_CLIENT_CONNECTION_TIMEOUT = 30000L;
    private static final long DEFAULT_CLIENT_RECEIVE_TIMEOUT = 15000L;

    private static final Map<Class<?>, Map<ServiceConfig, Object>> classCache = Collections.synchronizedMap(new LinkedHashMap<>());

    private ServiceFactory() {
        // Just static methods
    }

    public static <T> T getInstance(Class<T> serviceClass, ServiceConfig config) {
        if (!classCache.containsKey(serviceClass)) {
            classCache.put(serviceClass, Collections.synchronizedMap(new LinkedHashMap<ServiceConfig, Object>() {
                @Override
                protected boolean removeEldestEntry(Map.Entry<ServiceConfig, Object> eldest) {
                    String maxSize = System.getenv("WS_SUNAT_CACHE_MAX_SIZE");
                    int MAX_SIZE = maxSize != null ? Integer.parseInt(maxSize) : Integer.MAX_VALUE;
                    return size() > MAX_SIZE;
                }
            }));
        }
        Map<ServiceConfig, Object> instancesCache = classCache.get(serviceClass);
        if (!instancesCache.containsKey(config)) {
            T instance = initInstance(serviceClass, config);
            instancesCache.put(config, instance);
        }

        return (T) instancesCache.get(config);
    }

    private static <T> T initInstance(Class<T> serviceClass, ServiceConfig config) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(serviceClass);

        /*
         * Address
         */
        factory.setAddress(config.getUrl());

        /*
         * Logging
         */

        String enableLogging = System.getenv("WS_SUNAT_LOGGING");
        if (enableLogging != null && enableLogging.equalsIgnoreCase("true")) {
            factory.getInInterceptors().add(new LoggingInInterceptor());
            factory.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        T client = (T) factory.create();
        defineTimeouts(client);
        configSecurity(client, config.getUsername(), config.getPassword());

        return client;
    }

    private static void defineTimeouts(Object serviceClass) {
        Client cxfClient = ClientProxy.getClient(serviceClass);
        HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();

        TLSClientParameters tlsParams = new TLSClientParameters();
        tlsParams.setDisableCNCheck(true);
        httpConduit.setTlsClientParameters(tlsParams);

        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();


        String connectionTimeout = System.getenv("WS_SUNAT_CLIENT_CONNECTION_TIMEOUT");
        String receiveTimeout = System.getenv("WS_SUNAT_CLIENT_RECEIVE_TIMEOUT");

        httpClientPolicy.setConnectionTimeout(connectionTimeout != null ? Long.parseLong(connectionTimeout) : DEFAULT_CLIENT_CONNECTION_TIMEOUT);
        httpClientPolicy.setReceiveTimeout(receiveTimeout != null ? Long.parseLong(receiveTimeout) : DEFAULT_CLIENT_RECEIVE_TIMEOUT);

        httpClientPolicy.setAllowChunking(false);
        httpConduit.setClient(httpClientPolicy);
    }

    private static <T> void configSecurity(T t, String username, String password) {
        /*
         * Define the configuration properties
         */
        Map<String, Object> outProps = new HashMap<>();
        outProps.put("action", "UsernameToken");
        outProps.put("passwordType", "PasswordText");

        /*
         * The username ('admin') is provided as a literal, the corresponding password will be determined by the client
         * password callback object.
         */
        outProps.put("user", username);
        outProps.put("passwordCallbackClass", ServicePasswordCallback.class.getName());

        /*
         * Save user password on memory
         */
        ServicePasswordCallback.PASSWORDS.putIfAbsent(username, password);

        Client client = ClientProxy.getClient(t);
        Endpoint cxfEnpoint = client.getEndpoint();

        WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
        cxfEnpoint.getOutInterceptors().add(wssOut);
    }

}
