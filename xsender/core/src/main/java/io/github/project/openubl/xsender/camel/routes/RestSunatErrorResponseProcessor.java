package io.github.project.openubl.xsender.camel.routes;

import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.models.rest.ResponseDocumentErrorDto;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.http.base.HttpOperationFailedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestSunatErrorResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpOperationFailedException httpException = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);
        ResponseDocumentErrorDto responseDocumentErrorDto = exchange.getIn().getBody(ResponseDocumentErrorDto.class);

        String responseCode = Optional.ofNullable(responseDocumentErrorDto.getCod())
                .orElse(responseDocumentErrorDto.getStatus());
        int responseCodeInt = Integer.parseInt(responseCode);

        String responseDescription = Optional.ofNullable(responseDocumentErrorDto.getMsg())
                .orElse(responseDocumentErrorDto.getMessage());

        List<String> notes = Optional.ofNullable(responseDocumentErrorDto.getErrors())
                .orElse(Collections.emptyList())
                .stream()
                .map(error -> error.getCod() + " - " + error.getMsg())
                .collect(Collectors.toList());

        SunatResponse sunatResponse = SunatResponse.builder()
                .status(Status.fromCode(responseCodeInt))
                .metadata(Metadata.builder()
                        .notes(notes)
                        .responseCode(responseCodeInt)
                        .description(responseDescription)
                        .build()
                )
                .build();

        exchange.getIn().setBody(sunatResponse);
    }

}
