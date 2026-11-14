package io.github.project.openubl.quarkus.xsender.it;

import io.github.project.openubl.xsender.FileSender;
import io.github.project.openubl.xsender.models.SunatResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.io.IOException;
import java.io.InputStream;

@Path("/quarkus-xsender")
@ApplicationScoped
public class QuarkusXSenderResource {

    @Inject
    FileSender xsender;

    @POST
    @Path("bill-service/send-invoice")
    public String sendInvoice() throws IOException {
        SunatResponse response = xsender.sendFile(xml("xmls/12345678912-01-F001-1.xml"));
        return response.getStatus().toString();
    }

    @POST
    @Path("bill-service/send-voided-document")
    public String sendVoidedDocument() throws IOException {
        SunatResponse response = xsender.sendFile(xml("xmls/12345678912-RA-20200328-1.xml"));
        return response.getSunat().getTicket();
    }

    private static byte[] xml(String resource) throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            return is.readAllBytes();
        }
    }
}
