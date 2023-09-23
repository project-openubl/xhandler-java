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
package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.summaryDocumentItem;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog19;
import io.github.project.openubl.xbuilder.content.models.sunat.resumen.SummaryDocumentsItem;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSummaryDocumentsItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSummaryDocumentsItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoOperacionRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (
                isSummaryDocumentsItem.test(object) &&
                        whenSummaryDocumentsItem.apply(object).map(item -> item.getTipoOperacion() != null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<SummaryDocumentsItem> consumer = item -> {
            Catalog.valueOfCode(Catalog19.class, item.getTipoOperacion())
                    .ifPresent(catalog -> item.setTipoOperacion(catalog.getCode()));
        };
        whenSummaryDocumentsItem.apply(object).ifPresent(consumer);
    }
}
