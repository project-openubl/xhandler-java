package io.github.carlosthe19916.webservices;


import org.junit.Assert;
import org.junit.Test;
import service.sunat.gob.pe.billservice.StatusResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BillServiceFacadeTest {

    private String USERNAME = "20494637074MODDATOS";
    private String PASSWORD = "MODDATOS";
    private String URL = "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService";

    @Test
    public void sendBillBytesXml() throws IOException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        InputStream is = getClass().getResourceAsStream("/ubl/" + FILE_NAME);

        byte[] bytes = new byte[is.available()];
        int read = is.read(bytes);

        byte[] result = BillServiceFacade.sendBill(FILE_NAME, bytes, URL, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void sendBillBytesZip() throws IOException {
        final String FILE_NAME = "20494637074-01-F001-00000001.zip";
        InputStream is = getClass().getResourceAsStream("/ubl/" + FILE_NAME);

        byte[] bytes = new byte[is.available()];
        int read = is.read(bytes);

        byte[] result = BillServiceFacade.sendBill(FILE_NAME, bytes, URL, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void sendBillPath() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        java.net.URL url = getClass().getResource("/ubl/" + FILE_NAME);
        Path path = Paths.get(url.toURI());

        byte[] result = BillServiceFacade.sendBill(path, URL, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void sendBillFile() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        java.net.URL url = getClass().getResource("/ubl/" + FILE_NAME);
        Path path = Paths.get(url.toURI());
        File file = path.toFile();

        byte[] result = BillServiceFacade.sendBill(file, URL, USERNAME, PASSWORD);
        Assert.assertNotNull(result);
    }

    @Test
    public void getStatus() throws IOException, URISyntaxException {
        final String TICKET = "1529342625179";

        StatusResponse status = BillServiceFacade.getStatus(TICKET, URL, USERNAME, PASSWORD);
        Assert.assertNotNull(status);
    }

}
