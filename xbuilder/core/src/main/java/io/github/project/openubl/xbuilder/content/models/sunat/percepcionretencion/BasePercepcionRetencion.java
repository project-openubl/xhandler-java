package io.github.project.openubl.xbuilder.content.models.sunat.percepcionretencion;

import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Document;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class BasePercepcionRetencion extends Document {

    /**
     * Número del comprobante
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "99999999")
    private Integer numero;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0", maximum = "100", exclusiveMinimum = true)
    private BigDecimal tipoRegimenPorcentaje;

    private String observacion;

    /**
     * Cliente
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Cliente cliente;

    private PercepcionRetencionOperacion operacion;

}
