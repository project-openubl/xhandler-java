package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.invoice;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog51;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoOperacionRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isInvoice.test(object) &&
                        whenInvoice.apply(object).map(documento -> documento.getTipoOperacion() == null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = invoice -> {
            String tipoOperacion = null;
            if (invoice.getTipoOperacion() == null) {
                if (invoice.getDetraccion() != null) {
                    tipoOperacion = Catalog51.OPERACION_SUJETA_A_DETRACCION.getCode();
                } else if (invoice.getPercepcion() != null) {
                    tipoOperacion = Catalog51.OPERACION_SUJETA_A_PERCEPCION.getCode();
                } else {
                    tipoOperacion = Catalog51.VENTA_INTERNA.getCode();
                }
            }

            invoice.setTipoOperacion(tipoOperacion);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
