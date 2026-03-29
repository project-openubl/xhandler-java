package io.github.project.openubl.xbuilder.content.catalogs;

/**
 * Tipo de conductor en la Guía de Remisión Electrónica.
 * <p>
 * Se mapea al campo {@code cbc:JobTitle} del elemento {@code cac:DriverPerson}
 * en el XML UBL 2.1.
 * <p>
 * SUNAT distingue dos roles:
 * <ul>
 * <li><b>Principal</b>: conductor responsable del vehículo (obligatorio).</li>
 * <li><b>Secundario</b>: conductor de relevo o copiloto (opcional).</li>
 * </ul>
 * <p>
 * Ref: Anexo N.° 14 UBL 2.1, RS 000123-2022/SUNAT.
 */
public enum TipoConductor implements Catalog {

    /**
     * Conductor principal — obligatorio cuando se requiere consignar conductor.
     */
    PRINCIPAL("Principal"),

    /**
     * Conductor secundario (relevo, copiloto) — opcional.
     */
    SECUNDARIO("Secundario");

    private final String code;

    TipoConductor(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
