package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Clase base para CreditNote y DebitNOte.
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class Note extends SalesDocument {

    /**
     * Tipo de nota.
     * <p>
     * Nota de Crédito: Catalogo 09.
     * <p>
     * Nota de Débito: Catalogo 10.
     */
    @Schema(description = "Si NotaCredito Catalog 09. Si NotaDebito Catalog 10")
    private String tipoNota;

    /**
     * Serie y número del comprobante al que le aplica la nota de crédito/débito.
     * Ejemplo: F001-1
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String comprobanteAfectadoSerieNumero;

    /**
     * Tipo de del probante referido en {@link Note#comprobanteAfectadoSerieNumero}.
     * <p>
     * Catalogo 01.
     */
    @Schema(description = "Catalog 01")
    private String comprobanteAfectadoTipo;

    /**
     * Texto sustentatorio para la emision de la nota
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String sustentoDescripcion;

    /**
     * Importe total de la nota
     */
    private TotalImporteNote totalImporte;
}
