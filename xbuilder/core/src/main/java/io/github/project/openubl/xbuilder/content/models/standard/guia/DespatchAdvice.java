package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Modelo principal de la Guía de Remisión Electrónica (GRE).
 * <p>
 * Soporta ambos tipos:
 * <ul>
 * <li><b>GRE-Remitente (09)</b>: Serie Txxx. Emitida por el remitente de los
 * bienes.
 * El campo {@code remitente} contiene los datos del remitente (RUC + razón
 * social).
 * El {@code transportista} se consigna dentro de {@code envio} si modalidad es
 * pública.</li>
 * <li><b>GRE-Transportista (31)</b>: Serie Vxxx. Emitida por el transportista.
 * El campo {@code remitente} contiene los datos del transportista emitente.
 * El {@code tercero} contiene los datos del remitente original (quien envía los
 * bienes).</li>
 * </ul>
 * <p>
 * Fuente normativa: RS 000123-2022/SUNAT (base), RS 000240-2024/SUNAT (comercio
 * exterior),
 * RS 000133-2025/SUNAT (prórroga hasta 01-jul-2026).
 */
@Jacksonized
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DespatchAdvice {
    /**
     * Versión del formato de la guía de remisión (ejemplo: 2.0)
     */
    private String version;

    /**
     * Serie del comprobante.
     * <ul>
     * <li>GRE-Remitente: Txxx (alfanumérico) desde SEE del contribuyente</li>
     * <li>GRE-Transportista: Vxxx (alfanumérico) desde SEE del contribuyente</li>
     * </ul>
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 4, pattern = "^[T|t|V|v].*$")
    private String serie;

    /**
     * Número del comprobante
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "99999999")
    private Integer numero;

    /**
     * Fecha de emisión del comprobante. Ejemplo 2022-12-25 (YYYY-MM-SS)
     */
    @Schema(description = "Format: \"YYYY-MM-SS\". Ejemplo: 2022-12-25", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate fechaEmision;

    /**
     * Hora de emisión del comprobante. Ejemplo 12:00:00 (HH:MM:SS)
     */
    @Schema(description = "Format: \"HH:MM:SS\". Ejemplo 12:00:00", pattern = "^\\d{2}:\\d{2}:\\d{2}$")
    private LocalTime horaEmision;

    /**
     * Tipo de comprobante según Catálogo 01:
     * <ul>
     * <li>"09" = Guía de Remisión Remitente</li>
     * <li>"31" = Guía de Remisión Transportista</li>
     * </ul>
     */
    @Schema(description = "Catalogo 01")
    private String tipoComprobante;

    private String observaciones;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DocumentoBaja documentoBaja;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DocumentoRelacionado documentoRelacionado;

    /**
     * Documentos relacionados adicionales.
     * Permite vincular múltiples documentos como GRE-Remitente, DAM, DS, etc.
     * Ver también {@link Envio#getDeclaracionesAduaneras()} para referencias DAM/DS
     * específicas de comercio exterior.
     *
     * @since 2.0 - Permite múltiples documentos relacionados (antes solo uno).
     */
    @Singular("documentoRelacionadoAdicional")
    @Schema(description = "Documentos relacionados adicionales (Catálogo 21)")
    private List<DocumentoRelacionado> documentosRelacionados;

    /**
     * Documentos adicionales relacionados al transporte (Catálogo 61)
     */
    @Singular("documentoAdicional")
    @Schema(description = "Documentos adicionales relacionados al transporte")
    private List<DocumentoAdicional> documentosAdicionales;

    @Schema(description = "Persona que firma electrónicamente el comprobante. Si NULL los datos del proveedor son usados.")
    private Firmante firmante;

    /**
     * Datos del remitente.
     * <ul>
     * <li>GRE-Remitente (09): El remitente es quien envía los bienes.</li>
     * <li>GRE-Transportista (31): El remitente es el transportista emisor
     * (DespatchSupplierParty).</li>
     * </ul>
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Remitente remitente;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Destinatario destinatario;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Proveedor proveedor;

    /**
     * Datos del tercero (vendedor/remitente original de los bienes).
     * <p>
     * Aplica principalmente en GRE-Transportista (31): el tercero es el remitente
     * original que solicita el servicio de transporte.
     * Se mapea a {@code cac:SellerSupplierParty}.
     */
    @Schema(description = "Tercero/Vendedor de los bienes")
    private Tercero tercero;

    /**
     * Datos del comprador (adquiriente de los bienes).
     * Se mapea a {@code cac:BuyerCustomerParty}.
     */
    @Schema(description = "Comprador/Adquiriente de los bienes")
    private Comprador comprador;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Envio envio;

    @Singular
    @ArraySchema(minItems = 1, schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    private List<DespatchAdviceItem> detalles;

    // == Métodos utilitarios ==

    /**
     * Determina si esta GRE es de tipo Remitente (código 09).
     */
    public boolean isGRERemitente() {
        return "09".equals(tipoComprobante);
    }

    /**
     * Determina si esta GRE es de tipo Transportista (código 31).
     */
    public boolean isGRETransportista() {
        return "31".equals(tipoComprobante);
    }
}
