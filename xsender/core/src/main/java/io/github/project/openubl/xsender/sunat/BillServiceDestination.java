package io.github.project.openubl.xsender.sunat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.apache.camel.component.http.HttpMethods;

@Data
@Builder
@AllArgsConstructor
public class BillServiceDestination {
    private final String url;
    private final SoapOperation soapOperation;
    private final RestOperation restOperation;

    @Getter
    @AllArgsConstructor
    public enum SoapOperation {
        SEND_BILL("sendBill"),
        SEND_SUMMARY("sendSummary"),
        SEND_PACK("sendPack"),
        GET_STATUS("getStatus");

        private final String webMethod;
    }

    @Getter
    @AllArgsConstructor
    public enum RestOperation {
        SEND_DOCUMENT(HttpMethods.POST, "/comprobantes"),
        VERIFY_TICKET(HttpMethods.GET, "/comprobantes/envios");

        private final HttpMethods method;
        private final String path;
    }

}
