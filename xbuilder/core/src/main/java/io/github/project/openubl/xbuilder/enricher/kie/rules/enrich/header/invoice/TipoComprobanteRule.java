package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.invoice;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog1_Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocument;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoComprobanteRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isInvoice.test(object) &&
                        whenSalesDocument.apply(object).map(documento -> documento.getSerie() != null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = invoice -> {
            String newTipoComprobante = null;
            if (invoice.getSerie().matches("^[F|f].*$")) {
                newTipoComprobante = Catalog1_Invoice.FACTURA.getCode();
            } else if (invoice.getSerie().matches("^[B|b].*$")) {
                newTipoComprobante = Catalog1_Invoice.BOLETA.getCode();
            }

            invoice.setTipoComprobante(newTipoComprobante);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
