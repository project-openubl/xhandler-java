package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.BillConsultServiceManager;
import io.github.carlosthe19916.webservices.models.BillConsultBean;
import io.github.carlosthe19916.webservices.models.BillServiceResult;
import io.github.carlosthe19916.webservices.models.types.ConsultaResponseType;
import io.github.carlosthe19916.webservices.utils.Util;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;

import javax.xml.ws.soap.SOAPFaultException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillService1033ErrorHandler extends AbstractBillServiceErrorHandler {

    private final static String DEFAULT_CONSULT_URL = "https://e-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";
    private final static Pattern FILENAME_STRUCTURE = Pattern.compile("(?:\\d{11}-)\\d{2}-[a-zA-Z_0-9]{4}-\\d{1,8}"); // [RUC]-[TIPO DOCUMENTO]-[SERIE]-[NUMERO]

    @Override
    public BillServiceResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        String fileNameWithoutExtension = Util.getFileNameWithoutExtension(fileName);

        Matcher matcher = FILENAME_STRUCTURE.matcher(fileNameWithoutExtension);
        if (matcher.matches()) {
            String[] split = fileNameWithoutExtension.split("-");

            BillConsultBean consult = new BillConsultBean.Builder()
                    .ruc(split[0])
                    .tipo(split[1])
                    .serie(split[2])
                    .numero(Integer.parseInt(split[3]))
                    .build();

            ServiceConfig consultServiceConfig = new ServiceConfig.Builder()
                    .url(DEFAULT_CONSULT_URL)
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .build();

            service.sunat.gob.pe.billconsultservice.StatusResponse response = BillConsultServiceManager.getStatus(consult, consultServiceConfig);

            Optional<ConsultaResponseType> optional = ConsultaResponseType.searchByCode(response.getStatusCode());
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
                    default:
                        return null;
                }
            }
        }

        return null;
    }

    @Override
    public boolean test(SOAPFaultException e) {
        Integer errorCode = Util.getErrorCode(e).orElse(-1);
        return errorCode == 1_033;
    }

    @Override
    public int getPriority() {
        return 1;
    }

}
