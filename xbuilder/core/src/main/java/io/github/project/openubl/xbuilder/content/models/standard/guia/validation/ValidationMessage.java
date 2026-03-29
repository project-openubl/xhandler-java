package io.github.project.openubl.xbuilder.content.models.standard.guia.validation;

/**
 * Mensaje individual de validación con severidad asociada.
 * <p>
 * Cada instancia representa una regla evaluada que no se cumplió.
 *
 * @since 5.2.0
 * @see ValidationSeverity
 * @see ValidationResult
 */
public class ValidationMessage {

    private final ValidationSeverity severity;
    private final String message;

    /**
     * Crea un mensaje de validación.
     *
     * @param severity severidad del mensaje ({@link ValidationSeverity#ERROR} o {@link ValidationSeverity#WARNING})
     * @param message  texto descriptivo del problema detectado
     */
    public ValidationMessage(ValidationSeverity severity, String message) {
        this.severity = severity;
        this.message = message;
    }

    /**
     * Crea un mensaje de error ({@link ValidationSeverity#ERROR}).
     *
     * @param message texto descriptivo
     * @return nueva instancia con severidad ERROR
     */
    public static ValidationMessage error(String message) {
        return new ValidationMessage(ValidationSeverity.ERROR, message);
    }

    /**
     * Crea un mensaje de advertencia ({@link ValidationSeverity#WARNING}).
     *
     * @param message texto descriptivo
     * @return nueva instancia con severidad WARNING
     */
    public static ValidationMessage warning(String message) {
        return new ValidationMessage(ValidationSeverity.WARNING, message);
    }

    /**
     * @return la severidad de este mensaje
     */
    public ValidationSeverity getSeverity() {
        return severity;
    }

    /**
     * @return el texto descriptivo del problema
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return {@code true} si la severidad es {@link ValidationSeverity#ERROR}
     */
    public boolean isError() {
        return severity == ValidationSeverity.ERROR;
    }

    /**
     * @return {@code true} si la severidad es {@link ValidationSeverity#WARNING}
     */
    public boolean isWarning() {
        return severity == ValidationSeverity.WARNING;
    }

    @Override
    public String toString() {
        return "[" + severity + "] " + message;
    }
}
