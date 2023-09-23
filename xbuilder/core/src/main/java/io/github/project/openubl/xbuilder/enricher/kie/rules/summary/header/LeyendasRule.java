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
package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isSalesDocument;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenSalesDocument;

@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class LeyendasRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isSalesDocument.test(object));
    }

    @Override
    public void modify(Object object) {
        Consumer<SalesDocument> consumer = document -> {
            Map<String, String> leyendas = new HashMap<>(document.getLeyendas());

            document.getDetalles().stream()
                    .filter(detalle -> {
                        Catalog7 catalog7 = Catalog.valueOfCode(Catalog7.class, detalle.getIgvTipo()).orElseThrow(Catalog.invalidCatalogValue);
                        return catalog7.equals(Catalog7.GRAVADO_IVAP);
                    }).findAny().ifPresent(detalle -> {
                        leyendas.put("2007", "Leyenda: Operacion sujeta a IVAP");
                    });

            document.setLeyendas(leyendas);
        };
        whenSalesDocument.apply(object).ifPresent(consumer);
    }
}
