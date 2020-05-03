/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xmlsenderws.webservices.managers;

import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

    /**
     * Should send invoice
     * {@link BillServiceManager#sendBill(File, ServiceConfig)}
     */
    @Test
    public void test_sendBill_invoice() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-01-F001-00000001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        BillServiceModel result = BillServiceManager.sendBill(file, BOLETA_FACTURA_SERVICE_CONFIG);

        assertNotNull(result);
        assertNotNull(result.getCdr());
        assertEquals(Integer.valueOf(0), result.getCode());
        assertEquals("La Factura numero F001-00000001, ha sido aceptada", result.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
    }

    /**
     * Should send retention
     * {@link BillServiceManager#sendBill(File, ServiceConfig)}
     */
    @Test
    public void test_sendBill_retention() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-20-R001-00000001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        BillServiceModel result = BillServiceManager.sendBill(file, RETENTION_SERVICE_CONFIG);

        assertNotNull(result);
        assertNotNull(result.getCdr());
        assertEquals(Integer.valueOf(0), result.getCode());
        assertEquals("El Comprobante numero R001-00000001 ha sido aceptado", result.getDescription());
        assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
    }

    /**
     * Should Should handle exception
     * {@link BillServiceManager#sendBill(File, ServiceConfig)}
     */
    @Test
    public void test_sendBill_handleGeneric1036Exception() throws IOException, URISyntaxException {
        final String FILE_NAME = "20494637074-01-F001-00000003.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        BillServiceModel result = BillServiceManager.sendBill(file, BOLETA_FACTURA_SERVICE_CONFIG);

        assertNotNull(result);
        assertNull(result.getCdr());
        assertEquals(Integer.valueOf(1_036), result.getCode());
        assertEquals("Número de documento en el nombre del archivo no coincide con el consignado en el contenido del XML", result.getDescription());
        assertEquals(BillServiceModel.Status.EXCEPCION, result.getStatus());
    }

//    /**
//     * Should handle exception
//     * {@link io.github.carlosthe19916.webservices.managers.BillServiceManager#sendBill(File, ServiceConfig)}
//     */
//    @Test
//    public void test_sendBill_handleMocked2335Exception() throws IOException, URISyntaxException {
//        final String FILE_NAME = "20494637074-01-F001-00000002.xml";
//        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();
//
//        BillServiceModel result = BillServiceManager.sendBill(file, BOLETA_FACTURA_SERVICE_CONFIG);
//
//        assertNotNull(result);
//        assertNull(result.getCdr());
//        assertEquals(Integer.valueOf(2_335), result.getCode());
//        assertEquals("My mock message", result.getDescription());
//        assertEquals(BillServiceModel.Status.RECHAZADO, result.getStatusWrapper());
//    }

    /**
     * Should handle exception
     * {@link BillServiceManager#sendBill(File, ServiceConfig)}
     */
    @Test
    public void test_sendBill_handleGeneric2033Rechazo() throws IOException, URISyntaxException {
        final String FILE_NAME = "20603233591-03-B007-00000049.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        BillServiceModel result = BillServiceManager.sendBill(file, BOLETA_FACTURA_SERVICE_CONFIG);

        assertNotNull(result);
        assertNull(result.getCdr());
        assertEquals(Integer.valueOf(2_033), result.getCode());
        assertEquals("El dato ingresado en TaxAmount de lalíneano cumple con el formato establecido", result.getDescription());
        assertEquals(BillServiceModel.Status.RECHAZADO, result.getStatus());
    }

    /**
     * Should obtain tickets
     * {@link BillServiceManager#getStatus(String, ServiceConfig)}
     */
    @Test
    public void test_getStatus_ticket() throws URISyntaxException, IOException, InterruptedException {
        final String FILE_NAME = "20494637074-RA-20180316-00001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        BillServiceModel ticketResult = BillServiceManager.sendSummary(file, BOLETA_FACTURA_SERVICE_CONFIG);
        String ticket = ticketResult.getTicket();
        assertNotNull(ticket);
        Thread.sleep(1_000);


        BillServiceModel result = BillServiceManager.getStatus(ticket, BOLETA_FACTURA_SERVICE_CONFIG);
        assertNotNull(result);
        assertNotNull(result.getCdr());
        assertEquals(ticket, result.getTicket());
        assertEquals(Integer.valueOf(0), result.getCode());
        assertEquals(BillServiceModel.Status.ACEPTADO, result.getStatus());
        assertEquals("La Comunicacion de baja RA-20180316-00001, ha sido aceptada", result.getDescription());
    }

    /**
     * Should send voided document
     * {@link BillServiceManager#sendSummary(File, ServiceConfig)}
     */
    @Test
    public void test_sendSummary_voidedInvoice() throws IOException, URISyntaxException, InterruptedException {
        final String FILE_NAME = "20494637074-RA-20180316-00001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        BillServiceModel result = BillServiceManager.sendSummary(file, BOLETA_FACTURA_SERVICE_CONFIG);

        assertNotNull(result);
        assertNotNull(result.getTicket());
    }

    /**
     * Should send voided document
     * {@link BillServiceManager#sendSummary(File, ServiceConfig)}
     */
    @Test
    public void test_sendSummary_voidedRetention() throws IOException, URISyntaxException {
        final String FILE_NAME = "20603233591-RR-20180713-00001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        BillServiceModel result = BillServiceManager.sendSummary(file, RETENTION_SERVICE_CONFIG);

        assertNotNull(result);
        assertNotNull(result.getTicket());
    }

    /**
     * Should send voided document
     * {@link BillServiceManager#sendSummary(File, ServiceConfig)}
     */
    @Test
    public void test_sendSummary_andCallback() throws IOException, URISyntaxException, InterruptedException {
        final String FILE_NAME = "20494637074-RA-20180316-00001.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        MockBillServiceCallback callback = new MockBillServiceCallback();
        MockBillServiceCallback.StatusWrapper statusWrapper = callback.getStatusWrapper();

        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");

        BillServiceModel result = BillServiceManager.sendSummary(file, BOLETA_FACTURA_SERVICE_CONFIG, params, callback, 500);

        Thread.sleep(3_000);

        assertNotNull(result);
        assertNotNull(result.getTicket());

        assertEquals(BillServiceModel.Status.ACEPTADO, statusWrapper.getStatus());
        assertNotNull(statusWrapper.getCdr());
        assertEquals(Integer.valueOf(0), statusWrapper.getCode());
        assertEquals("La Comunicacion de baja RA-20180316-00001, ha sido aceptada", statusWrapper.getDescription());
        assertEquals(params, statusWrapper.getParams());
    }

}
