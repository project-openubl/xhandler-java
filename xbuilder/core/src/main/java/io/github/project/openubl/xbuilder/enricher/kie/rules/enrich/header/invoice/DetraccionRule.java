package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.invoice;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog52;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class DetraccionRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isInvoice.test(object) &&
                        whenInvoice.apply(object).map(documento -> documento.getDetraccion() != null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = document -> {
            Catalog52 leyenda = Catalog52.OPERACION_SUJETA_A_DETRACCION;

            Map<String, String> leyendas = new HashMap<>(document.getLeyendas());
            leyendas.put(leyenda.getCode(), leyenda.getLabel());

            document.setLeyendas(leyendas);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
