/*
 *
 *  * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 *  * and other contributors as indicated by the @author tags.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package io.github.carlosthe19916.webservices.wrappers;

import io.github.carlosthe19916.webservices.models.InternetMediaType;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

public class BillServiceWrapper {

    private BillServiceWrapper() {
        // Just static methods
    }

    public static byte[] sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        service.sunat.gob.pe.billservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, InternetMediaType.ZIP.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendBill(fileName, dataHandler, partyType);
    }

    public static service.sunat.gob.pe.billservice.StatusResponse getStatus(String ticket, ServiceConfig config) {
        service.sunat.gob.pe.billservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        return billService.getStatus(ticket);
    }

    public static String sendSummary(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        service.sunat.gob.pe.billservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, InternetMediaType.ZIP.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendSummary(fileName, dataHandler, partyType);
    }

    public static String sendPack(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        service.sunat.gob.pe.billservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, InternetMediaType.ZIP.getMimeType());
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendPack(fileName, dataHandler, partyType);
    }

}
