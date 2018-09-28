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

package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.models.BillConsultModel;
import io.github.carlosthe19916.webservices.wrappers.BillConsultServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public class BillConsultServiceManager {

    private BillConsultServiceManager() {
        // Just static methods
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatus(BillConsultModel consulta, ServiceConfig config) {
        return BillConsultServiceWrapper.getStatus(config, consulta.getRuc(), consulta.getTipo(), consulta.getSerie(), consulta.getNumero());
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatusCdr(BillConsultModel consulta, ServiceConfig config) {
        return BillConsultServiceWrapper.getStatusCdr(config, consulta.getRuc(), consulta.getTipo(), consulta.getSerie(), consulta.getNumero());
    }

}
