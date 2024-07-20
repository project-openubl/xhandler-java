package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header.note;

import io.github.project.openubl.xbuilder.content.models.standard.general.Note;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteNote;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import io.github.project.openubl.xbuilder.enricher.kie.rules.utils.DetalleUtils;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isNote;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenNote;

/**
 * Rule for {@link Note#totalImporte}
 */
@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class TotalImporteRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isNote.test(object) && whenNote.apply(object)
                .map(note -> note.getTotalImporte() == null &&
                        note.getDetalles() != null
                )
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Note> consumer = note -> {
            BigDecimal importeSinImpuestos = DetalleUtils.getImporteSinImpuestos(note.getDetalles());
            BigDecimal totalImpuestos = DetalleUtils.getTotalImpuestos(note.getDetalles());

            BigDecimal importe = importeSinImpuestos.add(totalImpuestos);

            note.setTotalImporte(TotalImporteNote.builder()
                    .importe(importe)
                    .importeSinImpuestos(importeSinImpuestos)
                    .build()
            );
        };
        whenNote.apply(object).ifPresent(consumer);
    }
}
