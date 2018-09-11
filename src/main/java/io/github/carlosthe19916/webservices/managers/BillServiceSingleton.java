package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.SUNATErrorHandler;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BillServiceSingleton {

    private static volatile BillServiceSingleton instance;

    private Set<BillServiceErrorHandler> errorHandlers;

    private BillServiceSingleton() {
        errorHandlers = new TreeSet<>(Comparator.comparingInt(SUNATErrorHandler::getPriority));
        for (BillServiceErrorHandler errorHandler : ServiceLoader.load(BillServiceErrorHandler.class)) {
            errorHandlers.add(errorHandler);
        }
    }

    public static BillServiceSingleton getInstance() {
        if (instance == null) {
            synchronized (BillServiceSingleton.class) {
                if (instance == null) {
                    instance = new BillServiceSingleton();
                }
            }
        }
        return instance;
    }

    public Set<BillServiceErrorHandler> getApplicableErrorHandlers(SOAPFaultException e) {
        return errorHandlers.stream()
                .filter(f -> f.test(e))
                .collect(Collectors.toSet());
    }

}
