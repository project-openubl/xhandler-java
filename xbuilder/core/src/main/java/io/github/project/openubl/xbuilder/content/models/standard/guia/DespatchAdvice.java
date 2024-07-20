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

@Jacksonized
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DespatchAdvice {
    /**
     * Serie del comprobante
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

    @Schema(description = "Catalogo 01")
    private String tipoComprobante;

    private String observaciones;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DocumentoBaja documentoBaja;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DocumentoRelacionado documentoRelacionado;

    @Schema(description = "Persona que firma electrónicamente el comprobante. Si NULL los datos del proveedor son usados.")
    private Firmante firmante;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Remitente remitente;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Destinatario destinatario;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Proveedor proveedor;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Envio envio;

    @Singular
    @ArraySchema(minItems = 1, schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED))
    private List<DespatchAdviceItem> detalles;
}
