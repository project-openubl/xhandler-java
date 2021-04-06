package io.github.project.openubl.xsender;

import io.github.project.openubl.xmlsenderws.webservices.exceptions.InvalidXMLFileException;
import io.github.project.openubl.xmlsenderws.webservices.exceptions.UnsupportedDocumentTypeException;
import io.github.project.openubl.xsender.cxf.*;
import io.github.project.openubl.xsender.flyweight.WsClientFactory;
import io.github.project.openubl.xsender.flyweight.SimpleWsClientFactory;
import io.github.project.openubl.xsender.manager.BillServiceAdapter;
import service.sunat.gob.pe.billservice.BillService;

public class Example {

    public static void main(String[] args) throws InvalidXMLFileException, UnsupportedDocumentTypeException {
        // One in all system
        ProxyClientServiceFactory proxyFactory = new SimpleClientServiceFactory();

        WsClientConfig wsClientConfig = WsClientConfigBuilder.aWsClientConfig()
                .build();
        WsClientFactory wsClientFactory = new SimpleWsClientFactory(proxyFactory, wsClientConfig);

        // On for company
        CompanyConfig companyConfig = null;

        // For file
        byte[] file = new byte[]{};

        // Using the facade
        Sunat sunat = new Sunat(wsClientFactory);
        sunat.sendFile(companyConfig, file);

        // Using manual
        WsClientAuth auth = WsClientAuthBuilder.aWsClientAuth()
                .withAddress("deliveryURL")
                .withUsername("username")
                .withPassword("password")
                .build();

        BillService billService = wsClientFactory.getInstance(BillService.class, auth);
        BillServiceAdapter billServiceAdapter = new BillServiceAdapter(billService);
        billServiceAdapter.sendBill("filename", file);
    }
}
