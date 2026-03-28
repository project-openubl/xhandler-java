package io.github.project.openubl.xbuilder.content.models.standard.guia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validador de reglas de negocio para la Guía de Remisión Electrónica (GRE).
 * <p>
 * Implementa las reglas funcionales de SUNAT según:
 * <ul>
 * <li>RS 000123-2022/SUNAT — Reglas base GRE-Remitente y GRE-Transportista</li>
 * <li>RS 000240-2024/SUNAT — Reglas de comercio exterior (vigentes desde
 * 14-nov-2024)</li>
 * <li>RS 000133-2025/SUNAT — Prórroga: derogatoria del ticket de salida al
 * 01-jul-2026</li>
 * </ul>
 * <p>
 * Las validaciones retornan una lista de mensajes de error. Si la lista está
 * vacía,
 * el documento es válido respecto a las reglas implementadas.
 * <p>
 * <b>IMPORTANTE:</b> Este validador NO reemplaza la validación de SUNAT
 * (XSD/XSL).
 * Es una capa de validación temprana para detectar errores comunes antes del
 * envío.
 */
public class DespatchAdviceValidator {

    /**
     * Motivos de traslado que requieren referencia a DAM/DS (comercio exterior).
     */
    private static final Set<String> MOTIVOS_COMERCIO_EXTERIOR = new HashSet<>(Arrays.asList(
            "08", // Importación
            "09", // Exportación
            "10", // Importación con DAM (RS 240-2024)
            "19" // Mercancía extranjera (RS 240-2024)
    ));

    /** Motivos de traslado que requieren puerto o aeropuerto. */
    private static final Set<String> MOTIVOS_CON_PUERTO = new HashSet<>(Arrays.asList(
            "08", // Importación
            "09", // Exportación
            "10", // Importación con DAM
            "19" // Mercancía extranjera
    ));

    /**
     * Valida un DespatchAdvice completo y retorna la lista de errores encontrados.
     *
     * @param da el DespatchAdvice a validar
     * @return lista de mensajes de error (vacía si es válido)
     */
    public static List<String> validate(DespatchAdvice da) {
        List<String> errors = new ArrayList<>();

        validateBasicFields(da, errors);
        validateSerie(da, errors);
        validateParties(da, errors);
        validateEnvio(da, errors);
        validateDetalles(da, errors);

        return errors;
    }

    private static void validateBasicFields(DespatchAdvice da, List<String> errors) {
        if (da.getSerie() == null || da.getSerie().isBlank()) {
            errors.add("La serie es requerida");
        }
        if (da.getNumero() == null || da.getNumero() < 1) {
            errors.add("El número debe ser mayor a 0");
        }
    }

    /**
     * Valida la coherencia entre la serie y el tipo de comprobante.
     * <p>
     * Regla SUNAT FAQ #7:
     * - GRE-Remitente: Serie TXXX
     * - GRE-Transportista: Serie VXXX
     */
    private static void validateSerie(DespatchAdvice da, List<String> errors) {
        if (da.getSerie() == null || da.getTipoComprobante() == null) {
            return;
        }
        String serie = da.getSerie().toUpperCase();
        String tipo = da.getTipoComprobante();

        if ("09".equals(tipo) && !serie.startsWith("T")) {
            errors.add("GRE-Remitente (09) requiere serie que inicie con 'T'. Serie actual: " + da.getSerie());
        }
        if ("31".equals(tipo) && !serie.startsWith("V")) {
            errors.add("GRE-Transportista (31) requiere serie que inicie con 'V'. Serie actual: " + da.getSerie());
        }
    }

    /**
     * Valida las partes (remitente, destinatario, tercero) según el tipo de GRE.
     */
    private static void validateParties(DespatchAdvice da, List<String> errors) {
        if (da.getRemitente() == null) {
            errors.add("El remitente es requerido");
            return;
        }
        if (da.getRemitente().getRuc() == null || da.getRemitente().getRuc().length() != 11) {
            errors.add("El RUC del remitente debe tener 11 dígitos");
        }

        if (da.getDestinatario() == null) {
            // FAQ #22: Para emisor itinerante, el destinatario puede ser el mismo
            // remitente.
            // Pero el campo sigue siendo obligatorio en el XML.
            errors.add("El destinatario es requerido");
        }

        // GRE-Transportista (31): el tercero (remitente original) debería estar
        // presente
        if ("31".equals(da.getTipoComprobante()) && da.getTercero() == null && da.getProveedor() == null) {
            errors.add("GRE-Transportista (31): se recomienda consignar el tercero (remitente original) " +
                    "o proveedor en SellerSupplierParty");
        }
    }

    /**
     * Valida los datos de envío/shipment según las reglas funcionales SUNAT.
     */
    private static void validateEnvio(DespatchAdvice da, List<String> errors) {
        Envio envio = da.getEnvio();
        if (envio == null) {
            errors.add("Los datos de envío son requeridos");
            return;
        }

        if (envio.getTipoTraslado() == null || envio.getTipoTraslado().isBlank()) {
            errors.add("El motivo de traslado (Catálogo 20) es requerido");
        }
        if (envio.getPesoTotal() == null) {
            errors.add("El peso total es requerido");
        }
        if (envio.getTipoModalidadTraslado() == null) {
            errors.add("La modalidad de traslado (Catálogo 18) es requerida");
        }
        if (envio.getFechaTraslado() == null) {
            errors.add("La fecha de traslado es requerida");
        }

        validateEnvioModalidad(da, envio, errors);
        validateEnvioComercioExterior(envio, errors);
    }

    /**
     * Valida reglas de modalidad de transporte.
     * <p>
     * Transporte privado (02): requiere conductor y vehículo.
     * Transporte público (01): requiere datos del transportista.
     */
    private static void validateEnvioModalidad(DespatchAdvice da, Envio envio, List<String> errors) {
        String modalidad = envio.getTipoModalidadTraslado();
        if (modalidad == null)
            return;

        boolean isGRERemitente = "09".equals(da.getTipoComprobante());

        if ("02".equals(modalidad) && isGRERemitente) {
            // Transporte privado en GRE-Remitente: conductor y vehículo requeridos
            boolean tieneIndicadorM1L = envio.getIndicadores() != null &&
                    envio.getIndicadores().contains("SUNAT_Envio_IndicadorTrasladoVehiculoM1L");

            if (!tieneIndicadorM1L) {
                if (envio.getChoferes() == null || envio.getChoferes().isEmpty()) {
                    errors.add("Transporte privado en GRE-Remitente requiere al menos un conductor " +
                            "(salvo vehículo categoría M1/L)");
                }
                if (envio.getVehiculo() == null) {
                    errors.add("Transporte privado en GRE-Remitente requiere datos del vehículo " +
                            "(salvo vehículo categoría M1/L)");
                }
            }
        }

        if ("01".equals(modalidad) && isGRERemitente) {
            // Transporte público en GRE-Remitente: transportista requerido
            if (envio.getTransportista() == null) {
                errors.add("Transporte público en GRE-Remitente requiere datos del transportista");
            }
        }

        // GRE-Transportista (31): siempre requiere conductor y vehículo
        if ("31".equals(da.getTipoComprobante())) {
            if (envio.getChoferes() == null || envio.getChoferes().isEmpty()) {
                errors.add("GRE-Transportista requiere al menos un conductor");
            }
            if (envio.getVehiculo() == null) {
                errors.add("GRE-Transportista requiere datos del vehículo");
            }
        }
    }

    /**
     * Valida reglas de comercio exterior (RS 000240-2024/SUNAT).
     * <p>
     * Cuando el motivo de traslado es de comercio exterior:
     * <ul>
     * <li>Se recomienda incluir referencia DAM/DS</li>
     * <li>Se recomienda incluir puerto o aeropuerto</li>
     * <li>Se recomienda incluir contenedores</li>
     * </ul>
     * <p>
     * NOTA: Estas reglas son actualmente de recomendación. La obligatoriedad plena
     * del motivo 19 y la derogación del ticket de salida fue pospuesta al
     * 01-jul-2026
     * por RS 000133-2025/SUNAT. Hasta esa fecha se aplica discrecionalidad.
     */
    private static void validateEnvioComercioExterior(Envio envio, List<String> errors) {
        String motivo = envio.getTipoTraslado();
        if (motivo == null || !MOTIVOS_COMERCIO_EXTERIOR.contains(motivo)) {
            return;
        }

        // Advertencia suave: no es error pero se recomienda
        boolean tieneDAM = (envio.getDeclaracionesAduaneras() != null && !envio.getDeclaracionesAduaneras().isEmpty());
        if (!tieneDAM) {
            // No se marca como error porque puede ser traslado para exportación sin DAM
            // numerada (FAQ #32)
            // Este es un caso donde la ambigüedad normativa indica que se puede usar motivo
            // "otros"
        }

        // Puerto/aeropuerto recomendado para comercio exterior
        if (MOTIVOS_CON_PUERTO.contains(motivo) && envio.getPuerto() == null && envio.getAeropuerto() == null) {
            // No error estricto, pero recomendable
        }
    }

    /**
     * Valida los detalles/líneas del documento.
     * <p>
     * Siempre debe haber al menos 1 línea (requerimiento UBL).
     * FAQ #24: Con indicador de traslado total DAM/DS, la línea puede estar vacía
     * (campos mínimos obligatorios por UBL).
     */
    private static void validateDetalles(DespatchAdvice da, List<String> errors) {
        if (da.getDetalles() == null || da.getDetalles().isEmpty()) {
            errors.add("Se requiere al menos una línea de detalle (requerimiento UBL)");
        }
    }

    private DespatchAdviceValidator() {
        // Utility class
    }
}
