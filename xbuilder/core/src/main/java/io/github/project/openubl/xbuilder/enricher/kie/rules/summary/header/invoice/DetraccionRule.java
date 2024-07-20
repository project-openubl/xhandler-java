package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header.invoice;

import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class DetraccionRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isInvoice.test(object) &&
                        whenInvoice
                                .apply(object)
                                .map(invoice -> invoice.getTotalImporte() != null && invoice.getDetraccion() != null)
                                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = invoice -> {
            BigDecimal detraccionMonto = invoice
                    .getDetraccion()
                    .getPorcentaje()
                    .multiply(invoice.getTotalImporte().getImporteConImpuestos())
                    .setScale(2, RoundingMode.HALF_EVEN);

            invoice.getDetraccion().setMonto(detraccionMonto);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
