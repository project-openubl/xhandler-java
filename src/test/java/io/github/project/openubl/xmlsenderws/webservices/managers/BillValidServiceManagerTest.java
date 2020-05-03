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

import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import org.junit.jupiter.api.Test;
import service.sunat.gob.pe.billvalidservice.StatusResponse;

import javax.xml.ws.soap.SOAPFaultException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BillValidServiceManagerTest {

    private String USERNAME = "20494637074MODDATOS";
    private String PASSWORD = "MODDATOS";
    private String URL_CONSULTA = "https://e-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService";

    @Test
    public void getStatus() throws IOException, URISyntaxException {
        final String FILE_NAME = "F001-00005954.xml";
        File file = Paths.get(getClass().getResource("/ubl/" + FILE_NAME).toURI()).toFile();

        ServiceConfig config = new ServiceConfig.Builder()
                .url(URL_CONSULTA)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        boolean exceptionWasThrew = false;

        try {
            StatusResponse status = BillValidServiceManager.getStatus(file, config);
            assertNotEquals(status.getStatusCode(), "0000");
        } catch (SOAPFaultException e) {
            // Las consultas deben de hacerse con un usuario y constraseña de produccion.
            exceptionWasThrew = true;
        }

        assertTrue(exceptionWasThrew);
    }
}
