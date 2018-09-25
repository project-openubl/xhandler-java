package io.github.carlosthe19916.webservices.managers.errorhandler;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class BillServiceErrorHandlerFactoryManager {

    private static volatile BillServiceErrorHandlerFactoryManager instance;

    protected List<BillServiceErrorHandlerFactory> factories;

    private BillServiceErrorHandlerFactoryManager() {
        factories = new LinkedList<>();
        for (BillServiceErrorHandlerFactory errorHandler : ServiceLoader.load(BillServiceErrorHandlerFactory.class)) {
            factories.add(errorHandler);
        }

        factories.sort((t1, t2) -> t2.getPriority() - t1.getPriority());
    }

    public static BillServiceErrorHandlerFactoryManager getInstance() {
        if (instance == null) {
            synchronized (BillServiceErrorHandlerFactoryManager.class) {
                if (instance == null) {
                    instance = new BillServiceErrorHandlerFactoryManager();
                }
            }
        }
        return instance;
    }

    public Set<BillServiceErrorHandlerFactory> getApplicableErrorHandlers(SOAPFaultException e) {
        return factories.stream()
                .filter(f -> f.test(e))
                .collect(Collectors.toSet());
    }

    public Set<BillServiceErrorHandlerFactory> getApplicableErrorHandlers(int errorCode) {
        return factories.stream()
                .filter(f -> f.test(errorCode))
                .collect(Collectors.toSet());
    }

}
