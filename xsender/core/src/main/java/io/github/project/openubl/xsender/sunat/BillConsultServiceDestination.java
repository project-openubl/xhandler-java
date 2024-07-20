package io.github.project.openubl.xsender.sunat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class BillConsultServiceDestination {
    private final String url;
    private final Operation operation;

    @Getter
    @AllArgsConstructor
    public enum Operation {
        GET_STATUS("getStatus"),
        GET_STATUS_CDR("getStatusCdr");

        private final String webMethod;
    }
}
