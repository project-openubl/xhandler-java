package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

/**
 * Modelo para vehículo de transporte de la guía de remisión.
 * Soporta vehículo principal y vehículos secundarios (carreta, etc.).
 * Basado en el modelo Vehicle de greenter.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    /**
     * Número de placa del vehículo
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String placa;

    /**
     * Número de tarjeta de circulación (TUC)
     */
    @Schema(description = "Número de tarjeta única de circulación")
    private String numeroCirculacion;

    /**
     * Número de autorización o certificado de habilitación vehicular
     */
    @Schema(description = "Número de autorización o certificado de habilitación")
    private String numeroAutorizacion;

    /**
     * Código de la entidad emisora de la autorización
     */
    @Schema(description = "Código de entidad autorizadora")
    private String codigoEmisor;

    /**
     * Lista de vehículos secundarios (carretas, semirremolques, etc.)
     */
    @Singular
    @Schema(description = "Vehículos secundarios adjuntos")
    private List<Vehicle> secundarios;
}
