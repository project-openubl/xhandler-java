package io.github.project.openubl.xbuilder.enricher.kie.rules.process.body.detalle;

import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.PROCESS)
public class IgvRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem.apply(object)
                .map(documento -> documento.getIgv() == null &&
                        documento.getIgvBaseImponible() != null &&
                        documento.getTasaIgv() != null
                )
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            BigDecimal igv = detalle.getIgvBaseImponible().multiply(detalle.getTasaIgv());
            detalle.setIgv(igv);
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
