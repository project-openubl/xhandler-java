package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header;

import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDespatchAdvice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDespatchAdvice;

/**
 * Regla de enriquecimiento para autodetectar el tipo de comprobante de la GRE
 * a partir del prefijo de la serie.
 * <p>
 * Regla funcional SUNAT:
 * <ul>
 * <li>Serie T* → GRE-Remitente (tipo "09")</li>
 * <li>Serie V* → GRE-Transportista (tipo "31")</li>
 * </ul>
 * <p>
 * Esta regla solo se aplica si {@code tipoComprobante} no fue establecido
 * explícitamente por el usuario, permitiendo autocompletar.
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class DespatchAdviceTipoComprobanteRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return isDespatchAdvice.test(object) && whenDespatchAdvice.apply(object)
                .map(da -> da.getTipoComprobante() == null && da.getSerie() != null)
                .orElse(false);
    }

    @Override
    public void modify(Object object) {
        whenDespatchAdvice.apply(object).ifPresent(da -> {
            String serie = da.getSerie().toUpperCase();
            if (serie.startsWith("T")) {
                da.setTipoComprobante("09");
            } else if (serie.startsWith("V")) {
                da.setTipoComprobante("31");
            }
        });
    }
}
