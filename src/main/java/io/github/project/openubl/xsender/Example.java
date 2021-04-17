package io.github.project.openubl.xsender;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyCredentialsBuilder;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.company.CompanyURLsBuilder;
import io.github.project.openubl.xsender.cxf.SimpleClientServiceFactory;
import io.github.project.openubl.xsender.cxf.WsClientConfig;
import io.github.project.openubl.xsender.cxf.WsClientConfigBuilder;
import io.github.project.openubl.xsender.discovery.*;
import io.github.project.openubl.xsender.exceptions.UnsupportedXMLFileException;
import io.github.project.openubl.xsender.flyweight.SimpleWsClientFactory;
import io.github.project.openubl.xsender.flyweight.WsClientFactory;
import io.github.project.openubl.xsender.response.CdrReader;
import io.github.project.openubl.xsender.response.CrdStatus;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Optional;

public class Example {

    public static void main(String[] args) throws ParserConfigurationException, IOException, UnsupportedXMLFileException, SAXException, XPathExpressionException {
        // One in all system
        WsClientConfig wsClientConfig = WsClientConfigBuilder.aWsClientConfig()
                .withConnectionTimeout(15_000L)
                .withReceiveTimeout(10_000L)
                .build();

        WsClientFactory wsClientFactory = new SimpleWsClientFactory(
                new SimpleClientServiceFactory(), wsClientConfig
        );

        // On for company
        CompanyURLs companyURLs = CompanyURLsBuilder.aCompanyURLs()
                .withInvoice("a")
                .withDespatch("b")
                .withPerceptionRetention("c")
                .build();

        CompanyCredentials credentials = CompanyCredentialsBuilder.aCompanyCredentials()
                .withUsername("")
                .withPassword("")
                .build();

        // Using the facade
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(new byte[]{}, companyURLs);

        Sunat sunat = new Sunat(wsClientFactory);
        SendFileResponse fileResponse = sunat.sendFile(
                fileAnalyzer.getZipFile(),
                fileAnalyzer.getFileDeliveryTarget(),
                credentials
        );

        CdrReader cdrReader = new CdrReader(fileResponse.getCdr());
        Optional<CrdStatus> cdrStatus = cdrReader.getCdrStatus();

        if (fileResponse.getTicket() != null) {
            VerifyTicketResponse ticketResponse = sunat.verifyTicketStatus(
                    fileResponse.getTicket(),
                    fileAnalyzer.getTicketDeliveryTarget(),
                    credentials
            );

            cdrReader = new CdrReader(ticketResponse.getCdr());
            cdrStatus = cdrReader.getCdrStatus();
        }

        //

        // Using manual
        sunat = new Sunat(wsClientFactory);
        sunat.sendFile(
                new ZipFile(new byte[]{}, "123456789.xml"),
                new FileDeliveryTarget("", FileDeliveryTarget.Method.SEND_BILL),
                credentials
        );
    }
}
