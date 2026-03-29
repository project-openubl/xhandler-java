package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.DespatchAdviceCommonValidator;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationMessage;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Validador de reglas de negocio para la Guía de Remisión Electrónica (GRE).
 * <p>
 * Implementa las reglas funcionales de SUNAT según:
 * <ul>
 * <li>RS 000123-2022/SUNAT — Reglas base GRE-Remitente y GRE-Transportista</li>
 * <li>RS 000240-2024/SUNAT — Reglas de comercio exterior (vigentes desde 14-nov-2024)</li>
 * <li>RS 000133-2025/SUNAT — Prórroga: derogatoria del ticket de salida al 01-jul-2026</li>
 * </ul>
 * <p>
 * Delega la lógica de validación a {@link DespatchAdviceCommonValidator} para evitar duplicación de reglas con
 * {@link GRERemitente} y {@link GRETransportista}.
 * <p>
 * Las validaciones retornan una lista de mensajes de error. Si la lista está vacía, el documento es válido respecto a
 * las reglas implementadas.
 * <p>
 * <b>IMPORTANTE:</b> Este validador NO reemplaza la validación de SUNAT (XSD/XSL). Es una capa de validación temprana
 * para detectar errores comunes antes del envío.
 *
 * @since 5.0.0
 * @see DespatchAdviceCommonValidator
 * @see ValidationResult
 */
public class DespatchAdviceValidator {

    /**
     * Valida un DespatchAdvice completo y retorna la lista de errores encontrados.
     * <p>
     * <b>Nota:</b> Solo retorna errores (severidad {@code ERROR}). Para obtener errores y advertencias con severidad
     * diferenciada, use {@link #validateDetailed(DespatchAdvice)}.
     *
     * @param da el DespatchAdvice a validar
     * @return lista de mensajes de error (vacía si es válido)
     */
    public static List<String> validate(DespatchAdvice da) {
        return validateDetailed(da).getErrors();
    }

    /**
     * Valida un DespatchAdvice completo y retorna un {@link ValidationResult} con errores y advertencias diferenciados
     * por severidad.
     * <p>
     * Criterio de severidad:
     * <ul>
     * <li>{@code ERROR} — Regla UBL o funcional SUNAT que causa rechazo</li>
     * <li>{@code WARNING} — Recomendación normativa (discrecionalidad vigente)</li>
     * </ul>
     *
     * @param da el DespatchAdvice a validar
     * @return resultado de validación con errores y advertencias
     * @since 5.2.0
     */
    public static ValidationResult validateDetailed(DespatchAdvice da) {
        List<ValidationMessage> messages = new ArrayList<>();

        // Campos básicos
        DespatchAdviceCommonValidator.validateBasicFields(da.getSerie(), da.getNumero(), messages);

        // Coherencia serie ↔ tipo comprobante
        DespatchAdviceCommonValidator.validateSerieCoherence(
                da.getSerie(), da.getTipoComprobante(), messages);

        // Partes
        DespatchAdviceCommonValidator.validateRemitente(da.getRemitente(), messages);
        DespatchAdviceCommonValidator.validateDestinatario(da.getDestinatario(), messages);

        // Tercero obligatorio para GRE-Transportista (31)
        if ("31".equals(da.getTipoComprobante()) && da.getTercero() == null && da.getProveedor() == null) {
            messages.add(ValidationMessage.error(
                    "GRE-Transportista (31): el tercero (remitente original) o proveedor "
                            + "en SellerSupplierParty es requerido"));
        }

        // Envío
        DespatchAdviceCommonValidator.validateEnvioRequired(da.getEnvio(), messages);
        DespatchAdviceCommonValidator.validatePartidaDestino(da.getEnvio(), messages);

        // Modalidad de transporte
        DespatchAdviceCommonValidator.validateModalidadGeneric(
                da.getTipoComprobante(), da.getEnvio(), messages);

        // Comercio exterior (advertencias)
        DespatchAdviceCommonValidator.validateComercioExterior(da.getEnvio(), messages);

        // Detalles
        DespatchAdviceCommonValidator.validateDetalles(da.getDetalles(), messages);

        return new ValidationResult(messages);
    }

    private DespatchAdviceValidator() {
        // Utility class
    }
}
