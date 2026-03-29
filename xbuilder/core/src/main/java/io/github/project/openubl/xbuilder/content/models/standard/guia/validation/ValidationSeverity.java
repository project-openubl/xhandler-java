package io.github.project.openubl.xbuilder.content.models.standard.guia.validation;

/**
 * Severidad de un mensaje de validación de la GRE.
 * <p>
 * Define dos niveles:
 * <ul>
 * <li>{@link #ERROR} — El documento será rechazado por SUNAT (falla de UBL/regla funcional). <em>Siempre impide la
 * emisión.</em></li>
 * <li>{@link #WARNING} — Recomendación funcional o normativa cuya obligatoriedad está pospuesta, es discrecional, o
 * depende del contexto de negocio. <em>No impide la emisión, pero conviene atender.</em></li>
 * </ul>
 *
 * @since 5.2.0
 * @see ValidationMessage
 * @see ValidationResult
 */
public enum ValidationSeverity {

    /**
     * Error duro: el documento será rechazado por SUNAT si se envía con este defecto.
     */
    ERROR,

    /**
     * Advertencia: recomendación de cumplimiento normativo, no bloquea el envío.
     */
    WARNING
}
