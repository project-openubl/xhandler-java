package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.wrappers.BillValidServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

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

    public static service.sunat.gob.pe.billvalidservice.StatusResponse validaCDPcriterios(BillValidBean bean, ServiceConfig config) {
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
