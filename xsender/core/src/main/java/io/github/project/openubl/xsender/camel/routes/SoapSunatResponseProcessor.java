package io.github.project.openubl.xsender.camel.routes;

import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.Sunat;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.utils.CdrReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;
import service.sunat.gob.pe.billservice.StatusResponse;

import java.util.Collections;
import java.util.Optional;

public class SoapSunatResponseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        MessageContentsList messageContentsList = exchange.getIn().getBody(MessageContentsList.class);
        if (messageContentsList != null && !messageContentsList.isEmpty()) {
            Object messageContent = messageContentsList.get(0);

            SunatResponse sunatResponse = null;
            if (messageContent instanceof byte[]) {
                byte[] cdrBytes = (byte[]) messageContent;
                CdrReader cdrReader = new CdrReader(cdrBytes);
                sunatResponse = SunatResponse.builder()
                        .status(cdrReader.getStatus())
                        .metadata(cdrReader.getMetadata())
                        .sunat(Sunat.builder()
                                .cdr(cdrBytes)
                                .build()
                        )
                        .build();
            } else if (messageContent instanceof String) {
                String ticket = (String) messageContent;
                sunatResponse = SunatResponse.builder()
                        .sunat(Sunat.builder()
                                .ticket(ticket)
                                .build()
                        )
                        .build();
            } else if (messageContent instanceof StatusResponse) {
                StatusResponse statusResponse = (StatusResponse) messageContent;

                int statusCode = Integer.parseInt(statusResponse.getStatusCode());
                byte[] cdrZipFile = statusResponse.getContent();

                Optional<TicketResponseType> ticketResponseTypeOptional = TicketResponseType.getFromCode(statusCode);
                if (ticketResponseTypeOptional.isPresent()) {
                    switch (ticketResponseTypeOptional.get()) {
                        case PROCESO_CORRECTAMENTE:
                        case PROCESO_CON_ERRORES: {
                            CdrReader cdrReader = new CdrReader(statusResponse.getContent());

                            sunatResponse = SunatResponse.builder()
                                    .status(cdrReader.getStatus())
                                    .metadata(cdrReader.getMetadata())
                                    .sunat(Sunat.builder()
                                            .cdr(cdrZipFile)
                                            .build()
                                    )
                                    .build();
                        }
                        break;
                        case EN_PROCESO: {
                            Metadata metadata = Metadata.builder()
                                    .responseCode(statusCode)
                                    .notes(Collections.emptyList())
                                    .description(TicketResponseType.EN_PROCESO.getDescription())
                                    .build();
                            sunatResponse = SunatResponse.builder()
                                    .status(Status.EN_PROCESO)
                                    .metadata(metadata)
                                    .sunat(Sunat.builder()
                                            .cdr(cdrZipFile)
                                            .build()
                                    )
                                    .build();
                        }
                        break;
                    }
                } else {
                    Metadata metadata = Metadata.builder()
                            .responseCode(statusCode)
                            .description("")
                            .notes(Collections.emptyList())
                            .build();

                    sunatResponse = SunatResponse.builder()
                            .status(Status.fromCode(statusCode))
                            .metadata(metadata)
                            .sunat(Sunat.builder()
                                    .cdr(cdrZipFile)
                                    .build()
                            )
                            .build();
                }
            }
            exchange.getIn().setBody(sunatResponse);
        }
    }

}
