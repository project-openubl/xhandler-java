package io.github.project.openubl.xsender.sunat;

import java.math.BigDecimal;

/**
 * The criteria SUNAT's validation service (validaCDPcriterios) checks a document against.
 *
 * @param issueDate           formatted as SUNAT expects, dd/MM/yyyy
 * @param authorizationNumber may be null
 */
public record CdpQuery(
        String ruc,
        String documentType,
        String serie,
        String number,
        String receiverIdType,
        String receiverIdNumber,
        String issueDate,
        BigDecimal total,
        String authorizationNumber
) {
}
