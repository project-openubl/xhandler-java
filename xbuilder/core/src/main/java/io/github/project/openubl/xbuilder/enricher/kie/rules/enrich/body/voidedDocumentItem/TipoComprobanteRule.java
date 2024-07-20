package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.voidedDocumentItem;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog1;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocumentsItem;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isVoidedDocumentsItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenVoidedDocumentsItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoComprobanteRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (
                isVoidedDocumentsItem.test(object) &&
                        whenVoidedDocumentsItem.apply(object).map(item -> item.getTipoComprobante() == null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<VoidedDocumentsItem> consumer = item -> {
            String newTipoComprobante = null;
            if (item.getSerie().matches("^[F|f].*$")) {
                newTipoComprobante = Catalog1.FACTURA.getCode();
            } else if (item.getSerie().matches("^[B|b].*$")) {
                newTipoComprobante = Catalog1.BOLETA.getCode();
            } else if (item.getSerie().matches("^[T|t].*$")) {
                newTipoComprobante = Catalog1.GUIA_REMISION_TRANSPORTISTA.getCode();
            } else if (item.getSerie().matches("^[P|p].*$")) {
                newTipoComprobante = Catalog1.PERCEPCION.getCode();
            } else if (item.getSerie().matches("^[R|r].*$")) {
                newTipoComprobante = Catalog1.RETENCION.getCode();
            }

            item.setTipoComprobante(newTipoComprobante);
        };
        whenVoidedDocumentsItem.apply(object).ifPresent(consumer);
    }
}
