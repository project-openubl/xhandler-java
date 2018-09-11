package io.github.carlosthe19916.webservices.utils;

import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import java.util.Optional;

public class Util {

    private Util() {
        // Just static methods
    }

    public static Optional<Integer> getErrorCode(SOAPFaultException exception) {
        String errorCode = "";

        SOAPFault fault = exception.getFault();
        if (fault != null) {
            String faultCode = fault.getFaultCode();
            if (faultCode != null) {
                errorCode = faultCode.replaceAll("soap-env:Client.", "");
            }
        }

        if (!errorCode.matches("-?\\d+")) {
            String exceptionMessage = exception.getMessage();
            if (exceptionMessage != null) {
                errorCode = exceptionMessage.replaceAll("soap-env:Client.", "");
            }
        }

        if (!errorCode.matches("-?\\d+")) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(errorCode));
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName;
    }

}
