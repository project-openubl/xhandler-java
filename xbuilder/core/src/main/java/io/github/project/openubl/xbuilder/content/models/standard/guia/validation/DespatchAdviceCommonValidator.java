package io.github.project.openubl.xbuilder.content.models.standard.guia.validation;

import io.github.project.openubl.xbuilder.content.models.standard.guia.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reglas de validación compartidas para la Guía de Remisión Electrónica (GRE).
 * <p>
 * Centraliza las validaciones comunes entre {@code DespatchAdviceValidator},
 * {@code GRERemitente.validate()} y {@code GRETransportista.validate()}, eliminando
 * la duplicación de reglas y garantizando coherencia funcional.
 * <p>
 * <b>Criterio de severidad:</b>
 * <ul>
 *   <li>{@link ValidationSeverity#ERROR} — Regla de UBL o funcional SUNAT cuyo incumplimiento
 *       causa rechazo del documento. Siempre bloquea la emisión.</li>
 *   <li>{@link ValidationSeverity#WARNING} — Recomendación normativa cuya obligatoriedad está
 *       diferida (RS 000133-2025/SUNAT), es discrecional, o depende del contexto del emisor.</li>
 * </ul>
 * <p>
 * Fuente normativa: RS 000123-2022/SUNAT, RS 000240-2024/SUNAT, RS 000133-2025/SUNAT.
 *
 * @since 5.2.0
 * @see io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdviceValidator
 * @see io.github.project.openubl.xbuilder.content.models.standard.guia.GRERemitente
 * @see io.github.project.openubl.xbuilder.content.models.standard.guia.GRETransportista
 */
public final class DespatchAdviceCommonValidator {

    /**
     * Motivos de traslado de comercio exterior (RS 000240-2024/SUNAT).
     */
    static final Set<String> MOTIVOS_COMERCIO_EXTERIOR = new HashSet<>(Arrays.asList(
            "08", // Importación
            "09", // Exportación
            "10", // Importación con DAM
            "19"  // Mercancía extranjera
    ));

    /**
     * Motivos que requieren puerto o aeropuerto.
     */
    static final Set<String> MOTIVOS_CON_PUERTO = new HashSet<>(Arrays.asList(
            "08", "09", "10", "19"
    ));

    private DespatchAdviceCommonValidator() {
        // Utility class
    }

    // ========================================================================
    // Reglas comunes — usadas por los tres validadores
    // ========================================================================

    /**
     * Valida campos básicos del comprobante: serie y número.
     * <p>
     * Regla UBL: serie y número son obligatorios para identificar el documento.
     *
     * @param serie    serie del comprobante
     * @param numero   número del comprobante
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateBasicFields(String serie, Integer numero,
                                           List<ValidationMessage> messages) {
        if (serie == null || serie.isBlank()) {
            messages.add(ValidationMessage.error("La serie es requerida"));
        }
        if (numero == null || numero < 1) {
            messages.add(ValidationMessage.error("El número debe ser mayor a 0"));
        }
    }

    /**
     * Valida que la serie sea coherente con el tipo de comprobante (09 → T*, 31 → V*).
     * <p>
     * Regla SUNAT FAQ #7.
     *
     * @param serie           serie del comprobante
     * @param tipoComprobante tipo de comprobante ("09" o "31")
     * @param messages        lista donde se agregan los mensajes
     */
    public static void validateSerieCoherence(String serie, String tipoComprobante,
                                              List<ValidationMessage> messages) {
        if (serie == null || tipoComprobante == null) {
            return;
        }
        String serieUpper = serie.toUpperCase();
        if ("09".equals(tipoComprobante) && !serieUpper.startsWith("T")) {
            messages.add(ValidationMessage.error(
                    "GRE-Remitente (09) requiere serie que inicie con 'T'. Serie actual: " + serie));
        }
        if ("31".equals(tipoComprobante) && !serieUpper.startsWith("V")) {
            messages.add(ValidationMessage.error(
                    "GRE-Transportista (31) requiere serie que inicie con 'V'. Serie actual: " + serie));
        }
    }

    /**
     * Valida que la serie del GRE-Remitente inicie con 'T'.
     *
     * @param serie    serie del comprobante
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateSerieRemitente(String serie, List<ValidationMessage> messages) {
        if (serie != null && !serie.isBlank() && !serie.toUpperCase().startsWith("T")) {
            messages.add(ValidationMessage.error(
                    "GRE-Remitente requiere serie que inicie con 'T'. Serie actual: " + serie));
        }
    }

    /**
     * Valida que la serie del GRE-Transportista inicie con 'V'.
     *
     * @param serie    serie del comprobante
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateSerieTransportista(String serie, List<ValidationMessage> messages) {
        if (serie != null && !serie.isBlank() && !serie.toUpperCase().startsWith("V")) {
            messages.add(ValidationMessage.error(
                    "GRE-Transportista requiere serie que inicie con 'V'. Serie actual: " + serie));
        }
    }

    /**
     * Valida el RUC del remitente (debe tener 11 dígitos).
     * <p>
     * Regla SUNAT: el RUC es obligatorio y de longitud 11.
     *
     * @param remitente datos del remitente
     * @param messages  lista donde se agregan los mensajes
     */
    public static void validateRemitente(Remitente remitente, List<ValidationMessage> messages) {
        if (remitente == null) {
            messages.add(ValidationMessage.error("El remitente es requerido"));
            return;
        }
        if (remitente.getRuc() == null || remitente.getRuc().length() != 11) {
            messages.add(ValidationMessage.error("El RUC del remitente debe tener 11 dígitos"));
        }
    }

    /**
     * Valida el RUC del transportista emisor (debe tener 11 dígitos).
     *
     * @param transportista datos del transportista emisor
     * @param messages      lista donde se agregan los mensajes
     */
    public static void validateTransportistaEmisor(Transportista transportista,
                                                   List<ValidationMessage> messages) {
        if (transportista == null) {
            messages.add(ValidationMessage.error("El transportista emisor es requerido"));
            return;
        }
        if (transportista.getNumeroDocumentoIdentidad() == null
                || transportista.getNumeroDocumentoIdentidad().length() != 11) {
            messages.add(ValidationMessage.error(
                    "El RUC del transportista emisor debe tener 11 dígitos"));
        }
    }

    /**
     * Valida que el destinatario esté presente.
     *
     * @param destinatario datos del destinatario
     * @param messages     lista donde se agregan los mensajes
     */
    public static void validateDestinatario(Destinatario destinatario,
                                            List<ValidationMessage> messages) {
        if (destinatario == null) {
            messages.add(ValidationMessage.error("El destinatario es requerido"));
        }
    }

    /**
     * Valida que el tercero (remitente original) esté presente en GRE-Transportista.
     * <p>
     * Para GRE-Transportista (31), el tercero identifica al remitente de los bienes.
     * Es un campo requerido por SUNAT en SellerSupplierParty.
     *
     * @param tercero  datos del tercero
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateTerceroTransportista(Tercero tercero,
                                                    List<ValidationMessage> messages) {
        if (tercero == null) {
            messages.add(ValidationMessage.error(
                    "El remitente original (tercero) es requerido para GRE-Transportista"));
        }
    }

    /**
     * Valida los campos obligatorios del envío (shipment).
     * <p>
     * Campos obligatorios según UBL/SUNAT:
     * <ul>
     *   <li>Motivo de traslado (Catálogo 20)</li>
     *   <li>Peso total</li>
     *   <li>Modalidad de traslado (Catálogo 18)</li>
     *   <li>Fecha de traslado</li>
     * </ul>
     *
     * @param envio    datos de envío
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateEnvioRequired(Envio envio, List<ValidationMessage> messages) {
        if (envio == null) {
            messages.add(ValidationMessage.error("Los datos de envío son requeridos"));
            return;
        }
        if (envio.getTipoTraslado() == null || envio.getTipoTraslado().isBlank()) {
            messages.add(ValidationMessage.error(
                    "El motivo de traslado (Catálogo 20) es requerido"));
        }
        if (envio.getPesoTotal() == null) {
            messages.add(ValidationMessage.error("El peso total es requerido"));
        }
        if (envio.getTipoModalidadTraslado() == null) {
            messages.add(ValidationMessage.error(
                    "La modalidad de traslado (Catálogo 18) es requerida"));
        }
        if (envio.getFechaTraslado() == null) {
            messages.add(ValidationMessage.error("La fecha de traslado es requerida"));
        }
    }

    /**
     * Valida punto de partida y destino del envío.
     * <p>
     * Ambos son obligatorios según el esquema UBL de la GRE.
     *
     * @param envio    datos de envío
     * @param messages lista donde se agregan los mensajes
     */
    public static void validatePartidaDestino(Envio envio, List<ValidationMessage> messages) {
        if (envio == null) {
            return;
        }
        if (envio.getPartida() == null) {
            messages.add(ValidationMessage.error("El punto de partida es requerido"));
        }
        if (envio.getDestino() == null) {
            messages.add(ValidationMessage.error("El punto de destino es requerido"));
        }
    }

    /**
     * Valida las reglas de modalidad de transporte para GRE-Remitente (tipo 09).
     * <p>
     * <ul>
     *   <li><b>Transporte privado (02):</b> requiere conductor y vehículo, salvo que se
     *       indique categoría M1/L. No debe consignar transportista externo.</li>
     *   <li><b>Transporte público (01):</b> requiere datos del transportista.</li>
     * </ul>
     *
     * @param envio    datos de envío
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateModalidadRemitente(Envio envio,
                                                  List<ValidationMessage> messages) {
        if (envio == null || envio.getTipoModalidadTraslado() == null) {
            return;
        }
        String modalidad = envio.getTipoModalidadTraslado();

        boolean tieneIndicadorM1L = envio.getIndicadores() != null
                && envio.getIndicadores().contains("SUNAT_Envio_IndicadorTrasladoVehiculoM1L");

        if ("02".equals(modalidad)) {
            // Transporte privado
            if (!tieneIndicadorM1L) {
                if (envio.getChoferes() == null || envio.getChoferes().isEmpty()) {
                    messages.add(ValidationMessage.error(
                            "Transporte privado requiere al menos un conductor "
                                    + "(salvo vehículo categoría M1/L)"));
                }
                if (envio.getVehiculo() == null) {
                    messages.add(ValidationMessage.error(
                            "Transporte privado requiere datos del vehículo "
                                    + "(salvo vehículo categoría M1/L)"));
                }
            }
            if (envio.getTransportista() != null) {
                messages.add(ValidationMessage.error(
                        "Transporte privado no debe consignar transportista externo "
                                + "(usar modalidad pública si subcontrata)"));
            }
        } else if ("01".equals(modalidad)) {
            // Transporte público
            if (envio.getTransportista() == null) {
                messages.add(ValidationMessage.error(
                        "Transporte público requiere datos del transportista"));
            }
        }
    }

    /**
     * Valida las reglas de modalidad de transporte para GRE-Transportista (tipo 31).
     * <p>
     * El transportista siempre requiere al menos un conductor y vehículo,
     * independientemente de la modalidad de traslado.
     *
     * @param conductores lista de conductores
     * @param vehiculo    vehículo principal
     * @param messages    lista donde se agregan los mensajes
     */
    public static void validateConductorVehiculoTransportista(List<Driver> conductores,
                                                              Vehicle vehiculo,
                                                              List<ValidationMessage> messages) {
        if (conductores == null || conductores.isEmpty()) {
            messages.add(ValidationMessage.error(
                    "GRE-Transportista requiere al menos un conductor"));
        }
        if (vehiculo == null) {
            messages.add(ValidationMessage.error(
                    "GRE-Transportista requiere datos del vehículo"));
        }
    }

    /**
     * Valida las reglas de modalidad para un DespatchAdvice genérico (post-conversión).
     * <p>
     * Se usa cuando se valida el modelo después de la conversión desde GRERemitente
     * o GRETransportista. Aplica reglas según el tipo de comprobante detectado.
     *
     * @param tipoComprobante tipo de comprobante ("09" o "31")
     * @param envio           datos de envío
     * @param messages        lista donde se agregan los mensajes
     */
    public static void validateModalidadGeneric(String tipoComprobante, Envio envio,
                                                List<ValidationMessage> messages) {
        if (envio == null || envio.getTipoModalidadTraslado() == null) {
            return;
        }
        String modalidad = envio.getTipoModalidadTraslado();
        boolean isRemitente = "09".equals(tipoComprobante);

        if ("02".equals(modalidad) && isRemitente) {
            boolean tieneM1L = envio.getIndicadores() != null
                    && envio.getIndicadores().contains("SUNAT_Envio_IndicadorTrasladoVehiculoM1L");
            if (!tieneM1L) {
                if (envio.getChoferes() == null || envio.getChoferes().isEmpty()) {
                    messages.add(ValidationMessage.error(
                            "Transporte privado en GRE-Remitente requiere al menos un conductor "
                                    + "(salvo vehículo categoría M1/L)"));
                }
                if (envio.getVehiculo() == null) {
                    messages.add(ValidationMessage.error(
                            "Transporte privado en GRE-Remitente requiere datos del vehículo "
                                    + "(salvo vehículo categoría M1/L)"));
                }
            }
        }

        if ("01".equals(modalidad) && isRemitente) {
            if (envio.getTransportista() == null) {
                messages.add(ValidationMessage.error(
                        "Transporte público en GRE-Remitente requiere datos del transportista"));
            }
        }

        if ("31".equals(tipoComprobante)) {
            if (envio.getChoferes() == null || envio.getChoferes().isEmpty()) {
                messages.add(ValidationMessage.error(
                        "GRE-Transportista requiere al menos un conductor"));
            }
            if (envio.getVehiculo() == null) {
                messages.add(ValidationMessage.error(
                        "GRE-Transportista requiere datos del vehículo"));
            }
        }
    }

    /**
     * Valida reglas de comercio exterior (RS 000240-2024/SUNAT).
     * <p>
     * Cuando el motivo de traslado es de comercio exterior (08, 09, 10, 19):
     * <ul>
     *   <li><b>WARNING:</b> Se recomienda incluir referencia DAM/DS</li>
     *   <li><b>WARNING:</b> Se recomienda incluir puerto o aeropuerto</li>
     * </ul>
     * <p>
     * NOTA: La obligatoriedad plena del motivo 19 y la derogación del ticket de
     * salida fue pospuesta al 01-jul-2026 por RS 000133-2025/SUNAT.
     * Hasta esa fecha se aplica discrecionalidad.
     *
     * @param envio    datos de envío
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateComercioExterior(Envio envio,
                                                List<ValidationMessage> messages) {
        if (envio == null) {
            return;
        }
        String motivo = envio.getTipoTraslado();
        if (motivo == null || !MOTIVOS_COMERCIO_EXTERIOR.contains(motivo)) {
            return;
        }

        boolean tieneDAM = envio.getDeclaracionesAduaneras() != null
                && !envio.getDeclaracionesAduaneras().isEmpty();
        if (!tieneDAM) {
            messages.add(ValidationMessage.warning(
                    "Comercio exterior: se recomienda incluir referencia a DAM/DS "
                            + "(puede no ser obligatorio según FAQ #32; discrecionalidad vigente "
                            + "hasta 01-jul-2026 por RS 000133-2025/SUNAT)"));
        }

        if (MOTIVOS_CON_PUERTO.contains(motivo)
                && envio.getPuerto() == null && envio.getAeropuerto() == null) {
            messages.add(ValidationMessage.warning(
                    "Comercio exterior: se recomienda incluir puerto o aeropuerto "
                            + "para motivos de traslado 08, 09, 10 o 19"));
        }
    }

    /**
     * Valida que haya al menos una línea de detalle.
     * <p>
     * Requerimiento UBL: toda GRE debe tener al menos un ítem.
     * FAQ #24: con indicador de traslado total DAM/DS la línea puede tener solo
     * campos mínimos, pero debe existir.
     *
     * @param detalles lista de ítems
     * @param messages lista donde se agregan los mensajes
     */
    public static void validateDetalles(List<DespatchAdviceItem> detalles,
                                        List<ValidationMessage> messages) {
        if (detalles == null || detalles.isEmpty()) {
            messages.add(ValidationMessage.error(
                    "Se requiere al menos una línea de detalle (requerimiento UBL)"));
        }
    }
}
