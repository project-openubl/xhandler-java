package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Invoice;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog51;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationMessage;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Validador de reglas de negocio para Factura Electrónica (01) y Boleta de Venta Electrónica (03).
 * <p>
 * Implementa validaciones previas al renderizado XML según:
 * <ul>
 * <li>Guía XML Factura 2.1 – SUNAT</li>
 * <li>Guía XML Boleta 2.1 – SUNAT</li>
 * <li>Reglas de Validación CPE (versión vigente)</li>
 * <li>RS 007-99/SUNAT – Reglamento de Comprobantes de Pago</li>
 * </ul>
 * <p>
 * <b>IMPORTANTE:</b> Este validador NO reemplaza la validación de SUNAT (XSD/XSL). Es una capa de validación temprana
 * para detectar errores comunes antes del firmado y envío.
 * </p>
 *
 * @since 5.3.0
 * @see Invoice
 * @see SalesDocumentValidator
 */
public final class InvoiceValidator {

    private InvoiceValidator() {
    }

    /**
     * Valida un Invoice y retorna solo los mensajes de error.
     *
     * @param invoice el Invoice a validar
     * @return lista de errores (vacía si es válido)
     */
    public static List<String> validate(Invoice invoice) {
        return validateDetailed(invoice).getErrors();
    }

    /**
     * Valida un Invoice y retorna un {@link ValidationResult} con errores y advertencias.
     *
     * @param invoice el Invoice a validar
     * @return resultado detallado con severidad
     */
    public static ValidationResult validateDetailed(Invoice invoice) {
        List<ValidationMessage> messages = new ArrayList<>();

        // ── Campos básicos ───────────────────────────────────────
        SalesDocumentValidator.validateBasicFields(invoice, messages);

        // ── Tipo de comprobante ──────────────────────────────────
        String tipo = invoice.getTipoComprobante();
        boolean isFactura = "01".equals(tipo);
        boolean isBoleta = "03".equals(tipo);

        if (tipo != null && !isFactura && !isBoleta) {
            messages.add(ValidationMessage.error(
                    "tipoComprobante debe ser '01' (Factura) o '03' (Boleta), valor: " + tipo));
        }

        // ── Serie coherente con tipo ─────────────────────────────
        String serie = invoice.getSerie();
        if (serie != null && tipo != null) {
            if (isFactura && !serie.toUpperCase().startsWith("F")) {
                messages.add(ValidationMessage.error(
                        "Factura (01) debe tener serie que empiece con 'F', valor: " + serie));
            }
            if (isBoleta && !serie.toUpperCase().startsWith("B")) {
                messages.add(ValidationMessage.error(
                        "Boleta (03) debe tener serie que empiece con 'B', valor: " + serie));
            }
        }

        // ── Tipo de operación (Catálogo 51) ──────────────────────
        String tipoOp = invoice.getTipoOperacion();
        if (tipoOp != null && Catalog.valueOfCode(Catalog51.class, tipoOp).isEmpty()) {
            messages.add(ValidationMessage.warning(
                    "tipoOperacion '" + tipoOp + "' no encontrado en Catálogo 51"));
        }

        // ── Cliente/receptor ─────────────────────────────────────
        validateCliente(invoice.getCliente(), isFactura, isBoleta, messages);

        // ── Detracción solo en facturas ──────────────────────────
        if (invoice.getDetraccion() != null && isBoleta) {
            messages.add(ValidationMessage.error(
                    "La detracción solo aplica a Facturas (01), no a Boletas (03)"));
        }

        // ── Detracción requiere tipoOperacion 1001 ──────────────
        if (invoice.getDetraccion() != null) {
            validateDetraccion(invoice.getDetraccion(), tipoOp, messages);
        }

        // ── Percepción requiere tipoOperacion 2001 ──────────────
        if (invoice.getPercepcion() != null) {
            validatePercepcion(invoice.getPercepcion(), tipoOp, messages);
        }

        // ── Detalles ─────────────────────────────────────────────
        SalesDocumentValidator.validateDetalles(invoice.getDetalles(), messages);

        // ── Exportación requiere código producto SUNAT ───────────
        if (tipoOp != null && tipoOp.startsWith("02")) {
            validateExportacionItems(invoice.getDetalles(), messages);
        }

        return new ValidationResult(messages);
    }

    // ── Validaciones de cliente ──────────────────────────────────

    private static void validateCliente(Cliente cliente, boolean isFactura, boolean isBoleta,
            List<ValidationMessage> messages) {
        if (cliente == null) {
            messages.add(ValidationMessage.error("El cliente/receptor es requerido"));
            return;
        }
        if (isBlank(cliente.getNombre())) {
            messages.add(ValidationMessage.error("El nombre del cliente es requerido"));
        }

        String tipoDoc = cliente.getTipoDocumentoIdentidad();
        String numDoc = cliente.getNumeroDocumentoIdentidad();

        if (isFactura) {
            // Factura: receptor debe tener RUC (tipo "6") o DNI para montos <= 700 PEN
            if (isBlank(tipoDoc) || isBlank(numDoc)) {
                messages.add(ValidationMessage.error(
                        "Factura: el tipo y número de documento del receptor son obligatorios"));
            }
            if ("6".equals(tipoDoc) && (numDoc == null || numDoc.length() != 11)) {
                messages.add(ValidationMessage.error(
                        "Factura: si tipoDocumentoIdentidad='6' (RUC), el número debe tener 11 dígitos"));
            }
        }

        if (isBoleta) {
            // Boleta: para montos > 700 PEN se requiere documento del receptor
            // (validación del monto se hace en el enricher/post-enricher)
            if (isBlank(tipoDoc) && isBlank(numDoc)) {
                messages.add(ValidationMessage.warning(
                        "Boleta: se recomienda consignar documento de identidad del receptor"));
            }
        }
    }

    // ── Validaciones de detracción ──────────────────────────────

    private static void validateDetraccion(Detraccion detraccion, String tipoOp,
            List<ValidationMessage> messages) {
        if (tipoOp != null && !tipoOp.startsWith("10")) {
            messages.add(ValidationMessage.error(
                    "Detracción requiere tipoOperacion 1001-1004 (Catálogo 51), valor: " + tipoOp));
        }
        if (isBlank(detraccion.getMedioDePago())) {
            messages.add(ValidationMessage.error("Detracción: medioDePago es requerido (Catálogo 59)"));
        }
        if (isBlank(detraccion.getCuentaBancaria())) {
            messages.add(ValidationMessage.error("Detracción: cuentaBancaria es requerida"));
        }
        if (isBlank(detraccion.getTipoBienDetraido())) {
            messages.add(ValidationMessage.error("Detracción: tipoBienDetraido es requerido (Catálogo 54)"));
        }
        if (detraccion.getPorcentaje() == null || detraccion.getPorcentaje().compareTo(BigDecimal.ZERO) <= 0) {
            messages.add(ValidationMessage.error("Detracción: porcentaje debe ser > 0"));
        }
    }

    // ── Validaciones de percepción ──────────────────────────────

    private static void validatePercepcion(Percepcion percepcion, String tipoOp,
            List<ValidationMessage> messages) {
        if (!"2001".equals(tipoOp)) {
            messages.add(ValidationMessage.error(
                    "Percepción requiere tipoOperacion='2001' (Catálogo 51), valor: " + tipoOp));
        }
        if (isBlank(percepcion.getTipo())) {
            messages.add(ValidationMessage.error("Percepción: tipo es requerido (Catálogo 53)"));
        }
    }

    // ── Exportación ─────────────────────────────────────────────

    private static void validateExportacionItems(List<DocumentoVentaDetalle> detalles,
            List<ValidationMessage> messages) {
        if (detalles == null)
            return;
        for (int i = 0; i < detalles.size(); i++) {
            DocumentoVentaDetalle d = detalles.get(i);
            if (isBlank(d.getCodigoProductoSunat())) {
                messages.add(ValidationMessage.warning(
                        "Línea " + (i + 1) + ": código producto SUNAT (UNSPSC) recomendado para exportación"));
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
