package io.github.carlosthe19916.webservices;

import io.github.carlosthe19916.webservices.cxf.BillServiceManager;
import io.github.carlosthe19916.webservices.cxf.ServiceConfig;
import jodd.io.ZipBuilder;
import service.sunat.gob.pe.billservice.StatusResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BillServiceSender {

    private BillServiceSender() {
        // Just static methods
    }

    /**
     * @param file     archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static byte[] sendBill(File file, String url, String username, String password) throws IOException {
        return sendBill(file.toPath(), url, username, password);
    }

    /**
     * @param path     ubicacion del archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static byte[] sendBill(Path path, String url, String username, String password) throws IOException {
        return sendBill(path.getFileName().toString(), Files.readAllBytes(path), url, username, password);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static byte[] sendBill(String fileName, byte[] file, String url, String username, String password) throws IOException {
        if (fileName.endsWith(".xml")) {
            file = ZipBuilder.createZipInMemory()
                    .add(file)
                    .path(fileName)
                    .save()
                    .toBytes();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        }

        ServiceConfig config = new ServiceConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceManager.sendBill(config, fileName, file, null);
    }

    /**
     * @param ticket   numero de ticket a ser consultado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static StatusResponse getStatus(String ticket, String url, String username, String password) {
        ServiceConfig config = new ServiceConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceManager.getStatus(config, ticket);
    }

    /**
     * @param file     archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static String sendSummary(File file, String url, String username, String password) throws IOException {
        return sendSummary(file.toPath(), url, username, password);
    }

    /**
     * @param path     ubicacion del archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static String sendSummary(Path path, String url, String username, String password) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), url, username, password);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static String sendSummary(String fileName, byte[] file, String url, String username, String password) throws IOException {
        if (fileName.endsWith(".xml")) {
            file = ZipBuilder.createZipInMemory()
                    .add(file)
                    .path(fileName)
                    .save()
                    .toBytes();
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        }

        ServiceConfig config = new ServiceConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceManager.sendSummary(config, fileName, file, null);
    }

    /**
     * @param file     archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static String sendPack(File file, String url, String username, String password) throws IOException {
        return sendPack(file.toPath(), url, username, password);
    }

    /**
     * @param path     ubicacion del archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static String sendPack(Path path, String url, String username, String password) throws IOException {
        return sendPack(path.getFileName().toString(), Files.readAllBytes(path), url, username, password);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. El archivo deberá ser un archivo .zip
     * @param file     archivo a ser enviado
     * @param url      endpoint al cual será enviado el archivo
     * @param username usuario SUNAT conformado por: RUC + USUARIO SOL
     * @param password password de la clave sol
     */
    public static String sendPack(String fileName, byte[] file, String url, String username, String password) {
        ServiceConfig config = new ServiceConfig.Builder()
                .url(url)
                .username(username)
                .passwod(password)
                .build();
        return BillServiceManager.sendPack(config, fileName, file, null);
    }

}
