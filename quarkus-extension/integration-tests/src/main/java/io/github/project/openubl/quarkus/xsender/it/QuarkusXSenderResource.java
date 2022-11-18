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

import io.github.project.openubl.xsender.XSender;
import io.github.project.openubl.xsender.XSenderFileResponse;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.discovery.XMLFileAnalyzer;
import java.io.InputStream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/quarkus-xsender")
@ApplicationScoped
public class QuarkusXSenderResource {

    @Inject
    XSender xsender;

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
            .getResourceAsStream("templates/12345678912-RA-20200328-1.xml");

        XMLFileAnalyzer xmlFileAnalyzer = new XMLFileAnalyzer(is, companyURLs);
        XSenderFileResponse response = xsender.sendXmlFile(
            xmlFileAnalyzer.getZipFile(),
            xmlFileAnalyzer.getFileDeliveryTarget(),
            credentials
        );

        return response.getTicket();
    }
}
