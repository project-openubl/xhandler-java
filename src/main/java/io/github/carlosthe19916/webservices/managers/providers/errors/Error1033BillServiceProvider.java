package io.github.carlosthe19916.webservices.managers.providers.errors;

import io.github.carlosthe19916.webservices.managers.providers.AbstractErrorBillServiceProvider;
import io.github.carlosthe19916.webservices.managers.providers.BillServiceModel;
import io.github.carlosthe19916.webservices.models.types.ConsultaResponseType;
import io.github.carlosthe19916.webservices.utils.CdrToModel;
import io.github.carlosthe19916.webservices.utils.Utils;
import io.github.carlosthe19916.webservices.wrappers.BillConsultServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.BillValidServiceWrapper;
import io.github.carlosthe19916.webservices.wrappers.ServiceConfig;
import org.xml.sax.SAXException;
import service.sunat.gob.pe.billconsultservice.StatusResponse;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Error1033BillServiceProvider extends AbstractErrorBillServiceProvider {

    private final static String DEFAULT_BILL_CONSULT_URL = "https://exception-factura.sunat.gob.pe/ol-it-wsconscpegem/billConsultService";
    private final static String DEFAULT_BILL_VALID_URL = "https://exception-factura.sunat.gob.pe/ol-it-wsconsvalidcpe/billValidService";
    private final static Pattern FILENAME_STRUCTURE = Pattern.compile("(?:\\d{11}-)\\d{2}-[a-zA-Z_0-9]{4}-\\d{1,8}"); // [RUC]-[TIPO DOCUMENTO]-[SERIE]-[NUMERO]

    private final Integer exceptionCode;
    private final BillServiceModel previousResult;

    public Error1033BillServiceProvider(Integer exceptionCode) {
        this.previousResult = null;
        this.exceptionCode = exceptionCode;
    }

    public Error1033BillServiceProvider(Integer exceptionCode, BillServiceModel previousResult) {
        this.exceptionCode = exceptionCode;
        this.previousResult = previousResult;
    }

    @Override
    public BillServiceModel sendBill(String fileName, byte[] file, ServiceConfig config) {
        String fileNameWithoutExtension = Utils.getFileNameWithoutExtension(fileName);

        Matcher matcher = FILENAME_STRUCTURE.matcher(fileNameWithoutExtension);
        if (matcher.matches()) {

            ServiceConfig consultServiceConfig = new ServiceConfig.Builder()
                    .url(DEFAULT_BILL_CONSULT_URL)
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .build();

            // STEP 1.CHECK INFO
            String[] split = fileNameWithoutExtension.split("-");
            StatusResponse statusResponse1 = BillConsultServiceWrapper.getStatus(
                    consultServiceConfig,
                    split[0],
                    split[1],
                    split[2],
                    Integer.parseInt(split[3])
            );

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

                byte[] xml = file;
                String xmlFileName = fileName;
                if (fileName.endsWith(".zip")) {
                    xml = Utils.getFirstXmlFileFromZip(file);
                    xmlFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".xml";
                }

                assert xml != null;
                byte[] encode = Base64.getEncoder().encode(xml);
                String xmlBase64Encoded = new String(encode);

                service.sunat.gob.pe.billvalidservice.StatusResponse statusResponse2 = BillValidServiceWrapper.getStatus(validServiceConfig, xmlFileName, xmlBase64Encoded);
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
                            return CdrToModel.toModel(statusResponse1.getContent());
                        }
                        case EXISTE_PERO_RECHAZADO: {
                            return CdrToModel.toModel(statusResponse1.getContent());
                        }
                        case EXISTE_PERO_DADO_DE_BAJA: {
                            BillServiceModel model = CdrToModel.toModel(statusResponse1.getContent());
                            model.setStatus(BillServiceModel.Status.BAJA);
                            return model;
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
