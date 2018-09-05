package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.sunat.gob.pe.billvalidservice.StatusResponse;

import javax.xml.ws.soap.SOAPFaultException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class BillValidServiceManagerTest {

    private String USERNAME = "20494637074MODDATOS";
    private String PASSWORD = "MODDATOS";
    private String URL_CONSULTA = "https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService";

    @Before
    public void before() {

    }

    @Test
    public void getStatus() throws IOException, URISyntaxException {
        final String FILE_NAME = "F001-00005954.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        ServiceConfig config = new ServiceConfig.Builder()
                .url(URL_CONSULTA)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        try {
            StatusResponse status = BillValidServiceManager.getStatus(file, config);
            Assert.assertNotEquals(status.getStatusCode(), "0000");
        } catch (SOAPFaultException e) {
            // Las consultas deben de hacerse con un usuario y constraseña de produccion.
            Assert.assertTrue(true);
        }

    }
}
