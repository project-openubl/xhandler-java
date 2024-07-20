package io.github.project.openubl.xsender.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SunatResponse {
    private Status status;
    private Metadata metadata;
    private Sunat sunat;

    public boolean isTicket() {
        return sunat != null && sunat.getTicket() != null;
    }

}
