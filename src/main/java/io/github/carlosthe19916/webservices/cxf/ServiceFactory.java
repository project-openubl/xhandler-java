package io.github.carlosthe19916.webservices.cxf;

import io.github.carlosthe19916.webservices.CxfConfig;
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
import service.sunat.gob.pe.billservice.BillService;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {

    private static final long DEFAULT_CLIENT_CONNECTION_TIMEOUT = 30000L;
    private static final long DEFAULT_CLIENT_RECEIVE_TIMEOUT = 15000L;

    private ServiceFactory() {
        // Just static methods
    }

    public static <T> T get(Class<T> serviceClass, CxfConfig config) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(BillService.class);

        /*
         * Address
         */
        factory.setAddress(config.getUrl());

        /*
         * Logging
         */
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());

        T client = (T) factory.create();
        defineTimeouts(client);
        configSecurity(client, config.getUsername(), config.getPasswod());
        return client;
    }

    private static void defineTimeouts(Object serviceClass) {
        Client cxfClient = ClientProxy.getClient(serviceClass);
        HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();

        TLSClientParameters tlsParams = new TLSClientParameters();
        tlsParams.setDisableCNCheck(true);
        httpConduit.setTlsClientParameters(tlsParams);

        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(DEFAULT_CLIENT_CONNECTION_TIMEOUT);
        httpClientPolicy.setReceiveTimeout(DEFAULT_CLIENT_RECEIVE_TIMEOUT);
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
