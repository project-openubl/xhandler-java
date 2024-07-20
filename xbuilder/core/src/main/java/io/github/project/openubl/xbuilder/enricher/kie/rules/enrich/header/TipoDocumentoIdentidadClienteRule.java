package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocument;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocument;

/**
 * Rule for {@link Cliente#tipoDocumentoIdentidad}
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoDocumentoIdentidadClienteRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isSalesDocument.test(object) &&
                        whenSalesDocument
                                .apply(object)
                                .map(documento ->
                                        documento.getCliente() != null && documento.getCliente().getTipoDocumentoIdentidad() != null
                                )
                                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<SalesDocument> consumer = document -> {
            Catalog
                    .valueOfCode(Catalog6.class, document.getCliente().getTipoDocumentoIdentidad())
                    .ifPresent(catalog6 -> document.getCliente().setTipoDocumentoIdentidad(catalog6.getCode()));
        };
        whenSalesDocument.apply(object).ifPresent(consumer);
    }
}
