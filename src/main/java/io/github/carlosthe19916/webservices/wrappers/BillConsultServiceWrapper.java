package io.github.carlosthe19916.webservices.wrappers;

public class BillConsultServiceWrapper {

    private BillConsultServiceWrapper() {
        // Just static methods
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatus(
            ServiceConfig config,
            String ruc,
            String tipo,
            String serie,
            int numero) {

        service.sunat.gob.pe.billconsultservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billconsultservice.BillService.class, config);
        return billService.getStatus(ruc, tipo, serie, numero);
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatusCdr(
            ServiceConfig config,
            String ruc,
            String tipo,
            String serie,
            int numero) {

        service.sunat.gob.pe.billconsultservice.BillService billService = SunatServiceFactory.getInstance(service.sunat.gob.pe.billconsultservice.BillService.class, config);
        return billService.getStatusCdr(ruc, tipo, serie, numero);
    }

}
