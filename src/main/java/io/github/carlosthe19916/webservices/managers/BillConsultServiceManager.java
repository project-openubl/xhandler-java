package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.models.BillConsultBean;
import io.github.carlosthe19916.webservices.wrappers.BillConsultServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

public class BillConsultServiceManager {

    private BillConsultServiceManager() {
        // Just static methods
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatus(BillConsultBean consulta, ServiceConfig config) {
        return BillConsultServiceWrapper.getStatus(config, consulta.getRuc(), consulta.getTipo(), consulta.getSerie(), consulta.getNumero());
    }

    public static service.sunat.gob.pe.billconsultservice.StatusResponse getStatusCdr(BillConsultBean consulta, ServiceConfig config) {
        return BillConsultServiceWrapper.getStatusCdr(config, consulta.getRuc(), consulta.getTipo(), consulta.getSerie(), consulta.getNumero());
    }

}
