package io.github.project.openubl.xbuilder.content.models.sunat.resumen;

import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comprobante {

    @Schema(requiredMode = Schema.RequiredMode.AUTO, description = "Moneda del comprobante declarado")
    private String moneda;
    
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Catalogo 01")
    private String tipoComprobante;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String serieNumero;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Cliente cliente;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private ComprobanteValorVenta valorVenta;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private ComprobanteImpuestos impuestos;

    private ComprobanteAfectado comprobanteAfectado;
}
