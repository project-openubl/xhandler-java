package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog9;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog10;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationMessage;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Validador de reglas de negocio para Notas de Crédito y Notas de Débito.
 * <p>
 * Implementa validaciones previas al renderizado según:
 * <ul>
 * <li>Guía XML Nota de Crédito 2.1 – SUNAT</li>
 * <li>Guía XML Nota de Débito 2.1 – SUNAT</li>
 * <li>Reglas de Validación CPE (versión vigente)</li>
 * </ul>
 *
 * @since 5.3.0
 * @see CreditNote
 * @see DebitNote
 */
public final class NoteValidator {

    private NoteValidator() {
    }

    /**
     * Valida una CreditNote y retorna solo los mensajes de error.
     */
    public static List<String> validate(CreditNote note) {
        return validateCreditNoteDetailed(note).getErrors();
    }

    /**
     * Valida una DebitNote y retorna solo los mensajes de error.
     */
    public static List<String> validate(DebitNote note) {
        return validateDebitNoteDetailed(note).getErrors();
    }

    /**
     * Valida una CreditNote con resultado detallado (errores + advertencias).
     */
    public static ValidationResult validateCreditNoteDetailed(CreditNote note) {
        List<ValidationMessage> messages = new ArrayList<>();
        validateCommon(note, messages);

        // tipoNota debe ser del Catálogo 09
        if (note.getTipoNota() != null && Catalog.valueOfCode(Catalog9.class, note.getTipoNota()).isEmpty()) {
            messages.add(ValidationMessage.error(
                    "tipoNota '" + note.getTipoNota() + "' no encontrado en Catálogo 09 (Nota de Crédito)"));
        }

        return new ValidationResult(messages);
    }

    /**
     * Valida una DebitNote con resultado detallado (errores + advertencias).
     */
    public static ValidationResult validateDebitNoteDetailed(DebitNote note) {
        List<ValidationMessage> messages = new ArrayList<>();
        validateCommon(note, messages);

        // tipoNota debe ser del Catálogo 10
        if (note.getTipoNota() != null && Catalog.valueOfCode(Catalog10.class, note.getTipoNota()).isEmpty()) {
            messages.add(ValidationMessage.error(
                    "tipoNota '" + note.getTipoNota() + "' no encontrado en Catálogo 10 (Nota de Débito)"));
        }

        return new ValidationResult(messages);
    }

    private static void validateCommon(Note note, List<ValidationMessage> messages) {
        SalesDocumentValidator.validateBasicFields(note, messages);

        if (isBlank(note.getComprobanteAfectadoSerieNumero())) {
            messages.add(ValidationMessage.error(
                    "comprobanteAfectadoSerieNumero es requerido (serie-numero del documento afectado)"));
        }

        if (isBlank(note.getComprobanteAfectadoTipo())) {
            messages.add(ValidationMessage.error(
                    "comprobanteAfectadoTipo es requerido (Catálogo 01: tipo del documento afectado)"));
        }

        if (isBlank(note.getTipoNota())) {
            messages.add(ValidationMessage.error(
                    "tipoNota es requerido (Catálogo 09 para NC, Catálogo 10 para ND)"));
        }

        if (isBlank(note.getSustentoDescripcion())) {
            messages.add(ValidationMessage.error(
                    "sustentoDescripcion es requerido (motivo de la nota)"));
        }

        SalesDocumentValidator.validateDetalles(note.getDetalles(), messages);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
