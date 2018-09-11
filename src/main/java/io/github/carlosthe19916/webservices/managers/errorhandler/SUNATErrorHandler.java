package io.github.carlosthe19916.webservices.managers.errorhandler;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.function.Predicate;

public interface SUNATErrorHandler extends Predicate<SOAPFaultException> {

    int getPriority();

}
