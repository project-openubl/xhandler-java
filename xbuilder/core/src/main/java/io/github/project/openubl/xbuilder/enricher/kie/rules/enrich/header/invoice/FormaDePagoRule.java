package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.invoice;

import io.github.project.openubl.xbuilder.content.models.standard.general.FormaDePago;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class FormaDePagoRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isInvoice.test(object) &&
                        whenInvoice.apply(object).map(documento -> documento.getFormaDePago() == null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = document -> {
            document.setFormaDePago(FormaDePago.builder().build());
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
