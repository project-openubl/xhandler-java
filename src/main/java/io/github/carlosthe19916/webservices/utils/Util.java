package io.github.carlosthe19916.webservices.utils;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Optional;

public class Util {

    private Util() {
        // Just static methods
    }

    public static Optional<Integer> getErrorCode(SOAPFaultException e) {
        String errorCode = e.getFault().getFaultCode().replaceAll("soap-env:Client.", "");

        if (!errorCode.matches("-?\\d+")) {
            errorCode = e.getMessage();
        }

        if (!errorCode.matches("-?\\d+")) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(errorCode));
    }

    public static String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

}
