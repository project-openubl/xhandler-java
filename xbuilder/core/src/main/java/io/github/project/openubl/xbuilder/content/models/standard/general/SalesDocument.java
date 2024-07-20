package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Document;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public abstract class SalesDocument extends Document {

    /**
     * Leyendas asociadas al comprobante
     */
    @Singular
    private Map<String, String> leyendas;

    /**
     * Tasa IVAP. Ejemplo: 0.04
     */
    @Schema(description = "Ejemplo: 0.04", minimum = "0", maximum = "1")
    private BigDecimal tasaIvap;

    /**
     * Tasa del IGV. Ejemplo: 0.18
     */
    @Schema(description = "Ejemplo: 0.18", minimum = "0", maximum = "1")
    private BigDecimal tasaIgv;

    /**
     * Tasa del IBC. Ejemplo: 0.2
     */
    @Schema(description = "Ejemplo: 0.2", minimum = "0")
    private BigDecimal tasaIcb;

    /**
     * Serie del comprobante
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 4, pattern = "^[F|f|B|b].*$")
    private String serie;

    /**
     * Número del comprobante
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "99999999")
    private Integer numero;

    /**
     * Hora de emisión del comprobante. Ejemplo 12:00:00 (HH:MM:SS)
     */
    @Schema(description = "Format: \"HH:MM:SS\". Ejemplo 12:00:00", pattern = "^\\d{2}:\\d{2}:\\d{2}$")
    private LocalTime horaEmision;

    /**
     * Orden de compra
     */
    private String ordenDeCompra;

    /**
     * Cliente
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Cliente cliente;

    /**
     * Total de impuestos a pagar
     */
    private TotalImpuestos totalImpuestos;

    /**
     * Detalle del comprobante
     */
    @Singular
    @ArraySchema(minItems = 1, schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    private List<DocumentoVentaDetalle> detalles;

    /**
     * Guias de remision relacionadas
     */
    @Singular
    @ArraySchema
    private List<Guia> guias;

    /**
     * Otros documentos relacionados
     */
    @Singular
    @ArraySchema
    private List<DocumentoRelacionado> documentosRelacionados;
}
