package io.github.carlosthe19916.webservices;

import io.github.carlosthe19916.webservices.cxf.BillServiceCPE;
import jodd.io.ZipBuilder;
import service.sunat.gob.pe.billservice.StatusResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BillServiceFacade {

    private BillServiceFacade() {
        // Just static methods
    }

    public static byte[] sendBill(File file, String url, String username, String password) throws IOException {
        return sendBill(file.toPath(), url, username, password);
    }

    public static byte[] sendBill(Path path, String url, String username, String password) throws IOException {
        return sendBill(path.getFileName().toString(), Files.readAllBytes(path), url, username, password);
    }

    public static byte[] sendBill(String fileName, byte[] file, String url, String username, String password) throws IOException {
        if (fileName.endsWith(".xml")) {
            file = ZipBuilder.createZipInMemory()
                    .add(file)
                    .path(fileName)
                    .save()
                    .toBytes();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        }

        CxfConfig config = new CxfConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceCPE.sendBill(config, fileName, file, null);
    }

    public static StatusResponse getStatus(String ticket, String url, String username, String password) {
        CxfConfig config = new CxfConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceCPE.getStatus(config, ticket);
    }

    public static String sendSummary(String fileName, byte[] file, String url, String username, String password) {
        CxfConfig config = new CxfConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceCPE.sendSummary(config, fileName, file, null);
    }

    public static String sendPack(String fileName, byte[] file, String url, String username, String password) {
        CxfConfig config = new CxfConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceCPE.sendPack(config, fileName, file, null);
    }

}
