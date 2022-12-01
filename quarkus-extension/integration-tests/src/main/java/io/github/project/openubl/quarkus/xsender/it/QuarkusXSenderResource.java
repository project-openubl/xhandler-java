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
package io.github.project.openubl.quarkus.xsender.it;

import io.github.project.openubl.xsender.camel.utils.CamelData;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.FileDestination;
import io.github.project.openubl.xsender.files.XMLFileAnalyzer;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;
import org.apache.camel.ProducerTemplate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.InputStream;

import static io.github.project.openubl.xsender.camel.routes.CxfRouteBuilder.XSENDER_URI;
import static io.github.project.openubl.xsender.camel.utils.CamelUtils.getCamelData;

@Path("/quarkus-xsender")
@ApplicationScoped
public class QuarkusXSenderResource {

    @Inject
    ProducerTemplate producerTemplate;

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

    @GET
    @Path("invoice")
    public String sendInvoice() throws Exception {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("xmls/12345678912-01-F001-1.xml");

        XMLFileAnalyzer fileAnalyzer = new XMLFileAnalyzer(is, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = producerTemplate
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);


        return sunatResponse.getStatus().toString();
    }

    @GET
    @Path("voided-document")
    public String sendVoidedDocument() throws Exception {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("xmls/12345678912-RA-20200328-1.xml");

        XMLFileAnalyzer fileAnalyzer = new XMLFileAnalyzer(is, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        FileDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = getCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = producerTemplate
                .requestBodyAndHeaders(XSENDER_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        return sunatResponse.getSunat().getTicket();
    }
}
