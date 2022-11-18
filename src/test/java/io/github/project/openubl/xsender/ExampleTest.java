package io.github.project.openubl.xsender;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.cxf.ProxyClientServiceFactory;
import io.github.project.openubl.xsender.cxf.SimpleClientServiceFactory;
import io.github.project.openubl.xsender.cxf.WsClientConfig;
import io.github.project.openubl.xsender.discovery.FileAnalyzer;
import io.github.project.openubl.xsender.discovery.FileDeliveryTarget;
import io.github.project.openubl.xsender.discovery.XMLFileAnalyzer;
import io.github.project.openubl.xsender.discovery.ZipFile;
import io.github.project.openubl.xsender.exceptions.UnsupportedXMLFileException;
import io.github.project.openubl.xsender.flyweight.SimpleWsClientFactory;
import io.github.project.openubl.xsender.sunat.SendFileResponse;
import io.github.project.openubl.xsender.sunat.Sunat;
import org.apache.cxf.helpers.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;

public class ExampleTest {

    public static void main(String[] args) throws ParserConfigurationException, IOException, UnsupportedXMLFileException, SAXException, XPathExpressionException {
        // On for company
        CompanyURLs companyURLs = CompanyURLs.builder()
                .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
                .despatch("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
                .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-guia-gem-beta/billService")
                .build();

        CompanyCredentials credentials = CompanyCredentials.builder()
                .username("12345678959MODDATOS")
                .password("MODDATOS")
                .build();

        // Using the facade
        InputStream xmlFileIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("ubl/12345678912-RA-20200328-1.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        XSenderFileResponse fileResponse = XSender.getInstance()
                .sendXmlFile(fileAnalyzer.getZipFile(), fileAnalyzer.getFileDeliveryTarget(), credentials);

        if (fileResponse.getTicket() != null) {
            XSenderTicketResponse ticketResponse = XSender.getInstance().ticket(
                    fileResponse.getTicket(),
                    fileAnalyzer.getTicketDeliveryTarget(),
                    credentials
            );

            System.out.println(ticketResponse);
        }

        // Using manual
//        ProxyClientServiceFactory proxyClientServiceFactory = new SimpleClientServiceFactory();
//        WsClientConfig wsClientConfig = WsClientConfig.builder()
//                .connectionTimeout(15_000L)
//                .receiveTimeout(10_000L)
//                .build();
//        SimpleWsClientFactory wsClientFactory = new SimpleWsClientFactory(proxyClientServiceFactory, wsClientConfig);
//
//        xmlFileIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("ubl/12345678912-01-F001-1.zip");
//        byte[] xmlFileBytes = IOUtils.readBytesFromStream(xmlFileIS);
//
//        Sunat sunat = new Sunat(wsClientFactory);
//        SendFileResponse sendFileResponse = sunat.sendFile(
//                new ZipFile(xmlFileBytes, "12345678912-01-F001-1.zip"),
//                new FileDeliveryTarget("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService", FileDeliveryTarget.Method.SEND_BILL),
//                credentials
//        );
//
//        System.out.println(sendFileResponse);
    }

}