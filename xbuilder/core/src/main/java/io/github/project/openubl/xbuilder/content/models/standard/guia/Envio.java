package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Schema(description = "Catalog 20", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoTraslado;
    private String motivoTraslado;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal pesoTotal;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String pesoTotalUnidadMedida;

    private Integer numeroDeBultos;
    private boolean transbordoProgramado;

    @Schema(description = "Catalog 18", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoModalidadTraslado;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaTraslado;

    private String numeroDeContenedor;
    private String codigoDePuerto;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Transportista transportista;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Partida partida;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Destino destino;
}
