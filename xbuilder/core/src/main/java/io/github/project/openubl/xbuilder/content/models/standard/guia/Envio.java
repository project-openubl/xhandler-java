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

/**
 * Modelo de envío/shipment de la Guía de Remisión Electrónica.
 * <p>
 * Fuente: Anexo N.° 14 UBL 2.1, RS 000123-2022/SUNAT, RS 000240-2024/SUNAT.
 */
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
     * Peso de los ítems seleccionados (en KGM).
     * Requerido cuando el peso bruto total difiere del peso de los ítems
     * (traslado parcial de DAM con carga a granel, FAQ #30 SUNAT).
     */
    @Schema(description = "Peso bruto de los items seleccionados")
    private BigDecimal pesoItems;

    /**
     * Sustento de la diferencia del peso bruto total respecto al peso de los ítems.
     * Requerido cuando {@code pesoItems} difiere de {@code pesoTotal}.
     * Ejemplo: "Retiro parcial de carga a granel", "Carga fraccionada" (FAQ #33).
     */
    @Schema(description = "Sustento de diferencia de peso")
    private String sustentoPeso;

    private Integer numeroDeBultos;

    @Schema(description = "Catalog 18", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoModalidadTraslado;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaTraslado;

    /**
     * Lista de contenedores con número y precinto.
     * <p>
     * Cada contenedor puede incluir número de identificación y precinto.
     * Aplica especialmente para comercio exterior (RS 000240-2024/SUNAT).
     */
    @Singular("contenedor")
    @Schema(description = "Lista de contenedores con número y precinto")
    private List<Contenedor> contenedores;

    /**
     * Puerto de embarque/desembarque.
     * Catálogo 63 de SUNAT.
     */
    @Schema(description = "Puerto de embarque/desembarque (Catalogo 63)")
    private Puerto puerto;

    /**
     * Aeropuerto de embarque/desembarque.
     * Catálogo 64 de SUNAT.
     */
    @Schema(description = "Aeropuerto de embarque/desembarque (Catalogo 64)")
    private Puerto aeropuerto;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Transportista transportista;

    /**
     * Lista de conductores (principal y secundarios).
     * <p>
     * GRE-Remitente con transporte privado: requerido (al menos conductor
     * principal).
     * GRE-Remitente con transporte público: no aplica (lo provee el transportista).
     * GRE-Transportista: requerido siempre.
     */
    @Singular("chofer")
    @Schema(description = "Lista de conductores")
    private List<Driver> choferes;

    /**
     * Vehículo principal con posibles vehículos secundarios.
     * <p>
     * GRE-Remitente con transporte privado: requerido.
     * GRE-Remitente con transporte público: no aplica.
     * GRE-Transportista: requerido siempre.
     */
    @Schema(description = "Vehículo de transporte")
    private Vehicle vehiculo;

    /**
     * Indicadores especiales de transporte.
     * <p>
     * Se mapean a {@code cbc:SpecialInstructions} en el XML.
     * Ver
     * {@link io.github.project.openubl.xbuilder.content.catalogs.IndicadorEnvio}
     * para la lista de valores tipados. Se acepta String libre para extensibilidad.
     * <p>
     * Indicadores comunes:
     * <ul>
     * <li>SUNAT_Envio_IndicadorTrasladoTotalDAMDS: traslado total de DAM/DS</li>
     * <li>SUNAT_Envio_IndicadorBienNormalizado: bien sujeto a SPOT/IVAP</li>
     * <li>SUNAT_Envio_IndicadorTrasladoVehiculoM1L: vehículos categoría M1/L (exime
     * conductor/vehículo)</li>
     * <li>SUNAT_Envio_IndicadorTransbordoProgramado: transbordo programado por
     * eventos</li>
     * <li>SUNAT_Envio_IndicadorRetornoVehiculoEnvasesVacios</li>
     * <li>SUNAT_Envio_IndicadorRetornoVehiculoVacio</li>
     * </ul>
     */
    @Singular("indicador")
    @Schema(description = "Indicadores especiales de transporte")
    private List<String> indicadores;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Partida partida;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Destino destino;

    // == Campos de comercio exterior (RS 000240-2024/SUNAT) ==

    /**
     * Número de manifiesto de carga.
     * <p>
     * Relevante para motivos de traslado de comercio exterior (08, 09, 10, 19).
     * Se consigna como documento relacionado (Catálogo 21, código 04).
     *
     * @since 2.0
     */
    @Schema(description = "Número de manifiesto de carga")
    private String numeroManifiesto;

    /**
     * Referencias a Declaraciones Aduaneras de Mercancías (DAM) o
     * Declaraciones Simplificadas (DS).
     * <p>
     * Requerido para motivos 08 (Importación), 09 (Exportación),
     * 10 (Importación con DAM) y 19 (Mercancía extranjera).
     * <p>
     * Se mapean a {@code cac:AdditionalDocumentReference} con Catálogo 61.
     *
     * @since 2.0
     */
    @Singular("declaracionAduanera")
    @Schema(description = "Declaraciones aduaneras (DAM/DS)")
    private List<DeclaracionAduanera> declaracionesAduaneras;
}
