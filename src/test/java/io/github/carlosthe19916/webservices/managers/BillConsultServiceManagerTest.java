package io.github.carlosthe19916.webservices.managers;


import io.github.carlosthe19916.webservices.models.BillConsultBean;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.ws.soap.SOAPFaultException;
import java.io.IOException;

public class BillConsultServiceManagerTest {

    private String USERNAME = "20494637074MODDATOS";
    private String PASSWORD = "MODDATOS";
    private String URL_CONSULTA = "https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";

    @Test
    public void getStatus() throws IOException {
        ServiceConfig config = new ServiceConfig.Builder()
                .url(URL_CONSULTA)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        BillConsultBean consult = new BillConsultBean.Builder()
                .ruc("20494918910")
                .tipo("01")
                .serie("F001")
                .numero(102)
                .build();

        try {
            service.sunat.gob.pe.billconsultservice.StatusResponse response = BillConsultServiceManager.getStatus(consult, config);
        } catch (SOAPFaultException e) {
            // Las consultas deben de hacerse con un usuario y constraseña de produccion.
            Assert.assertEquals(e.getMessage(), "El Usuario ingresado no existe");
        }
    }

    @Test
    public void getStatusCdr() throws IOException {
        ServiceConfig config = new ServiceConfig.Builder()
                .url(URL_CONSULTA)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        BillConsultBean consult = new BillConsultBean.Builder()
                .ruc("20494918910")
                .tipo("01")
                .serie("F001")
                .numero(102)
                .build();

        try {
            service.sunat.gob.pe.billconsultservice.StatusResponse response = BillConsultServiceManager.getStatusCdr(consult, config);
        } catch (SOAPFaultException e) {
            // Las consultas deben de hacerse con un usuario y constraseña de produccion.
            Assert.assertEquals(e.getMessage(), "El Usuario ingresado no existe");
        }
    }

}
