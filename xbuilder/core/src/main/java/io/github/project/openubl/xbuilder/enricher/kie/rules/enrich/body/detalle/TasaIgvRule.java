package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.detalle;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocumentItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocumentItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TasaIgvRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem.apply(object)
                .map(documento -> documento.getTasaIgv() == null && documento.getIgvTipo() != null)
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            BigDecimal igvTasa;

            Catalog7 catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);
            if (catalog7.equals(Catalog7.GRAVADO_IVAP)) {
                igvTasa = getRuleContext().getTasaIvap();
            } else {
                switch (catalog7.getGrupo()) {
                    case EXPORTACION:
                    case EXONERADO:
                    case INAFECTO: {
                        igvTasa = BigDecimal.ZERO;
                        break;
                    }
                    default: {
                        igvTasa = getRuleContext().getTasaIgv();
                        break;
                    }
                }
            }

            detalle.setTasaIgv(igvTasa);
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }
}
