package io.github.project.openubl.xsender.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sunat {
    private String ticket;
    private byte[] cdr;
    /**
     * For an accepted guía de remisión, the URL its printed copy's QR code must
     * encode; null for other documents (and with a core older than 6.0.0-alpha.2)
     */
    private String documentDescription;
}
