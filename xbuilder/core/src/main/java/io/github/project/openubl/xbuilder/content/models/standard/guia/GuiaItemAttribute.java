package io.github.project.openubl.xbuilder.content.models.standard.guia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Atributo adicional de un ítem de la Guía de Remisión Electrónica.
 * <p>
 * Permite agregar pares clave-valor con información complementaria sobre el bien trasladado (por ejemplo, lote, fecha
 * de vencimiento, número de serie, etc.).
 * <p>
 * Se mapea a {@code cac:DespatchLine/cac:Item/cac:AdditionalItemProperty}.
 *
 * @since 2.0
 * @see DespatchAdviceItem#getAtributos()
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuiaItemAttribute {

    /** Código del atributo (identificador del tipo de propiedad). */
    private String code;

    /** Nombre descriptivo del atributo. */
    private String name;

    /** Valor del atributo. */
    private String value;
}
