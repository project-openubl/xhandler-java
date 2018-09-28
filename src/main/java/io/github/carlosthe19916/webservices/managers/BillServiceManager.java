package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.providers.BillServiceCallback;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.providers.BillServiceProvider;
import io.github.carlosthe19916.webservices.providers.DefaultBillServiceProvider;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BillServiceManager {

    static final long DEFAULT_DELAY = 5_000;

    private BillServiceManager() {
        // Just static methods
    }

    /**
     * @param file   archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendBill(File file, ServiceConfig config) throws IOException {
        return sendBill(file.toPath(), config);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendBill(Path path, ServiceConfig config) throws IOException {
        return sendBill(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) throws IOException {
        BillServiceProvider billServiceProvider = new DefaultBillServiceProvider();
        return billServiceProvider.sendBill(fileName, file, config);
    }

    /**
     * @param ticket numero de ticket a ser consultado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel getStatus(String ticket, ServiceConfig config) {
        BillServiceProvider billServiceProvider = new DefaultBillServiceProvider();
        return billServiceProvider.getStatus(ticket, config);
    }


    /**
     * @param file   archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendSummary(File file, ServiceConfig config) throws IOException {
        return sendSummary(file.toPath(), config);
    }

    public static BillServiceModel sendSummary(File file, ServiceConfig config, BillServiceCallback callback) throws IOException {
        return sendSummary(file.toPath(), config, callback, DEFAULT_DELAY);
    }

    public static BillServiceModel sendSummary(File file, ServiceConfig config, BillServiceCallback callback, long delay) throws IOException {
        return sendSummary(file.toPath(), config, callback, delay);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendSummary(Path path, ServiceConfig config) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config, null, -1);
    }

    public static BillServiceModel sendSummary(Path path, ServiceConfig config, BillServiceCallback callback) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config, callback, DEFAULT_DELAY);
    }

    public static BillServiceModel sendSummary(Path path, ServiceConfig config, BillServiceCallback callback, long delay) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config, callback, delay);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config, BillServiceCallback callback, long delay) throws IOException {
        BillServiceProvider billServiceProvider = new DefaultBillServiceProvider();

        BillServiceModel result;
        if (callback != null) {
            result = billServiceProvider.sendSummary(fileName, file, config, callback, delay);
        } else {
            result = billServiceProvider.sendSummary(fileName, file, config);
        }

        return result;
    }

    /**
     * @param file   archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendPack(File file, ServiceConfig config) throws IOException {
        return sendPack(file.toPath(), config);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendPack(Path path, ServiceConfig config) throws IOException {
        return sendPack(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. El archivo deberá ser un archivo .zip
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendPack(String fileName, byte[] file, ServiceConfig config) {
        BillServiceProvider billServiceProvider = new DefaultBillServiceProvider();
        return billServiceProvider.sendPack(fileName, file, config);
    }

}
