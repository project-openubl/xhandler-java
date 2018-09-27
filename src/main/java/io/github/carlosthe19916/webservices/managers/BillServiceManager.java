package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.managers.providers.BillServiceProvider;
import io.github.carlosthe19916.webservices.managers.providers.DefaultBillServiceProvider;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BillServiceManager {

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

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendSummary(Path path, ServiceConfig config) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config) throws IOException {
        BillServiceProvider billServiceProvider = new DefaultBillServiceProvider();
        return billServiceProvider.sendSummary(fileName, file, config);
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
