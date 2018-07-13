package io.github.carlosthe19916.webservices.cxf;

import io.github.carlosthe19916.webservices.cxf.ws.ServiceFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

public class BillServiceManager {

    private BillServiceManager() {
        // Just static methods
    }

    public static byte[] sendBill(ServiceConfig config, String fileName, byte[] zipFile, String partyType) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, "application/zip");
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendBill(fileName, dataHandler, partyType);
    }

    public static service.sunat.gob.pe.billservice.StatusResponse getStatus(ServiceConfig config, String ticket) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        return billService.getStatus(ticket);
    }

    public static String sendSummary(ServiceConfig config, String fileName, byte[] zipFile, String partyType) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, "application/zip");
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendSummary(fileName, dataHandler, partyType);
    }

    public static String sendPack(ServiceConfig config, String fileName, byte[] zipFile, String partyType) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.getInstance(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, "application/zip");
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendPack(fileName, dataHandler, partyType);
    }

}
