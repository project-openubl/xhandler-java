package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.models.DocumentStatusResult;
import io.github.carlosthe19916.webservices.models.SendSummaryResult;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.junit.Assert;
import org.junit.Test;
import service.sunat.gob.pe.billservice.StatusResponse;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class BillServiceManagerTest {

    private String USERNAME = "20494637074MODDATOS";
    private String PASSWORD = "MODDATOS";
    private String URL_BOLETA_FACTURA = "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService";
    private String URL_RETENCION = "https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService";

    private ServiceConfig BOLETA_FACTURA_SERVICE_CONFIG = new ServiceConfig.Builder()
            .url(URL_BOLETA_FACTURA)
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    private ServiceConfig RETENTION_SERVICE_CONFIG = new ServiceConfig.Builder()
            .url(URL_RETENCION)
            .username(USERNAME)
            .password(PASSWORD)
            .build();

    @Test
    public void sendBillInvoice() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        DocumentStatusResult statusResult = BillServiceManager.sendBill(file, BOLETA_FACTURA_SERVICE_CONFIG);
        Assert.assertNotNull(statusResult);
    }

    @Test
    public void sendBillRetencion() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-20-R001-00000001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        DocumentStatusResult result = BillServiceManager.sendBill(file, RETENTION_SERVICE_CONFIG);
        Assert.assertNotNull(result);
    }

    @Test
    public void getStatus() {
        final String TICKET = "1529342625179";

        DocumentStatusResult statusResult = BillServiceManager.getStatus(TICKET, BOLETA_FACTURA_SERVICE_CONFIG);
        Assert.assertNotNull(statusResult);
    }

    @Test
    public void sendInvoiceVoidedDocumentXml() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-RA-20180316-00001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        SendSummaryResult sendSummaryResult = BillServiceManager.sendSummary(file, BOLETA_FACTURA_SERVICE_CONFIG);
        Assert.assertNotNull(sendSummaryResult);
    }

    @Test
    public void sendRetencionVoidedDocumentXml() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-RR-20180713-00001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        SendSummaryResult sendSummaryResult = BillServiceManager.sendSummary(file, RETENTION_SERVICE_CONFIG);
        Assert.assertNotNull(sendSummaryResult);
    }

}
