package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.note.debitnote;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog10;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.Note;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDebitNote;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDebitNote;

/**
 * Rule for {@link Note#tipoNota}
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoNotaDebitoRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isDebitNote.test(object));
    }

    @Override
    public void modify(Object object) {
        Consumer<DebitNote> consumer = note -> {
            if (note.getTipoNota() == null) {
                note.setTipoNota(Catalog10.AUMENTO_EN_EL_VALOR.getCode());
            } else {
                Catalog
                        .valueOfCode(Catalog10.class, note.getTipoNota())
                        .ifPresent(catalog10 -> note.setTipoNota(catalog10.getCode()));
            }
        };
        whenDebitNote.apply(object).ifPresent(consumer);
    }
}
