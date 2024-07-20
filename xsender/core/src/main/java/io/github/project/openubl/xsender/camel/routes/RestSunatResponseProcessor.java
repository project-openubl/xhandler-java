package io.github.project.openubl.xsender.camel.routes;

import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.Sunat;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.models.rest.ResponseDocumentSuccessDto;
import io.github.project.openubl.xsender.utils.CdrReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.codec.binary.Hex;

import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

public class RestSunatResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        ResponseDocumentSuccessDto responseDto = exchange.getIn().getBody(ResponseDocumentSuccessDto.class);
        if (responseDto != null) {
            SunatResponse sunatResponse = null;

            if (responseDto.getNumTicket() != null) {
                sunatResponse = SunatResponse.builder()
                        .sunat(Sunat.builder()
                                .ticket(responseDto.getNumTicket())
                                .build()
                        )
                        .build();
            } else if (responseDto.getArcCdr() != null) {
                String cdrBase64Hex = responseDto.getArcCdr();
                byte[] bytes = Hex.decodeHex(cdrBase64Hex);
                byte[] cdrBytes = Base64.getDecoder().decode(bytes);
                CdrReader cdrReader = new CdrReader(cdrBytes);

                SunatResponse.builder()
                        .status(cdrReader.getStatus())
                        .metadata(cdrReader.getMetadata())
                        .sunat(Sunat.builder()
                                .cdr(cdrBytes)
                                .build()
                        );
            } else if (responseDto.getCodRespuesta() != null) {
                int statusCode = Integer.parseInt(responseDto.getCodRespuesta());

                Optional<String> responseErrorCode = responseDto.getError() != null ? Optional.ofNullable(responseDto.getError().getNumError()) : Optional.empty();
                Optional<String> responseErrorDescription = responseDto.getError() != null ? Optional.ofNullable(responseDto.getError().getDesError()) : Optional.empty();

                Metadata metadata = Metadata.builder()
                        .responseCode(responseErrorCode.map(Integer::parseInt).orElse(statusCode))
                        .description(responseErrorDescription.orElse(null))
                        .notes(Collections.emptyList())
                        .build();

                sunatResponse = SunatResponse.builder()
                        .status(Status.fromCode(statusCode))
                        .metadata(metadata)
                        .sunat(null)
                        .build();
            }

            exchange.getIn().setBody(sunatResponse);
        }
    }

}
