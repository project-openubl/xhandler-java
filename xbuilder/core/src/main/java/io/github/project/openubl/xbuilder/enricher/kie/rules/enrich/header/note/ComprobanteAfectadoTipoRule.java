/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
