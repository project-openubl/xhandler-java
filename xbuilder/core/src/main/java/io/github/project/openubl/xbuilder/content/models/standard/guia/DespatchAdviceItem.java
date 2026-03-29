package io.github.project.openubl.xbuilder.content.models.standard.guia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.math.BigDecimal;
import java.util.List;

/**
 * Línea de detalle de la Guía de Remisión Electrónica.
 * <p>
 * Cada ítem representa un bien trasladado con su cantidad, unidad de medida, descripción y código. Toda GRE requiere al
 * menos un ítem (requerimiento UBL).
 * <p>
 * FAQ #24 SUNAT: cuando se usa el indicador de traslado total DAM/DS, la línea puede contener solo los campos mínimos
 * obligatorios.
 *
 * @since 2.0
 * @see DespatchAdvice
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DespatchAdviceItem {

    /** Unidad de medida según código UN/ECE Rec. 20 (ejemplo: "NIU", "KGM"). */
    private String unidadMedida;

    /** Cantidad del bien trasladado. */
    private BigDecimal cantidad;

    /** Descripción del bien. */
    private String descripcion;

    /** Código interno del bien asignado por el emisor. */
    private String codigo;

    /** Código SUNAT del bien (Catálogo según corresponda). */
    private String codigoSunat;

    /** Atributos adicionales del ítem (pares código-valor). */
    @Singular
    private List<GuiaItemAttribute> atributos;
}
