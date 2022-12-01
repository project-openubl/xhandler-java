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
package io.github.project.openubl.xsender.camel.utils;

import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.files.FileDestination;
import io.github.project.openubl.xsender.files.TicketDestination;
import io.github.project.openubl.xsender.files.ZipFile;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.attachment.ByteDataSource;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CamelUtils {

    public static final String XMLNS_NS = "http://www.w3.org/2000/xmlns/";
    public static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    public static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    public static final String USERNAMETOKEN_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0";
    public static final String PASSWORD_TEXT = USERNAMETOKEN_NS + "#PasswordText";

    public static CamelData getCamelData(ZipFile zipFile, FileDestination destination, CompanyCredentials credentials) {
        List<Object> body = createFileBody(zipFile);
        Map<String, Object> headers = createHeaders(destination, credentials);

        return CamelData.builder()
                .body(body)
                .headers(headers)
                .build();
    }

    public static CamelData getCamelData(String ticket, TicketDestination destination, CompanyCredentials credentials) {
        String uri = destination.getOperation().getName();
        List<Object> body = createTicketBody(ticket);
        Map<String, Object> headers = createHeaders(destination, credentials);

        return CamelData.builder()
                .body(body)
                .headers(headers)
                .build();
    }

    private static List<Object> createFileBody(ZipFile zipFile) {
        DataSource dataSource = new ByteDataSource(zipFile.getFile(), "application/zip");
        DataHandler dataHandler = new DataHandler(dataSource);

        return Arrays.asList(zipFile.getFilename(), dataHandler, null);
    }

    private static List<Object> createTicketBody(String ticket) {
        return Collections.singletonList(ticket);
    }

    private static Map<String, Object> createHeaders(FileDestination destination, CompanyCredentials credentials) {
        return createHeaders(destination.getUrl(), destination.getOperation().getName(), credentials);
    }

    private static Map<String, Object> createHeaders(
            TicketDestination destination,
            CompanyCredentials credentials
    ) {
        return createHeaders(destination.getUrl(), destination.getOperation().getName(), credentials);
    }

    private static Map<String, Object> createHeaders(
            String destinationUrl,
            String operationName,
            CompanyCredentials credentials
    ) throws RuntimeException {
        QName security = new QName(WSSE_NS, "Security");

        Document xmlDocument = DOMUtils.createDocument();
        Element securityEl = xmlDocument.createElementNS(WSSE_NS, "wsse:Security");
        securityEl.setAttributeNS(XMLNS_NS, "xmlns:wsse", WSSE_NS);
        securityEl.setAttribute("xmlns:wsu", WSU_NS);

        Element usernameTokenEl = xmlDocument.createElementNS(WSSE_NS, "wsse:UsernameToken");
        securityEl.appendChild(usernameTokenEl);

        Element usernameEl = xmlDocument.createElementNS(WSSE_NS, "wsse:Username");
        usernameEl.setTextContent(credentials.getUsername());
        usernameTokenEl.appendChild(usernameEl);

        Element passwordEl = xmlDocument.createElementNS(WSSE_NS, "wsse:Password");
        passwordEl.setTextContent(credentials.getPassword());
        passwordEl.setAttribute("Type", PASSWORD_TEXT);
        usernameTokenEl.appendChild(passwordEl);

        SoapHeader securitySoapHeader = new SoapHeader(security, securityEl);

        return Map.of(
                Header.HEADER_LIST, List.of(securitySoapHeader),
                CxfConstants.OPERATION_NAME, operationName,
                CxfConstants.DESTINATION_OVERRIDE_URL, destinationUrl
        );
    }
}
