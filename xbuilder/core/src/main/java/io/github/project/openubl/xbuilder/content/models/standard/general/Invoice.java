package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.models.common.Direccion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Modelo de Factura Electrónica (01) y Boleta de Venta Electrónica (03).
 * <p>
 * Ambos documentos comparten la misma estructura UBL 2.1 ({@code Invoice}). La diferencia normativa está en:
 * <ul>
 * <li><b>Serie</b>: Factura = Fxxx, Boleta = Bxxx</li>
 * <li><b>Tipo comprobante</b> (Catálogo 01): Factura = "01", Boleta = "03"</li>
 * <li><b>Receptor</b>: Factura requiere RUC (6). Boleta acepta DNI (1), CE (4), etc.</li>
 * <li><b>Detracción</b>: Solo aplica a facturas</li>
 * <li><b>Resumen diario</b>: Las boletas se informan vía ResumenDiario; las facturas se envían individualmente</li>
 * </ul>
 * <p>
 * El campo {@code tipoComprobante} se deduce automáticamente de la serie si no se especifica.
 * </p>
 *
 * @see io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Invoice
 * @see io.github.project.openubl.xbuilder.content.catalogs.Catalog51
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Invoice extends SalesDocument {

    @Schema(description = "Ejemplo 2022-12-25", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaVencimiento;

    @Schema(description = "Catalogo 01")
    private String tipoComprobante;

    private String observaciones;

    /**
     * Catalog51
     */
    @Schema(description = "Catalogo 51")
    private String tipoOperacion;

    @Schema(description = "Forma de pago: al credito, o al contado")
    private FormaDePago formaDePago;

    @Schema(description = "Total importe del comprobante")
    private TotalImporteInvoice totalImporte;

    private Direccion direccionEntrega;
    private Detraccion detraccion;
    private Percepcion percepcion;

    /**
     * Guia de remision embebida (Factura Guia)
     */
    private EmbededDespatch guiaEmbebida;

    /**
     * Anticipos asociados al comprobante
     */
    @Singular
    private List<Anticipo> anticipos;

    @Singular
    private List<Descuento> descuentos;
}
