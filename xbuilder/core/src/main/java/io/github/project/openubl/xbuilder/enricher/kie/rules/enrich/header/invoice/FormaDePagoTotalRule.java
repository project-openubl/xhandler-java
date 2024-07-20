package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.invoice;

import io.github.project.openubl.xbuilder.content.models.standard.general.CuotaDePago;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class FormaDePagoTotalRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isInvoice.test(object) &&
                        whenInvoice
                                .apply(object)
                                .map(documento -> documento.getFormaDePago() != null && documento.getFormaDePago().getCuotas() != null)
                                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = document -> {
            BigDecimal total = document
                    .getFormaDePago()
                    .getCuotas()
                    .stream()
                    .map(CuotaDePago::getImporte)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            document.getFormaDePago().setTotal(total);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
