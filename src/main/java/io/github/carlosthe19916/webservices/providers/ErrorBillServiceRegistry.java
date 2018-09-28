package io.github.carlosthe19916.webservices.providers;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class ErrorBillServiceRegistry {

    private static volatile ErrorBillServiceRegistry instance;

    protected List<ErrorBillServiceProviderFactory> factories;

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
        return factories.stream()
                .filter(f -> f.isSupported(e))
                .collect(Collectors.toSet());
    }

    public Set<ErrorBillServiceProviderFactory> getFactories(int errorCode) {
        return factories.stream()
                .filter(f -> f.isSupported(errorCode))
                .collect(Collectors.toSet());
    }

}
