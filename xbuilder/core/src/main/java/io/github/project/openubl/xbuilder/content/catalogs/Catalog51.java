package io.github.project.openubl.xbuilder.content.catalogs;

/**
 * Catálogo 51: Código de tipo de operación.
 * <p>
 * Fuente: SUNAT – Anexo V de la RS 097-2012/SUNAT y modificatorias. Usado en {@code cbc:InvoiceTypeCode/@listID} para
 * facturas y boletas.
 * </p>
 *
 * @see <a href="https://cpe.sunat.gob.pe/guias-y-manuales">Guía XML Factura 2.1 – Catálogo 51</a>
 */
public enum Catalog51 implements Catalog {

    // ── Operaciones comunes ──────────────────────────────────────────
    /** Venta interna (operación estándar gravada, exonerada, inafecta o mixta). */
    VENTA_INTERNA("0101"),
    /** Venta interna – anticipos. */
    VENTA_INTERNA_ANTICIPOS("0113"),
    /** Venta itinerante. */
    VENTA_ITINERANTE("0112"),

    // ── Exportación ──────────────────────────────────────────────────
    /** Exportación de bienes. */
    EXPORTACION_BIENES("0200"),
    /** Exportación de servicios – prestación de servicios (num. 1 art. 33 Ley IGV). */
    EXPORTACION_SERVICIOS_PRESTACION("0201"),
    /** Exportación de servicios – hospedaje no domiciliado. */
    EXPORTACION_SERVICIOS_HOSPEDAJE("0202"),
    /** Exportación de servicios – transporte navieros. */
    EXPORTACION_SERVICIOS_TRANSPORTE_NAVIEROS("0203"),
    /** Exportación de servicios – servicios a turistas no domiciliados. */
    EXPORTACION_SERVICIOS_TURISTAS("0204"),
    /** Exportación de servicios – venta de bienes a pasajeros. */
    EXPORTACION_SERVICIOS_BIENES_PASAJEROS("0205"),
    /** Exportación de servicios – asistencia técnica. */
    EXPORTACION_SERVICIOS_ASISTENCIA_TECNICA("0206"),
    /** Exportación de servicios – otros (arts. 33, 33-A, 76 Ley IGV). */
    EXPORTACION_SERVICIOS_OTROS("0207"),
    /** Exportación de servicios – prestación realizada en zona franca. */
    EXPORTACION_SERVICIOS_ZONA_FRANCA("0208"),

    // ── Operaciones con no domiciliados ──────────────────────────────
    /** Operación sujeta a detracción – recursos hidrobiológicos. */
    OPERACION_SUJETA_A_DETRACCION("1001"),
    /** Operación sujeta a detracción – servicios de transporte pasajeros. */
    OPERACION_SUJETA_DETRACCION_TRANSPORTE_PASAJEROS("1002"),
    /** Operación sujeta a detracción – servicios de transporte carga. */
    OPERACION_SUJETA_DETRACCION_TRANSPORTE_CARGA("1003"),
    /** Operación sujeta a detracción – IVAP (arroz pilado). */
    OPERACION_SUJETA_DETRACCION_IVAP("1004"),

    // ── Percepción ───────────────────────────────────────────────────
    /** Operación sujeta a percepción. */
    OPERACION_SUJETA_A_PERCEPCION("2001"),

    // ── Gratuitas ────────────────────────────────────────────────────
    /** Operación gratuita – transferencia gratuita. */
    OPERACION_GRATUITA("0112"),

    // ── NRUS ─────────────────────────────────────────────────────────
    /** Venta realizada por sujeto del NRUS. */
    VENTA_NRUS("0113"),

    // ── Otros ────────────────────────────────────────────────────────
    /** Factura guía (venta interna + guía de remisión embebida). */
    FACTURA_GUIA("0401");

    private final String code;

    Catalog51(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
