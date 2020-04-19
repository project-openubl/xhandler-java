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

package io.github.projectopenubl.xmlsenderlib.webservices.managers;

import io.github.projectopenubl.xmlsenderlib.webservices.models.BillValidModel;
import io.github.projectopenubl.xmlsenderlib.webservices.wrappers.BillValidServiceWrapper;
import io.github.projectopenubl.xmlsenderlib.webservices.wrappers.ServiceConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class BillValidServiceManager {

    private BillValidServiceManager() {
        // Just static methods
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse getStatus(File file, ServiceConfig config) throws IOException {
        return getStatus(file.toPath(), config);
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse getStatus(Path path, ServiceConfig config) throws IOException {
        return getStatus(path.getFileName().toString(), Files.readAllBytes(path), config);
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse getStatus(String fileName, byte[] file, ServiceConfig config) throws IOException {
        byte[] encode = Base64.getEncoder().encode(file);
        String base64Encoded = new String(encode);

        return BillValidServiceWrapper.getStatus(config, fileName, base64Encoded);
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse validaCDPcriterios(BillValidModel bean, ServiceConfig config) {
        return BillValidServiceWrapper.validaCDPcriterios(config,
                bean.getRucEmisor(),
                bean.getTipoCDP(),
                bean.getSerieCDP(),
                bean.getNumeroCDP(),
                bean.getTipoDocIdReceptor(),
                bean.getNumeroDocIdReceptor(),
                bean.getFechaEmision(),
                bean.getImporteTotal(),
                bean.getNroAutorizacion());
    }
}
