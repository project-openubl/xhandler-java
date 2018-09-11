package io.github.carlosthe19916.webservices.managers.errorhandler.codes;

import io.github.carlosthe19916.webservices.managers.BillConsultServiceManager;
import io.github.carlosthe19916.webservices.managers.errorhandler.AbstractErrorHandler;
import io.github.carlosthe19916.webservices.managers.errorhandler.BillServiceErrorHandler;
import io.github.carlosthe19916.webservices.models.BillConsultBean;
import io.github.carlosthe19916.webservices.models.BillServiceResult;
import io.github.carlosthe19916.webservices.models.types.ConsultaCdrResponseType;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillService1033ErrorHandler extends AbstractErrorHandler implements BillServiceErrorHandler {

    // [RUC]-[TIPO DOCUMENTO]-[SERIE]-[NUMERO]
    private final static Pattern FILENAME_STRUCTURE = Pattern.compile("(?:\\d{11}-)\\d{2}-[a-zA-Z_0-9]{4}-\\d{1,8}");

    @Override
    public BillServiceResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        Matcher matcher = FILENAME_STRUCTURE.matcher(getFileNameWithoutExtension(fileName));
        if (matcher.matches()) {
            BillConsultBean consult = new BillConsultBean.Builder().build();
            service.sunat.gob.pe.billconsultservice.StatusResponse response = BillConsultServiceManager.getStatusCdr(consult, config);

            Optional<ConsultaCdrResponseType> optional = ConsultaCdrResponseType.searchByCode(response.getStatusCode());
            if (optional.isPresent()) {
                switch (optional.get()) {
                    case EXISTE_Y_ACEPTADO:
                        return new BillServiceResult(
                                BillServiceResult.Status.ACEPTADO,
                                response.getContent(),
                                response.getStatusMessage()
                        );
                    case EXISTE_PERO_RECHAZADO:
                        return new BillServiceResult(
                                BillServiceResult.Status.RECHAZADO,
                                response.getContent(),
                                response.getStatusMessage()
                        );
                    case EXISTE_PERO_DADO_DE_BAJA:
                        return new BillServiceResult(
                                BillServiceResult.Status.BAJA,
                                response.getContent(),
                                response.getStatusMessage()
                        );
                }
            }
        }

        return null;
    }

    @Override
    public service.sunat.gob.pe.billservice.StatusResponse getStatus(String ticket, ServiceConfig config) {
        return null;
    }

    @Override
    public String sendSummary(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        return null;
    }

    @Override
    public String sendPack(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean test(SOAPFaultException e) {
        Integer errorCode = getErrorCode(e).orElse(-1);
        return errorCode == 1_033;
    }

}
