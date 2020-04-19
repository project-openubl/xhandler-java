/*
 * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.projectopenubl.xmlsenderlib.webservices.managers;


import io.github.projectopenubl.xmlsenderlib.webservices.models.BillConsultModel;
import io.github.projectopenubl.xmlsenderlib.webservices.wrappers.ServiceConfig;
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

        BillConsultModel consult = new BillConsultModel.Builder()
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

        BillConsultModel consult = new BillConsultModel.Builder()
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
