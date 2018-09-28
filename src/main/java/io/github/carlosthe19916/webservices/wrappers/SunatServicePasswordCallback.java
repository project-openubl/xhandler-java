/*
 * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.carlosthe19916.webservices.wrappers;

import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SunatServicePasswordCallback implements CallbackHandler {

    protected static final Map<String, String> PASSWORDS = new ConcurrentHashMap<>();

    @Override
    public void handle(Callback[] callbacks) {
        WSPasswordCallback passwordCallback = (WSPasswordCallback) callbacks[0];
        String user = passwordCallback.getIdentifier();
        passwordCallback.setPassword(PASSWORDS.get(user));
    }

}
