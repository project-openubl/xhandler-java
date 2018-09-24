package io.github.carlosthe19916.webservices.managers.errorhandler.billservice;

import io.github.carlosthe19916.webservices.managers.BillConsultServiceManager;
import io.github.carlosthe19916.webservices.managers.BillValidServiceManager;
import io.github.carlosthe19916.webservices.models.BillConsultBean;
import io.github.carlosthe19916.webservices.models.BillServiceResult;
import io.github.carlosthe19916.webservices.models.types.ConsultaResponseType;
import io.github.carlosthe19916.webservices.utils.CdrUtils;
import io.github.carlosthe19916.webservices.utils.Util;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billvalidservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillService1033ErrorHandler extends AbstractBillServiceErrorHandler {

    private final static String DEFAULT_BILL_CONSULT_URL = "https://exception-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";
    private final static String DEFAULT_BILL_VALID_URL = "https://exception-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService";
    private final static Pattern FILENAME_STRUCTURE = Pattern.compile("(?:\\d{11}-)\\d{2}-[a-zA-Z_0-9]{4}-\\d{1,8}"); // [RUC]-[TIPO DOCUMENTO]-[SERIE]-[NUMERO]

    private final SOAPFaultException exception;

    public BillService1033ErrorHandler(SOAPFaultException exception) {
        this.exception = exception;
    }

    @Override
    public BillServiceResult sendBill(String fileName, byte[] zipFile, String partyType, ServiceConfig config) {
        String fileNameWithoutExtension = Util.getFileNameWithoutExtension(fileName);

        Matcher matcher = FILENAME_STRUCTURE.matcher(fileNameWithoutExtension);
        if (matcher.matches()) {

            // STEP 1.CHECK INFO
            String[] split = fileNameWithoutExtension.split("-");
            BillConsultBean consult = new BillConsultBean.Builder()
                    .ruc(split[0])
                    .tipo(split[1])
                    .serie(split[2])
                    .numero(Integer.parseInt(split[3]))
                    .build();

            ServiceConfig consultServiceConfig = new ServiceConfig.Builder()
                    .url(DEFAULT_BILL_CONSULT_URL)
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .build();

            service.sunat.gob.pe.billconsultservice.StatusResponse statusResponse1 = BillConsultServiceManager.getStatus(consult, consultServiceConfig);
            int statusCode1 = Integer.parseInt(statusResponse1.getStatusCode());
            if (statusCode1 < 1 || statusCode1 > 3) {
                // Unknown code
                return null;
            }

            // STEP 2. CHECK XML FILE
            try {
                ServiceConfig validServiceConfig = new ServiceConfig.Builder()
                        .url(DEFAULT_BILL_VALID_URL)
                        .username(config.getUsername())
                        .password(config.getPassword())
                        .build();

                byte[] xml = null;
                String xmlFileName = null;
                if (fileName.endsWith(".zip")) {
                    xml = Util.getFirstXmlFileFromZip(zipFile);
                    xmlFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".xml";
                }

                StatusResponse statusResponse2 = BillValidServiceManager.getStatus(xmlFileName, xml, validServiceConfig);
                int statusCode2 = Integer.parseInt(statusResponse2.getStatusCode());
                if (statusCode2 != 0) {
                    // xml file was not valid
                    return null;
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

            // STEP 3. PROCESS
            try {
                Optional<ConsultaResponseType> optional = ConsultaResponseType.searchByCode(statusResponse1.getStatusCode());
                if (optional.isPresent()) {
                    switch (optional.get()) {
                        case EXISTE_Y_ACEPTADO: {
                            return CdrUtils.processZip(statusResponse1.getContent());
                        }
                        case EXISTE_PERO_RECHAZADO: {
                            return CdrUtils.processZip(statusResponse1.getContent());
                        }
                        case EXISTE_PERO_DADO_DE_BAJA: {
                            BillServiceResult billServiceResult = CdrUtils.processZip(statusResponse1.getContent());
                            billServiceResult.setStatus(BillServiceResult.DocumentStatus.BAJA);
                            return billServiceResult;
                        }
                        default: {
                            return null;
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | XPathExpressionException | SAXException e) {
                throw new IllegalStateException(e);
            }
        }

        return null;
    }

}
