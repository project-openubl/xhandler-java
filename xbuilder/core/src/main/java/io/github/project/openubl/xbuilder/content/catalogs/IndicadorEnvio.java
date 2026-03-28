/*
 * Indicadores especiales de envío para la GRE
 *
 * Fuente normativa: Anexo N.° 14, UBL 2.1 de la RS 000123-2022/SUNAT.
 * Estos indicadores se consignan como cbc:SpecialInstructions dentro de cac:Shipment.
 *
 * Nota: No todos los indicadores aplican a ambos tipos de GRE.
 * La columna "Aplica GRE-Remitente / GRE-Transportista" se documenta en cada valor.
 */
package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Indicadores especiales de envío (SpecialInstructions) para la GRE.
 * <p>
 * Se mapean a {@code cbc:SpecialInstructions} dentro de {@code cac:Shipment}.
 * Cada indicador tiene un código literal que debe coincidir exactamente con
 * lo que valida SUNAT.
 */
public enum IndicadorEnvio implements Catalog {

    /**
     * Indica que el traslado involucra la totalidad de la DAM/DS.
     * <p>
     * Aplica: GRE-Remitente (motivos 08, 09, 10, 19).
     * Si se marca, no se requiere detalle de ítems (se acepta línea vacía
     * obligatoria por UBL).
     * Ref: FAQ #24 SUNAT.
     */
    TRASLADO_TOTAL_DAM_DS("SUNAT_Envio_IndicadorTrasladoTotalDAMDS"),

    /**
     * Indica que los bienes trasladados son bienes normalizados sujetos a
     * SPOT/IVAP.
     * <p>
     * Aplica: GRE-Remitente.
     * Condición: solo se marca si el bien está en el Catálogo 62 Y está sujeto a
     * SPOT o IVAP.
     * Ref: FAQ #25 SUNAT - si un bien del catálogo 62 NO está sujeto a SPOT o IVAP,
     * NO califica como bien normalizado.
     */
    BIEN_NORMALIZADO("SUNAT_Envio_IndicadorBienNormalizado"),

    /**
     * Indica que el traslado es en vehículos de categoría M1 o L (vehículos
     * menores).
     * <p>
     * Aplica: GRE-Remitente (transporte privado).
     * Cuando se marca, no se requiere consignar placa ni conductor.
     * <p>
     * <b>PENDIENTE DE VALIDACIÓN:</b> El token exacto de este indicador debe
     * confirmarse contra el Anexo N.° 14 o el catálogo interno del portal SUNAT.
     * La separación conceptual respecto de {@link #TRANSBORDO_PROGRAMADO} es
     * correcta.
     * <p>
     * Ref: Anexo N.° 14, RS 000123-2022/SUNAT — campo cbc:SpecialInstructions.
     */
    VEHICULO_M1_L("SUNAT_Envio_IndicadorTrasladoVehiculoM1L"),

    /**
     * Indica que se ha producido un transbordo programado durante el trayecto.
     * <p>
     * Este indicador corresponde a la GRE por eventos: se utiliza cuando ocurre
     * un hecho no imputable al emisor que obliga a un transbordo o reinicio del
     * traslado. NO es equivalente a vehículo categoría M1/L.
     * <p>
     * Aplica: GRE-Remitente, GRE-Transportista (GRE complementaria por eventos).
     * Ref: Numeral 4, Anexo RS 000123-2022/SUNAT — GRE por eventos.
     */
    TRANSBORDO_PROGRAMADO("SUNAT_Envio_IndicadorTransbordoProgramado"),

    /**
     * Indica que el retorno del vehículo está programado y la GRE ampara el
     * retorno.
     * <p>
     * Aplica: GRE-Remitente.
     */
    RETORNO_VEHICULO_ENVASES("SUNAT_Envio_IndicadorRetornoVehiculoEnvasesVacios"),

    /**
     * Indica que el retorno del vehículo con envases vacíos está programado.
     * <p>
     * Aplica: GRE-Remitente.
     */
    RETORNO_VEHICULO_VACIO("SUNAT_Envio_IndicadorRetornoVehiculoVacio"),

    /**
     * Indica que el traslado es operación de importación de bienes en zona
     * primaria.
     * <p>
     * Aplica: GRE-Remitente (motivo 19 mercancía extranjera).
     * Incorporado por RS 000240-2024/SUNAT para trazabilidad.
     * Vigente desde 14-nov-2024; obligatoriedad plena pospuesta al 01-jul-2026.
     */
    TRASLADO_ZONA_PRIMARIA_COMEXT("SUNAT_Envio_IndicadorTrasladoVehiculoPesadoCarga");

    private final String code;

    IndicadorEnvio(String code) {
        this.code = code;
    }

    public static Optional<IndicadorEnvio> valueOfCode(String code) {
        return Stream.of(IndicadorEnvio.values()).filter(p -> p.code.equals(code)).findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
