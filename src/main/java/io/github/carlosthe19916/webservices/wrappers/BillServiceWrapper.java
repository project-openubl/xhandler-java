package io.github.carlosthe19916.webservices.wrappers;

import io.github.carlosthe19916.webservices.models.types.InternetMediaType;

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
