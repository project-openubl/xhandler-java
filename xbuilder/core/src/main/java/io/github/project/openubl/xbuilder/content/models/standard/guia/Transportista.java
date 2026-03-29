package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos del transportista en la Guía de Remisión Electrónica.
 * <p>
 * Se usa en dos contextos:
 * <ul>
 * <li>GRE-Remitente con transporte público (01): el transportista contratado se consigna en
 * {@link Envio#getTransportista()}.</li>
 * <li>GRE-Transportista: el transportista emisor se mapea desde {@link GRETransportista#getTransportistaEmisor()} hacia
 * {@code cac:DespatchSupplierParty}.</li>
 * </ul>
 *
 * @since 2.0
 * @see Envio#getTransportista()
 * @see GRETransportista#getTransportistaEmisor()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transportista {

    /** Tipo de documento de identidad (Catálogo 06). */
    @Schema(description = "Catalogo 06", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumentoIdentidad;

    /** Número de documento de identidad (RUC para empresas). */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroDocumentoIdentidad;

    /** Razón social o nombre del transportista. */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    /**
     * Número de registro del Ministerio de Transportes y Comunicaciones
     */
    @Schema(description = "Número de registro MTC")
    private String numeroRegistroMTC;

}
