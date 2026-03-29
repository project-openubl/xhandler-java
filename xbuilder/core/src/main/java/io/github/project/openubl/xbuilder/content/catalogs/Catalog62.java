/*
 * Catálogo 62 - Bienes normalizados sujetos a SPOT/IVAP
 *
 * Fuente normativa: Anexo N.° 8, Catálogo N.° 62 de la RS 000123-2022/SUNAT.
 *
 * Nota SUNAT FAQ #25: Se consideran bienes normalizados los bienes detallados
 * en este catálogo CUANDO se encuentran sujetos al SPOT o IVAP. Si no están
 * sujetos, no califican como bien normalizado y no se marca el indicador.
 */
package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Catálogo N.° 62 - Bienes normalizados sujetos a detracción (SPOT) o IVAP.
 * <p>
 * Se utiliza para marcar el indicador SUNAT_Envio_IndicadorBienNormalizado
 * en el XML de la GRE cuando se transportan estos bienes y están sujetos
 * a SPOT/IVAP.
 */
public enum Catalog62 implements Catalog {

    /** Azúcar - sujeto a SPOT */
    AZUCAR("01"),

    /** Arroz - sujeto a IVAP */
    ARROZ("02"),

    /** Alcohol etílico - sujeto a SPOT */
    ALCOHOL_ETILICO("03"),

    /** Cemento (zonas de control de IQBF) */
    CEMENTO("04");

    private final String code;

    Catalog62(String code) {
        this.code = code;
    }

    public static Optional<Catalog62> valueOfCode(String code) {
        return Stream.of(Catalog62.values()).filter(p -> p.code.equals(code)).findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
