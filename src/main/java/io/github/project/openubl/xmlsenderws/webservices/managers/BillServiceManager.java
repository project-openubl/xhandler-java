/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xmlsenderws.webservices.managers;

import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceCallback;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceProvider;
import io.github.project.openubl.xmlsenderws.webservices.providers.DefaultBillServiceProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

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

    public static BillServiceModel sendSummary(File file, ServiceConfig config, Map<String, Object> params, BillServiceCallback callback, long delay) throws IOException {
        return sendSummary(file.toPath(), config, params, callback, delay);
    }

    /**
     * @param path   ubicacion del archivo a ser enviado
     * @param config Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendSummary(Path path, ServiceConfig config) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config, Collections.emptyMap(),null, -1);
    }

    public static BillServiceModel sendSummary(Path path, ServiceConfig config, Map<String, Object> params, BillServiceCallback callback, long delay) throws IOException {
        return sendSummary(path.getFileName().toString(), Files.readAllBytes(path), config, params, callback, delay);
    }

    /**
     * @param fileName nombre del archivo a ser enviado. Si el archivo es un xml, este sera comprimido en un archivo .zip automaticamente
     * @param file     archivo a ser enviado
     * @param config   Credenciales y URL de destino de la petición
     */
    public static BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config) throws IOException {
        return sendSummary(fileName, file, config, Collections.emptyMap(),null, -1);
    }

    public static BillServiceModel sendSummary(String fileName, byte[] file, ServiceConfig config, Map<String, Object> params, BillServiceCallback callback, long delay) throws IOException {
        BillServiceProvider billServiceProvider = new DefaultBillServiceProvider();

        BillServiceModel result;
        if (callback != null) {
            result = billServiceProvider.sendSummary(fileName, file, config, params, callback, delay);
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
