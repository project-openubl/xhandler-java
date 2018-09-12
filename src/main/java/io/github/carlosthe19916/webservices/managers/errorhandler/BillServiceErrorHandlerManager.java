package io.github.carlosthe19916.webservices.managers.errorhandler;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class BillServiceErrorHandlerManager {

    private static volatile BillServiceErrorHandlerManager instance;

    protected List<BillServiceErrorHandler> errorHandlers;

    private BillServiceErrorHandlerManager() {
        errorHandlers = new LinkedList<>();
        for (BillServiceErrorHandler errorHandler : ServiceLoader.load(BillServiceErrorHandler.class)) {
            errorHandlers.add(errorHandler);
        }

        errorHandlers.sort((t1, t2) -> t2.getPriority() - t1.getPriority());
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
