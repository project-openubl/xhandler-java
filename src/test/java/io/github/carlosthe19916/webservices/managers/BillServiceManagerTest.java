package io.github.carlosthe19916.webservices.managers;


import org.junit.Assert;
import org.junit.Test;
import service.sunat.gob.pe.billservice.StatusResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BillServiceManagerTest {

    private String USERNAME = "20494637074MODDATOS";
    private String PASSWORD = "MODDATOS";
    private String URL_BOLETA_FACTURA = "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService";
    private String URL_RETENCION = "https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService";

    @Test
    public void sendBillInvoiceXml() throws IOException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        InputStream is = getClass().getResourceAsStream("/ubl/" + FILE_NAME);

        byte[] bytes = new byte[is.available()];
        int read = is.read(bytes);

        byte[] result = BillServiceManager.sendBill(FILE_NAME, bytes, URL_BOLETA_FACTURA, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void sendBillInvoicePath() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        java.net.URL url = getClass().getResource("/ubl/" + FILE_NAME);
        Path path = Paths.get(url.toURI());

        byte[] result = BillServiceManager.sendBill(path, URL_BOLETA_FACTURA, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void sendBillInvoiceFile() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        java.net.URL url = getClass().getResource("/ubl/" + FILE_NAME);
        Path path = Paths.get(url.toURI());
        File file = path.toFile();

        byte[] result = BillServiceManager.sendBill(file, URL_BOLETA_FACTURA, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void sendBillRetencionXml() throws IOException {
        final String FILE_NAME = "20494637074-20-R001-00000001.xml";
        InputStream is = getClass().getResourceAsStream("/ubl/" + FILE_NAME);

        byte[] bytes = new byte[is.available()];
        int read = is.read(bytes);

        byte[] result = BillServiceManager.sendBill(FILE_NAME, bytes, URL_RETENCION, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void getStatus() {
        final String TICKET = "1529342625179";

        StatusResponse status = BillServiceManager.getStatus(TICKET, URL_BOLETA_FACTURA, USERNAME, PASSWORD);
        Assert.assertNotNull(status);
    }

    @Test
    public void sendInvoiceVoidedDocumentXml() throws IOException {
        final String FILE_NAME = "20494637074-RA-20180316-00001.xml";
        InputStream is = getClass().getResourceAsStream("/ubl/" + FILE_NAME);

        byte[] bytes = new byte[is.available()];
        int read = is.read(bytes);

        String ticket = BillServiceManager.sendSummary(FILE_NAME, bytes, URL_BOLETA_FACTURA, USERNAME, PASSWORD);
        Assert.assertNotNull(ticket);
    }

    @Test
    public void sendRetencionVoidedDocumentXml() throws IOException {
        final String FILE_NAME = "20494637074-RR-20180713-00001.xml";
        InputStream is = getClass().getResourceAsStream("/ubl/" + FILE_NAME);

        byte[] bytes = new byte[is.available()];
        int read = is.read(bytes);

        String ticket = BillServiceManager.sendSummary(FILE_NAME, bytes, URL_RETENCION, USERNAME, PASSWORD);
        Assert.assertNotNull(ticket);
    }

}
