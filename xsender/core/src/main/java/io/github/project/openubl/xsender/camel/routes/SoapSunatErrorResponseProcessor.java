package io.github.project.openubl.xsender.camel.routes;

import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePropertyKey;
import org.apache.camel.Processor;
import org.apache.cxf.binding.soap.SoapFault;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.regex.Pattern;

public class SoapSunatErrorResponseProcessor implements Processor {
    private static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        SoapFault fault = exchange.getProperty(ExchangePropertyKey.EXCEPTION_CAUGHT, SoapFault.class);
        String message = fault.getMessage();

        QName qName = fault.getFaultCode();
        String localPart = qName.getLocalPart();

        int errorCodeInt;
        String errorCodeString = localPart
                .replaceAll("Client.", "")
                .replaceAll("Server.", "");
        if (isNumeric(errorCodeString)) {
            errorCodeInt = Integer.parseInt(errorCodeString);
        } else if (isNumeric(message)) {
            errorCodeInt = Integer.parseInt(message);
        } else {
            throw new IllegalStateException("Could not extract sunat error code", fault);
        }

        Metadata metadata = Metadata.builder()
                .notes(Collections.emptyList())
                .responseCode(errorCodeInt)
                .description(message)
                .build();

        SunatResponse sunatResponse = SunatResponse.builder()
                .status(Status.fromCode(errorCodeInt))
                .metadata(metadata)
                .build();
        exchange.getIn().setBody(sunatResponse);
    }

}
