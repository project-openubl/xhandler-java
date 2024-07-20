package io.github.project.openubl.xbuilder.enricher.kie.rules.process.body.detalle;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.PROCESS)
public class TotalImpuestosRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem
                .apply(object)
                .map(documento -> documento.getTotalImpuestos() == null &&
                        documento.getIgv() != null &&
                        documento.getIcb() != null &&
                        documento.getIsc() != null
                )
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            BigDecimal isc;
            BigDecimal igv;
            BigDecimal icb = detalle.getIcb();

            Catalog7 catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);
            if (catalog7.isOperacionOnerosa()) {
                isc = detalle.getIsc();
                igv = detalle.getIgv();
            } else {
                isc = BigDecimal.ZERO;
                igv = BigDecimal.ZERO;
            }

            BigDecimal totalImpuestos = isc.add(igv).add(icb);
            detalle.setTotalImpuestos(totalImpuestos);
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
