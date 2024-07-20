package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.note.creditnote;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog9;
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.Note;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isCreditNote;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenCreditNote;

/**
 * Rule for {@link Note#tipoNota}
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoNotaCreditoRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isCreditNote.test(object));
    }

    @Override
    public void modify(Object object) {
        Consumer<CreditNote> consumer = note -> {
            if (note.getTipoNota() == null) {
                note.setTipoNota(Catalog9.ANULACION_DE_LA_OPERACION.getCode());
            } else {
                Catalog
                        .valueOfCode(Catalog9.class, note.getTipoNota())
                        .ifPresent(catalog6 -> note.setTipoNota(catalog6.getCode()));
            }
        };
        whenCreditNote.apply(object).ifPresent(consumer);
    }
}
