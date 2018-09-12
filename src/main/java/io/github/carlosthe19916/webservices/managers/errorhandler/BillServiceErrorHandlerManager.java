package io.github.carlosthe19916.webservices.managers.errorhandler;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Comparator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BillServiceErrorHandlerManager {

    private static volatile BillServiceErrorHandlerManager instance;

    private Set<BillServiceErrorHandler> errorHandlers;

    private BillServiceErrorHandlerManager() {
        errorHandlers = new TreeSet<>(Comparator.comparingInt(SUNATErrorHandler::getPriority));
        for (BillServiceErrorHandler errorHandler : ServiceLoader.load(BillServiceErrorHandler.class)) {
            errorHandlers.add(errorHandler);
        }
    }

    public static BillServiceErrorHandlerManager getInstance() {
        if (instance == null) {
            synchronized (BillServiceErrorHandlerManager.class) {
                if (instance == null) {
                    instance = new BillServiceErrorHandlerManager();
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
