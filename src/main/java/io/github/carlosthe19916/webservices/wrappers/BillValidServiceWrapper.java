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

package io.github.carlosthe19916.webservices.wrappers;

import service.sunat.gob.pe.billvalidservice.BillValidService;

public class BillValidServiceWrapper {

    private BillValidServiceWrapper() {
        // Just static methods
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse getStatus(ServiceConfig config, String nombre, String archivo) {
        BillValidService billValidService = SunatServiceFactory.getInstance(BillValidService.class, config);
        return billValidService.verificaCPEarchivo(nombre, archivo);
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse validaCDPcriterios(ServiceConfig config,
                                                                                          String rucEmisor,
                                                                                          String tipoCDP,
                                                                                          String serieCDP,
                                                                                          String numeroCDP,
                                                                                          String tipoDocIdReceptor,
                                                                                          String numeroDocIdReceptor,
                                                                                          String fechaEmision,
                                                                                          double importeTotal,
                                                                                          String nroAutorizacion) {

        BillValidService billValidService = SunatServiceFactory.getInstance(BillValidService.class, config);
        return billValidService.validaCDPcriterios(rucEmisor, tipoCDP, serieCDP, numeroCDP, tipoDocIdReceptor, numeroDocIdReceptor, fechaEmision, importeTotal, nroAutorizacion);
    }

}
