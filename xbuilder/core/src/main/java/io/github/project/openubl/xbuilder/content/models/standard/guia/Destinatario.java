package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos del destinatario de los bienes en la Guía de Remisión Electrónica.
 * <p>
 * Se mapea a {@code cac:DeliveryCustomerParty} en el XML UBL. El destinatario es obligatorio en toda GRE.
 * <p>
 * FAQ #22 SUNAT: para emisor itinerante, el destinatario puede ser el mismo remitente, pero el campo sigue siendo
 * obligatorio en el XML.
 *
 * @since 2.0
 * @see DespatchAdvice#getDestinatario()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Destinatario {

    /** Tipo de documento de identidad del destinatario (Catálogo 06). */
    @Schema(description = "Catalogo 06", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoDocumentoIdentidad;

    /** Número de documento de identidad del destinatario. */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroDocumentoIdentidad;

    /** Razón social o nombre del destinatario. */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}
