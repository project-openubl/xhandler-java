package io.github.carlosthe19916.webservices.wrappers;

import io.github.carlosthe19916.webservices.factories.ServiceFactory;
import service.sunat.gob.pe.billvalidservice.BillValidService;

public class BillValidServiceWrapper {

    private BillValidServiceWrapper() {
        // Just static methods
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse getStatus(ServiceConfig config, String nombre, String archivo) {
        BillValidService billValidService = ServiceFactory.getInstance(BillValidService.class, config);
        return billValidService.verificaCPEarchivo(nombre, archivo);
    }

    public static service.sunat.gob.pe.billvalidservice.StatusResponse validaCDPcriterios(ServiceConfig config,
                                                                                          String rucEmisor,
                                                                                          String tipoCDP,
                                                                                          String serieCDP,
                                                                                          String numeroCDP,
                                                                                          String tipoDocIdReceptor,
                                                                                          String numeroDocIdReceptor,
                                                                                          String fechaEmision,
                                                                                          double importeTotal,
                                                                                          String nroAutorizacion) {

        BillValidService billValidService = ServiceFactory.getInstance(BillValidService.class, config);
        return billValidService.validaCDPcriterios(rucEmisor, tipoCDP, serieCDP, numeroCDP, tipoDocIdReceptor, numeroDocIdReceptor, fechaEmision, importeTotal, nroAutorizacion);
    }

}
