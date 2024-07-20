package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header;

import io.github.project.openubl.xbuilder.content.models.common.Document;
import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import io.github.project.openubl.xbuilder.content.models.standard.guia.DespatchAdvice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDespatchAdvice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDocument;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDespatchAdvice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDocument;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class FirmanteRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isDocument.test(object) && whenDocument.apply(object)
                .map(documento -> documento.getProveedor() != null)
                .orElse(false)
        ) || (isDespatchAdvice.test(object) && whenDespatchAdvice.apply(object)
                .map(documento -> documento.getRemitente() != null)
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Document> consumer1 = document -> {
            if (document.getFirmante() == null) {
                document.setFirmante(Firmante.builder().build());
            }

            if (document.getFirmante().getRuc() == null) {
                document.getFirmante().setRuc(document.getProveedor().getRuc());
            }
            if (document.getFirmante().getRazonSocial() == null) {
                document.getFirmante().setRazonSocial(document.getProveedor().getRazonSocial());
            }
        };
        Consumer<DespatchAdvice> consumer2 = document -> {
            if (document.getFirmante() == null) {
                document.setFirmante(Firmante.builder().build());
            }

            if (document.getFirmante().getRuc() == null) {
                document.getFirmante().setRuc(document.getRemitente().getRuc());
            }
            if (document.getFirmante().getRazonSocial() == null) {
                document.getFirmante().setRazonSocial(document.getRemitente().getRazonSocial());
            }
        };

        whenDocument.apply(object).ifPresent(consumer1);
        whenDespatchAdvice.apply(object).ifPresent(consumer2);
    }
}
