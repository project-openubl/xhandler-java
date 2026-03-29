package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationMessage;

import java.math.BigDecimal;
import java.util.List;

/**
 * Validaciones compartidas entre Invoice, CreditNote y DebitNote.
 * <p>
 * Valida campos comunes de {@link SalesDocument} y sus líneas de detalle.
 * </p>
 *
 * @since 5.3.0
 */
public final class SalesDocumentValidator {

    private SalesDocumentValidator() {
    }

    /**
     * Valida campos básicos del documento de venta: serie, número, proveedor.
     */
    static void validateBasicFields(SalesDocument doc, List<ValidationMessage> messages) {
        if (doc.getSerie() == null || doc.getSerie().isBlank()) {
            messages.add(ValidationMessage.error("La serie es requerida"));
        } else if (doc.getSerie().length() != 4) {
            messages.add(ValidationMessage.error(
                    "La serie debe tener 4 caracteres, valor: '" + doc.getSerie() + "'"));
        }

        if (doc.getNumero() == null) {
            messages.add(ValidationMessage.error("El número es requerido"));
        } else if (doc.getNumero() < 1 || doc.getNumero() > 99999999) {
            messages.add(ValidationMessage.error(
                    "El número debe estar entre 1 y 99999999, valor: " + doc.getNumero()));
        }

        if (doc.getProveedor() == null) {
            messages.add(ValidationMessage.error("El proveedor (emisor) es requerido"));
        } else {
            if (doc.getProveedor().getRuc() == null || doc.getProveedor().getRuc().length() != 11) {
                messages.add(ValidationMessage.error("El RUC del proveedor debe tener 11 dígitos"));
            }
            if (doc.getProveedor().getRazonSocial() == null || doc.getProveedor().getRazonSocial().isBlank()) {
                messages.add(ValidationMessage.error("La razón social del proveedor es requerida"));
            }
        }
    }

    /**
     * Valida las líneas de detalle del documento.
     */
    static void validateDetalles(List<DocumentoVentaDetalle> detalles, List<ValidationMessage> messages) {
        if (detalles == null || detalles.isEmpty()) {
            messages.add(ValidationMessage.error("El documento debe tener al menos una línea de detalle"));
            return;
        }

        for (int i = 0; i < detalles.size(); i++) {
            DocumentoVentaDetalle d = detalles.get(i);
            String prefix = "Línea " + (i + 1) + ": ";

            if (d.getDescripcion() == null || d.getDescripcion().isBlank()) {
                messages.add(ValidationMessage.error(prefix + "descripción es requerida"));
            }
            if (d.getCantidad() == null || d.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                messages.add(ValidationMessage.error(prefix + "cantidad debe ser > 0"));
            }
            if (d.getPrecio() == null && !d.isPrecioConImpuestos()) {
                messages.add(ValidationMessage.error(prefix + "precio es requerido"));
            }

            // Validar igvTipo si se especifica
            String igvTipo = d.getIgvTipo();
            if (igvTipo != null && Catalog.valueOfCode(Catalog7.class, igvTipo).isEmpty()) {
                messages.add(ValidationMessage.warning(
                        prefix + "igvTipo '" + igvTipo + "' no encontrado en Catálogo 07"));
            }
        }
    }
}
