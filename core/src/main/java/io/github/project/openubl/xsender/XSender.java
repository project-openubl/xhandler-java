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
import io.github.project.openubl.xsender.discovery.FileDeliveryTarget;
import io.github.project.openubl.xsender.discovery.TicketDeliveryTarget;
import io.github.project.openubl.xsender.discovery.ZipFile;
import io.github.project.openubl.xsender.flyweight.WsClientFactory;
import io.github.project.openubl.xsender.response.CdrReader;
import io.github.project.openubl.xsender.sunat.SendFileResponse;
import io.github.project.openubl.xsender.sunat.Sunat;
import io.github.project.openubl.xsender.sunat.VerifyTicketResponse;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

public class XSender {

    private final WsClientFactory wsClientFactory;

    public XSender(WsClientFactory wsClientFactory) {
        this.wsClientFactory = wsClientFactory;
    }

    public XSenderFileResponse sendXmlFile(
        ZipFile zipFile,
        FileDeliveryTarget deliveryTarget,
        CompanyCredentials credentials
    ) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        Sunat sunat = new Sunat(this.wsClientFactory);
        SendFileResponse fileResponse = sunat.sendFile(zipFile, deliveryTarget, credentials);

        byte[] cdr = fileResponse.getCdr();
        String ticket = fileResponse.getTicket();

        CdrReader cdrReader = cdr != null ? new CdrReader(cdr) : null;
        return new XSenderFileResponse(cdrReader, ticket);
    }

    public XSenderTicketResponse ticket(
        String ticket,
        TicketDeliveryTarget deliveryTarget,
        CompanyCredentials credentials
    ) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        Sunat sunat = new Sunat(this.wsClientFactory);
        VerifyTicketResponse ticketResponse = sunat.verifyTicketStatus(ticket, deliveryTarget, credentials);

        byte[] cdr = ticketResponse.getCdr();
        String statusCode = ticketResponse.getStatusCode();

        return new XSenderTicketResponse(new CdrReader(cdr), statusCode);
    }
}
