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

package io.github.projectopenubl.xmlsenderlib.webservices.providers;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.*;
import java.util.stream.Collectors;

public class ErrorBillServiceRegistry {

    private static volatile ErrorBillServiceRegistry instance;

    private List<ErrorBillServiceProviderFactory> factories;

    private ErrorBillServiceRegistry() {
        factories = new LinkedList<>();
        for (ErrorBillServiceProviderFactory factory : ServiceLoader.load(ErrorBillServiceProviderFactory.class)) {
            this.factories.add(factory);
        }
        factories.sort((t1, t2) -> t2.getPriority() - t1.getPriority());
    }

    public static ErrorBillServiceRegistry getInstance() {
        if (instance == null) {
            synchronized (ErrorBillServiceRegistry.class) {
                if (instance == null) {
                    instance = new ErrorBillServiceRegistry();
                }
            }
        }
        return instance;
    }

    public Set<ErrorBillServiceProviderFactory> getFactories(SOAPFaultException e) {
        TreeSet<ErrorBillServiceProviderFactory> result = new TreeSet<>((t1, t2) -> t2.getPriority() - t1.getPriority());
        return factories.stream()
                .filter(f -> f.isSupported(e))
                .collect(Collectors.toCollection(() -> result));
    }

    public Set<ErrorBillServiceProviderFactory> getFactories(int errorCode) {
        TreeSet<ErrorBillServiceProviderFactory> result = new TreeSet<>((t1, t2) -> t2.getPriority() - t1.getPriority());
        return factories.stream()
                .filter(f -> f.isSupported(errorCode))
                .collect(Collectors.toCollection(() -> result));
    }

}
