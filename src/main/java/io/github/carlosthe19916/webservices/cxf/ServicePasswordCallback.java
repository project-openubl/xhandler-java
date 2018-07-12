package io.github.carlosthe19916.webservices.cxf;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import java.util.Map;
import java.util.TreeMap;

public class ServicePasswordCallback implements CallbackHandler {

    public static final Map<String, String> PASSWORDS = new TreeMap<>();

    @Override
    public void handle(Callback[] callbacks) {
        WSPasswordCallback passwordCallback = (WSPasswordCallback) callbacks[0];
        String user = passwordCallback.getIdentifier();
        passwordCallback.setPassword(PASSWORDS.get(user));
    }

}
