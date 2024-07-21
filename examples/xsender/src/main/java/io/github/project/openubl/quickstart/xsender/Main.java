package io.github.project.openubl.quickstart.xsender;

import io.github.project.openubl.xsender.Constants;
import io.github.project.openubl.xsender.camel.StandaloneCamel;
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

import java.io.File;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        CompanyURLs companyURLs = CompanyURLs.builder()
                .invoice("https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService")
                .perceptionRetention("https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService")
                .despatch("https://api-cpe.sunat.gob.pe/v1/contribuyente/gem")
                .build();

        CompanyCredentials credentials = CompanyCredentials.builder()
                .username("12345678959MODDATOS")
                .password("MODDATOS")
                .build();

        File xml = Paths.get(Main.class.getClassLoader().getResource("invoice.xml").toURI()).toFile();
        BillServiceFileAnalyzer fileAnalyzer = new BillServiceXMLFileAnalyzer(xml, companyURLs);

        // Archivo ZIP
        ZipFile zipFile = fileAnalyzer.getZipFile();

        // Configuración para enviar xml y Configuración para consultar ticket
        BillServiceDestination fileDestination = fileAnalyzer.getSendFileDestination();
        BillServiceDestination ticketDestination = fileAnalyzer.getVerifyTicketDestination();

        // Send file
        CamelContext camelContext = StandaloneCamel.getInstance()
                .getMainCamel()
                .getCamelContext();

        CamelData camelData = CamelUtils.getBillServiceCamelData(zipFile, fileDestination, credentials);

        SunatResponse sendFileSunatResponse = camelContext.createProducerTemplate()
                .requestBodyAndHeaders(
                        Constants.XSENDER_BILL_SERVICE_URI,
                        camelData.getBody(),
                        camelData.getHeaders(),
                        SunatResponse.class
                );

        System.out.println("SUNAT status: " + sendFileSunatResponse.getStatus());
    }
}
