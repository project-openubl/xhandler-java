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
public class TasaIscRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocumentItem.test(object) && whenSalesDocumentItem.apply(object)
                .map(documento -> documento.getIgvTipo() != null)
                .orElse(false)
        );
    }

    // TODO Respect user's tasaISC and not overwrite it?
    @Override
    public void modify(Object object) {
        Consumer<DocumentoVentaDetalle> consumer = detalle -> {
            BigDecimal iscTasa;
            if (detalle.getTasaIsc() == null) {
                iscTasa = BigDecimal.ZERO;
            } else {
                Catalog7 catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);
                if (!catalog7.isOperacionOnerosa()) {
                    iscTasa = BigDecimal.ZERO;
                } else {
                    switch (catalog7.getGrupo()) {
                        case EXPORTACION:
                        case EXONERADO:
                        case INAFECTO: {
                            iscTasa = BigDecimal.ZERO;
                            break;
                        }
                        default: {
                            iscTasa = detalle.getTasaIsc();
                            break;
                        }
                    }
                }
            }
            detalle.setTasaIsc(iscTasa);
        };
        whenSalesDocumentItem.apply(object).ifPresent(consumer);
    }

}
