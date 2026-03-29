package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.List;

/**
 * Línea de detalle de un documento de venta (factura, boleta, nota de crédito/débito).
 * <p>
 * Cada instancia representa una línea {@code cac:InvoiceLine} / {@code cac:CreditNoteLine} / {@code cac:DebitNoteLine}
 * en el XML UBL 2.1.
 * </p>
 * <p>
 * <b>Campos calculados automáticamente por el enricher:</b> {@code igv}, {@code igvBaseImponible}, {@code isc},
 * {@code iscBaseImponible}, {@code icb}, {@code totalImpuestos}, {@code precioReferencia},
 * {@code precioReferenciaTipo}.
 * </p>
 *
 * @see io.github.project.openubl.xbuilder.content.catalogs.Catalog7
 * @see io.github.project.openubl.xbuilder.content.catalogs.Catalog8
 * @see io.github.project.openubl.xbuilder.content.catalogs.Catalog16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoVentaDetalle {

    // ── Descripción e identificación ──────────────────────────────

    /** Descripción del bien o servicio ({@code cbc:Description}). */
    @Schema(description = "Descripción del bien o servicio", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descripcion;

    /**
     * Código interno del producto del vendedor ({@code cac:SellersItemIdentification/cbc:ID}). Opcional, pero
     * recomendado para trazabilidad.
     */
    @Schema(description = "Código interno del vendedor")
    private String codigoProducto;

    /**
     * Código de producto SUNAT / UNSPSC ({@code cac:CommodityClassification/cbc:ItemClassificationCode}). Obligatorio
     * para: exportaciones, detracciones y cuando RS 133-2019/SUNAT lo exija según cronograma.
     */
    @Schema(description = "Código UNSPSC (Catálogo 25 SUNAT)")
    private String codigoProductoSunat;

    /**
     * Código de producto estándar GS1 – GTIN/EAN ({@code cac:StandardItemIdentification/cbc:ID}). Opcional.
     */
    @Schema(description = "Código GS1/GTIN/EAN del producto")
    private String codigoProductoGS1;

    // ── Cantidad y medida ─────────────────────────────────────────

    /** Unidad de medida (Catálogo 03 SUNAT). Default: "NIU" (unidad). */
    private String unidadMedida;

    /** Cantidad del bien o servicio. */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", exclusiveMinimum = true)
    private BigDecimal cantidad;

    // ── Precios ───────────────────────────────────────────────────

    /** Precio unitario sin incluir impuestos ({@code cac:Price/cbc:PriceAmount}). */
    @Schema(description = "Precio sin incluir impuestos", minimum = "0")
    private BigDecimal precio;

    /** Si {@code true}, el campo {@code precio} ya incluye IGV y se recalculará internamente. */
    @Schema(description = "Precio incluyendo impuestos")
    private boolean precioConImpuestos;

    /** Precio de referencia unitario ({@code cac:PricingReference/cac:AlternativeConditionPrice}). Calculado. */
    @Schema(minimum = "0")
    private BigDecimal precioReferencia;

    /** Tipo de precio de referencia (Catálogo 16). Calculado. */
    @Schema(description = "Catálogo 16")
    private String precioReferenciaTipo;

    // ── IGV ───────────────────────────────────────────────────────

    /** Tasa de IGV. Ejemplo: 0.18. Heredada del documento padre si no se especifica. */
    @Schema(description = "Ejemplo: 0.18", minimum = "0", maximum = "1")
    private BigDecimal tasaIgv;

    /** Monto total de IGV de esta línea. Calculado. */
    @Schema(description = "Monto total de IGV", minimum = "0")
    private BigDecimal igv;

    /** Base imponible del IGV. Calculado. */
    @Schema(minimum = "0")
    private BigDecimal igvBaseImponible;

    /**
     * Tipo de afectación al IGV (Catálogo 07). Default: "10" (gravado – operación onerosa).
     */
    @Schema(description = "Catálogo 07")
    private String igvTipo;

    // ── ICBPER (Impuesto al Consumo de Bolsas de Plástico) ───────

    /** Tasa del ICBPER por unidad. Ejemplo: 0.50 (PEN por bolsa). */
    @Schema(minimum = "0")
    private BigDecimal tasaIcb;

    /** Monto total del ICBPER. Calculado: cantidad × tasaIcb. */
    @Schema(minimum = "0")
    private BigDecimal icb;

    /** {@code true} si el ICBPER aplica a esta línea (bolsas de plástico). */
    @Schema(description = "'true' si ICB es aplicado a este bien o servicio")
    private boolean icbAplica;

    // ── ISC (Impuesto Selectivo al Consumo) ──────────────────────

    /** Tasa del ISC. Ejemplo: 0.17. */
    @Schema(description = "Ejemplo: 0.17", minimum = "0", maximum = "1")
    private BigDecimal tasaIsc;

    /** Monto total del ISC. Calculado. */
    @Schema(description = "Monto total de ISC", minimum = "0")
    private BigDecimal isc;

    /** Base imponible del ISC. Calculado. */
    @Schema(minimum = "0")
    private BigDecimal iscBaseImponible;

    /** Sistema de cálculo del ISC (Catálogo 08). */
    @Schema(description = "Catálogo 08")
    private String iscTipo;

    // ── Descuentos/cargos por línea ──────────────────────────────

    /**
     * Descuentos aplicados a esta línea ({@code cac:AllowanceCharge} con {@code ChargeIndicator=false}). Catálogo 53 –
     * código "00" (descuento que afecta base imponible) o "01" (descuento que no afecta).
     */
    @Singular
    private List<CargoDescuento> descuentos;

    /**
     * Cargos aplicados a esta línea ({@code cac:AllowanceCharge} con {@code ChargeIndicator=true}). Catálogo 53 –
     * código "47" (cargo que afecta base imponible) o "48" (otros cargos).
     */
    @Singular
    private List<CargoDescuento> cargos;

    // ── Totales ──────────────────────────────────────────────────

    /** Total de impuestos de la línea (IGV + ISC + ICBPER). Calculado. */
    @Schema(minimum = "0")
    private BigDecimal totalImpuestos;
}
