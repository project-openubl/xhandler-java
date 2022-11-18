/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xsender;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.discovery.FileAnalyzer;
import io.github.project.openubl.xsender.discovery.XMLFileAnalyzer;
import io.github.project.openubl.xsender.exceptions.UnsupportedXMLFileException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

public class ExampleTest {

    public static void main(String[] args)
        throws ParserConfigurationException, IOException, UnsupportedXMLFileException, SAXException, XPathExpressionException {
        // On for company
        CompanyURLs companyURLs = CompanyURLs
            .builder()
            .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .despatch("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
            .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-guia-gem-beta/billService")
            .build();

        CompanyCredentials credentials = CompanyCredentials
            .builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .build();

        // Using the facade
        InputStream xmlFileIS = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("ubl/12345678912-RA-20200328-1.xml");
        FileAnalyzer fileAnalyzer = new XMLFileAnalyzer(xmlFileIS, companyURLs);

        XSenderFileResponse fileResponse = XSenderHolder
            .getInstance()
            .getxSender()
            .sendXmlFile(fileAnalyzer.getZipFile(), fileAnalyzer.getFileDeliveryTarget(), credentials);

        if (fileResponse.getTicket() != null) {
            XSenderTicketResponse ticketResponse = XSenderHolder
                .getInstance()
                .getxSender()
                .ticket(fileResponse.getTicket(), fileAnalyzer.getTicketDeliveryTarget(), credentials);

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
