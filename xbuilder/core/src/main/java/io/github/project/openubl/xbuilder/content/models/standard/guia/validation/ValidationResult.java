package io.github.project.openubl.xbuilder.content.models.standard.guia.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Resultado agregado de una validación de la GRE.
 * <p>
 * Contiene la lista completa de {@link ValidationMessage} y ofrece métodos de conveniencia para consultar errores y
 * advertencias por separado.
 *
 * <pre>{@code
 * ValidationResult result = DespatchAdviceValidator.validateDetailed(da);
 * if (result.hasErrors()) {
 *     result.getErrors().forEach(System.err::println);
 * }
 * result.getWarnings().forEach(log::warn);
 * }</pre>
 *
 * @since 5.2.0
 * @see DespatchAdviceCommonValidator
 */
public class ValidationResult {

    private final List<ValidationMessage> messages;

    /**
     * Crea un resultado con la lista de mensajes proporcionada.
     *
     * @param messages lista de mensajes de validación
     */
    public ValidationResult(List<ValidationMessage> messages) {
        this.messages = messages != null ? Collections.unmodifiableList(new ArrayList<>(messages))
                : Collections.emptyList();
    }

    /**
     * @return lista completa e inmutable de todos los mensajes (errores + advertencias)
     */
    public List<ValidationMessage> getMessages() {
        return messages;
    }

    /**
     * @return solo los textos de los mensajes con severidad {@link ValidationSeverity#ERROR}
     */
    public List<String> getErrors() {
        return messages.stream()
                .filter(ValidationMessage::isError)
                .map(ValidationMessage::getMessage)
                .collect(Collectors.toList());
    }

    /**
     * @return solo los textos de los mensajes con severidad {@link ValidationSeverity#WARNING}
     */
    public List<String> getWarnings() {
        return messages.stream()
                .filter(ValidationMessage::isWarning)
                .map(ValidationMessage::getMessage)
                .collect(Collectors.toList());
    }

    /**
     * @return {@code true} si hay al menos un mensaje de severidad {@link ValidationSeverity#ERROR}
     */
    public boolean hasErrors() {
        return messages.stream().anyMatch(ValidationMessage::isError);
    }

    /**
     * @return {@code true} si hay al menos un mensaje de severidad {@link ValidationSeverity#WARNING}
     */
    public boolean hasWarnings() {
        return messages.stream().anyMatch(ValidationMessage::isWarning);
    }

    /**
     * @return {@code true} si no hay errores (puede haber advertencias)
     */
    public boolean isValid() {
        return !hasErrors();
    }

    /**
     * @return solo los textos de mensajes de error (retrocompatible con {@code List<String>})
     */
    public List<String> getErrorMessages() {
        return getErrors();
    }

    @Override
    public String toString() {
        return "ValidationResult{errors=" + getErrors().size()
                + ", warnings=" + getWarnings().size() + "}";
    }
}
