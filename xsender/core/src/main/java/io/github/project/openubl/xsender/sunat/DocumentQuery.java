package io.github.project.openubl.xsender.sunat;

/**
 * Identifies an issued document in SUNAT's consult service.
 *
 * @param ruc          issuer's RUC
 * @param documentType catalog 1 code, e.g. "01" for factura
 * @param serie        e.g. "F001"
 * @param number       correlative number
 */
public record DocumentQuery(String ruc, String documentType, String serie, int number) {
}
