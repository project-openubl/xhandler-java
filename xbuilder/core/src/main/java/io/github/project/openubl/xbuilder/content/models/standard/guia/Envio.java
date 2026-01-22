package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    /**
     * Peso de los ítems seleccionados (en KGM)
     */
    @Schema(description = "Peso bruto de los items seleccionados")
    private BigDecimal pesoItems;

    /**
     * Sustento de la diferencia del peso bruto total respecto al peso de los ítems
     */
    @Schema(description = "Sustento de diferencia de peso")
    private String sustentoPeso;

    private Integer numeroDeBultos;

    @Schema(description = "Catalog 18", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoModalidadTraslado;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaTraslado;

    /**
     * Lista de contenedores/precintos
     */
    @Singular("contenedor")
    @Schema(description = "Lista de contenedores o precintos")
    private List<String> contenedores;

    /**
     * Puerto de embarque/desembarque
     */
    @Schema(description = "Puerto de embarque/desembarque (Catalogo 63)")
    private Puerto puerto;

    /**
     * Aeropuerto de embarque/desembarque
     */
    @Schema(description = "Aeropuerto de embarque/desembarque (Catalogo 64)")
    private Puerto aeropuerto;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Transportista transportista;

    /**
     * Lista de conductores (principal y secundarios)
     */
    @Singular("chofer")
    @Schema(description = "Lista de conductores")
    private List<Driver> choferes;

    /**
     * Vehículo principal con posibles vehículos secundarios
     */
    @Schema(description = "Vehículo de transporte")
    private Vehicle vehiculo;

    /**
     * Indicadores especiales de transporte (SUNAT_Envio_*)
     */
    @Singular("indicador")
    @Schema(description = "Indicadores especiales de transporte")
    private List<String> indicadores;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Partida partida;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Destino destino;
}
