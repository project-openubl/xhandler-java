package io.github.project.openubl.springboot.xsender.it;

import io.github.project.openubl.xsender.FileSender;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class SpringBootXSenderResource {

    private final FileSender xsender;

    public SpringBootXSenderResource(FileSender xsender) {
        this.xsender = xsender;
    }

    @PostMapping("/springboot-xsender/bill-service/send-invoice")
    public String sendInvoice() throws IOException {
        SunatResponse response = xsender.sendFile(xml("xmls/12345678912-01-F001-1.xml"));
        return response.getStatus().toString();
    }

    @PostMapping("/springboot-xsender/bill-service/send-voided-document")
    public String sendVoidedDocument() throws IOException {
        SunatResponse response = xsender.sendFile(xml("xmls/12345678912-RA-20200328-1.xml"));
        return response.getSunat().getTicket();
    }

    private static byte[] xml(String resource) throws IOException {
        try (InputStream is = SpringBootXSenderResource.class.getClassLoader().getResourceAsStream(resource)) {
            return is.readAllBytes();
        }
    }
}
