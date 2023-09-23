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
package io.github.project.openubl.springboot.xsender.it;

import io.github.project.openubl.xsender.Constants;
import io.github.project.openubl.xsender.camel.utils.CamelData;
import io.github.project.openubl.xsender.camel.utils.CamelUtils;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.BillServiceXMLFileAnalyzer;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.sunat.BillConsultServiceDestination;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import io.github.project.openubl.xsender.sunat.BillValidServiceDestination;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

import static io.github.project.openubl.xsender.camel.utils.CamelUtils.getBillConsultService;
import static io.github.project.openubl.xsender.camel.utils.CamelUtils.getBillValidService;

@RestController
public class SpringBootXSenderResource {

    @Autowired
    private ProducerTemplate producerTemplate;

    CompanyURLs companyURLs = CompanyURLs
            .builder()
            .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
            .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-guia-gem-beta/billService")
            .despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")
            .build();

    CompanyCredentials credentials = CompanyCredentials
            .builder()
            .username("12345678959MODDATOS")
            .password("MODDATOS")
            .token("myToken")
            .build();

    @PostMapping("/springboot-xsender/bill-service/send-invoice")
    public String sendInvoice() throws Exception {
        InputStream is = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/12345678912-01-F001-1.xml");

        BillServiceXMLFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(is, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = CamelUtils.getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = producerTemplate
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        return sunatResponse.getStatus().toString();
    }

    @PostMapping("/springboot-xsender/bill-service/send-voided-document")
    public String sendVoidedDocument() throws Exception {
        InputStream is = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/12345678912-RA-20200328-1.xml");

        BillServiceXMLFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(is, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = CamelUtils.getBillServiceCamelData(zipFile, destination, credentials);

        SunatResponse sunatResponse = producerTemplate
                .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);

        return sunatResponse.getSunat().getTicket();
    }

    @PostMapping("/springboot-xsender/consult-service/get-status")
    public String consultService_getStatus() throws Exception {
        BillConsultServiceDestination destination = BillConsultServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .operation(BillConsultServiceDestination.Operation.GET_STATUS)
                .build();

        CamelData camelData = getBillConsultService(
                "20494918910",
                "01",
                "F001",
                102,
                destination,
                credentials
        );

        try {
            service.sunat.gob.pe.billconsultservice.StatusResponse sunatResponse = producerTemplate
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_CONSULT_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billconsultservice.StatusResponse.class);
        } catch (CamelExecutionException e) {
            return e.getCause().getMessage();
        }

        throw new IllegalStateException("Excepcion no atrapada");
    }

    @PostMapping("/springboot-xsender/consult-service/get-status-cdr")
    public String consultService_getStatusCdr() throws Exception {
        BillConsultServiceDestination destination = BillConsultServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .operation(BillConsultServiceDestination.Operation.GET_STATUS_CDR)
                .build();

        CamelData camelData = getBillConsultService(
                "20494918910",
                "01",
                "F001",
                102,
                destination,
                credentials
        );

        try {
            service.sunat.gob.pe.billconsultservice.StatusResponse sunatResponse = producerTemplate
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_CONSULT_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billconsultservice.StatusResponse.class);
        } catch (CamelExecutionException e) {
            return e.getCause().getMessage();
        }

        throw new IllegalStateException("Excepcion no atrapada");
    }

    @PostMapping("/springboot-xsender/bill-service/send-despatch-advice")
    public String sendDespatchAdvice() throws Exception {
        InputStream is = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/20000000001-31-VVV1-1.xml");

        BillServiceXMLFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(is, companyURLs);

        ZipFile zipFile = fileAnalyzer.getZipFile();
        BillServiceDestination destination = fileAnalyzer.getSendFileDestination();
        CamelData camelData = CamelUtils.getBillServiceCamelData(zipFile, destination, credentials);

        try {
            SunatResponse sunatResponse = producerTemplate
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), SunatResponse.class);
        } catch (CamelExecutionException e) {
            return e.getCause().getMessage();
        }

        throw new IllegalStateException("Excepcion no atrapada");
    }

    @PostMapping("/springboot-xsender/consult-valid-service/validate-data")
    public String consultValidService_validateData() throws Exception {
        BillValidServiceDestination destination = BillValidServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .build();

        CamelData camelData = getBillValidService(
                "20494918910",
                "01",
                "F001",
                "102",
                "06",
                "12345678",
                "01-12-2022",
                120.5,
                "",
                destination,
                credentials
        );

        try {
            service.sunat.gob.pe.billvalidservice.StatusResponse sunatResponse = producerTemplate
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_VALID_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billvalidservice.StatusResponse.class);
        } catch (CamelExecutionException e) {
            return e.getCause().getMessage();
        }

        throw new IllegalStateException("Excepcion no atrapada");
    }

    @PostMapping("/springboot-xsender/consult-valid-service/validate-file")
    public String consultValidService_validateFile() throws Exception {
        String fileName = "12345678912-01-F001-1.xml";
        byte[] fileContent = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream("xmls/" + fileName)
                .readAllBytes();

        BillValidServiceDestination destination = BillValidServiceDestination.builder()
                .url("https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService")
                .build();

        CamelData camelData = getBillValidService(
                fileName,
                fileContent,
                destination,
                credentials
        );

        try {
            service.sunat.gob.pe.billvalidservice.StatusResponse sunatResponse = producerTemplate
                    .requestBodyAndHeaders(Constants.XSENDER_BILL_VALID_SERVICE_URI, camelData.getBody(), camelData.getHeaders(), service.sunat.gob.pe.billvalidservice.StatusResponse.class);
        } catch (CamelExecutionException e) {
            return e.getCause().getMessage();
        }

        throw new IllegalStateException("Excepcion no atrapada");
    }
}
