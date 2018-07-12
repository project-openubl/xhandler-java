package io.github.carlosthe19916.webservices.cxf;

import io.github.carlosthe19916.webservices.CxfConfig;
import io.github.carlosthe19916.webservices.cxf.ServiceFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

public class BillServiceCPE {

    private BillServiceCPE() {
        // Just static methods
    }

    public static byte[] sendBill(CxfConfig config, String fileName, byte[] zipFile, String partyType) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.get(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, "application/zip");
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendBill(fileName, dataHandler, partyType);
    }

    public static service.sunat.gob.pe.billservice.StatusResponse getStatus(CxfConfig config, String ticket) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.get(service.sunat.gob.pe.billservice.BillService.class, config);
        return billService.getStatus(ticket);
    }

    public static String sendSummary(CxfConfig config, String fileName, byte[] zipFile, String partyType) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.get(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, "application/zip");
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendSummary(fileName, dataHandler, partyType);
    }

    public static String sendPack(CxfConfig config, String fileName, byte[] zipFile, String partyType) {
        service.sunat.gob.pe.billservice.BillService billService = ServiceFactory.get(service.sunat.gob.pe.billservice.BillService.class, config);
        DataSource dataSource = new ByteArrayDataSource(zipFile, "application/zip");
        DataHandler dataHandler = new DataHandler(dataSource);
        return billService.sendPack(fileName, dataHandler, partyType);
    }

}
