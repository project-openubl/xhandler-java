package io.github.project.openubl.xsender.models;

import io.github.project.openubl.xsender.models.answers.CDR;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SunatResponse {
    private Status status;
    private Metadata metadata;
    private Sunat sunat;
    /**
     * The CDR as the xhandler core read it, with every field it has (e.g.
     * {@code documentDescription}); null without CDR
     */
    private CDR cdr;

    public boolean isTicket() {
        return sunat != null && sunat.getTicket() != null;
    }

}
