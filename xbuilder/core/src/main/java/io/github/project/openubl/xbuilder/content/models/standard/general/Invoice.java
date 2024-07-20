package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.models.common.Direccion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Jacksonized
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
     * Anticipos asociados al comprobante
     */
    @Singular
    private List<Anticipo> anticipos;

    @Singular
    private List<Descuento> descuentos;
}
