/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * <p>
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.eclipse.org/legal/epl-2.0/
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.quickstart.xbuilder.springboot;

import io.github.project.openubl.xsender.Constants;
import io.github.project.openubl.xsender.camel.utils.CamelData;
import io.github.project.openubl.xsender.camel.utils.CamelUtils;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.BillServiceFileAnalyzer;
import io.github.project.openubl.xsender.files.BillServiceXMLFileAnalyzer;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class XSenderController {

    @Autowired
    private CamelContext camelContext;

    CompanyURLs companyURLs = CompanyURLs.builder()
            .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
            .despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")
            .build();

    CompanyCredentials credentials = CompanyCredentials.builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .token("accessTokenParaGuiasDeRemision")
            .build();

    @PostMapping("/api/file/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();

        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(bytes, companyURLs);

        // Archivo ZIP
        ZipFile zipFile = fileAnalyzer.getZipFile();

        // Configuración para enviar xml y Configuración para consultar ticket
        BillServiceDestination fileDestination = fileAnalyzer.getSendFileDestination();
        BillServiceDestination ticketDestination = fileAnalyzer.getVerifyTicketDestination();

        // Send file
        CamelData camelData = CamelUtils.getBillServiceCamelData(zipFile, fileDestination, credentials);

        SunatResponse sendFileSunatResponse = camelContext.createProducerTemplate()
                .requestBodyAndHeaders(
                        Constants.XSENDER_BILL_SERVICE_URI,
                        camelData.getBody(),
                        camelData.getHeaders(),
                        SunatResponse.class
                );

        return fileAnalyzer.getXmlContent().getDocumentType() + " " + sendFileSunatResponse.getStatus();
    }

}
