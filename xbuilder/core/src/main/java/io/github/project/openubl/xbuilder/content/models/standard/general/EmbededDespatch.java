package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Direccion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbededDespatch {

    @Schema(description = "Punto de llegada")
    private Direccion llegada;

    @Schema(description = "Punto de partida")
    private Direccion partida;

    @Schema(description = "Datos del transportista")
    private Cliente transportista;

    @Schema(description = "Numero de licencia de conducir")
    private String nroLicencia;

    @Schema(description = "Placa del vehiculo")
    private String transpPlaca;

    @Schema(description = "Codigo de autorizacion del transporte")
    private String transpCodeAuth;

    @Schema(description = "Marca del vehiculo")
    private String transpMarca;

    @Schema(description = "Modalidad de traslado. Catalog 18")
    private String modTraslado;

    @Schema(description = "Peso bruto total")
    private BigDecimal pesoBruto;

    @Schema(description = "Unidad de medida del peso bruto")
    private String undPesoBruto;

}
