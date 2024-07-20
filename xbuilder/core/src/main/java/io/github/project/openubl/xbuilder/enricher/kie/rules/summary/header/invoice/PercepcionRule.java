package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header.invoice;

import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class PercepcionRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isInvoice.test(object) &&
                        whenInvoice
                                .apply(object)
                                .map(invoice -> invoice.getPercepcion() != null && invoice.getTotalImporte() != null)
                                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = invoice -> {
            BigDecimal montoBase = invoice.getTotalImporte().getImporteSinImpuestos();
            BigDecimal porcentaje = Optional.ofNullable(invoice.getPercepcion().getPorcentaje()).orElse(BigDecimal.ONE);
            BigDecimal monto = montoBase.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal montoTotal = montoBase.add(monto);

            invoice.getPercepcion().setMontoBase(montoBase);
            invoice.getPercepcion().setPorcentaje(porcentaje);
            invoice.getPercepcion().setMonto(monto);
            invoice.getPercepcion().setMontoTotal(montoTotal);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
