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
package io.github.project.openubl.xmlsenderws.webservices.wrappers;

public class BillConsultServiceWrapper {

    private BillConsultServiceWrapper() {
        // Just static methods
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatus(
            ServiceConfig config,
            String ruc,
            String tipo,
            String serie,
            int numero) {

        service.sunat.gob.pe.billconsultservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billconsultservice.BillService.class, config);
        return billService.getStatus(ruc, tipo, serie, numero);
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatusCdr(
            ServiceConfig config,
            String ruc,
            String tipo,
            String serie,
            int numero) {

        service.sunat.gob.pe.billconsultservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billconsultservice.BillService.class, config);
        return billService.getStatusCdr(ruc, tipo, serie, numero);
    }

}
