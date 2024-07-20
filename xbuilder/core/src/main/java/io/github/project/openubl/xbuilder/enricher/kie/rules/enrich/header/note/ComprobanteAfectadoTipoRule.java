package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.note;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog1;
import io.github.project.openubl.xbuilder.content.models.standard.general.Note;
import io.github.project.openubl.xbuilder.content.models.utils.UBLRegex;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isNote;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenNote;

/**
 * Rule for {@link Note#comprobanteAfectadoTipo}
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class ComprobanteAfectadoTipoRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
                isNote.test(object) &&
                        whenNote.apply(object).map(note -> note.getComprobanteAfectadoSerieNumero() != null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Note> consumer = note -> {
            String comprobanteAfectadoTipo = note.getComprobanteAfectadoTipo();

            if (UBLRegex.FACTURA_SERIE_REGEX.matcher(note.getSerie()).matches()) {
                comprobanteAfectadoTipo = Catalog1.FACTURA.getCode();
            } else if (UBLRegex.BOLETA_SERIE_REGEX.matcher(note.getSerie()).matches()) {
                comprobanteAfectadoTipo = Catalog1.BOLETA.getCode();
            }

            note.setComprobanteAfectadoTipo(comprobanteAfectadoTipo);
        };
        whenNote.apply(object).ifPresent(consumer);
    }
}
