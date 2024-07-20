package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header;

import io.github.project.openubl.xbuilder.content.models.common.Direccion;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocument;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocument;

/**
 * Rule for {@link Proveedor#direccion}
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class ProveedorDireccionRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isSalesDocument.test(object) &&
                        whenSalesDocument.apply(object).map(documento -> documento.getProveedor() != null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<SalesDocument> consumer = document -> {
            if (document.getProveedor().getDireccion() == null) {
                document.getProveedor().setDireccion(Direccion.builder().build());
            }

            if (document.getProveedor().getDireccion().getCodigoLocal() == null) {
                document.getProveedor().getDireccion().setCodigoLocal("0000");
            }
        };
        whenSalesDocument.apply(object).ifPresent(consumer);
    }
}
