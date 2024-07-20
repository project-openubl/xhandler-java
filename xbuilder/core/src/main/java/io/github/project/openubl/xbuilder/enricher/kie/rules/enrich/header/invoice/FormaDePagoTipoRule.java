package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.invoice;

import io.github.project.openubl.xbuilder.content.catalogs.CatalogContadoCredito;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class FormaDePagoTipoRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return isInvoice.test(object);
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = document -> {
            CatalogContadoCredito formaDePagoTipo = document.getFormaDePago().getCuotas() == null ||
                    document.getFormaDePago().getCuotas().isEmpty()
                    ? CatalogContadoCredito.CONTADO
                    : CatalogContadoCredito.CREDITO;

            document.getFormaDePago().setTipo(formaDePagoTipo.getCode());
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
