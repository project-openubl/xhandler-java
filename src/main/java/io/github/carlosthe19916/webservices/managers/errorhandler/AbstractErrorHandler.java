package io.github.carlosthe19916.webservices.managers.errorhandler;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Optional;

public abstract class AbstractErrorHandler implements SUNATErrorHandler {

    protected Optional<Integer> getErrorCode(SOAPFaultException e) {
        String errorCode = e.getFault().getFaultCode().replaceAll("soap-env:Client.", "");

        if (!errorCode.matches("-?\\d+")) {
            errorCode = e.getMessage();
        }

        if (!errorCode.matches("-?\\d+")) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(errorCode));
    }

    protected String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

}
