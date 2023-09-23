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
package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.descuento;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53_DescuentoGlobal;
import io.github.project.openubl.xbuilder.content.models.standard.general.Descuento;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isDescuento;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenDescuento;

/**
 * Rule for: {@link Descuento#tipoDescuento}
 *
 * @author <a href="mailto:carlosthe19916@gmail.com">Carlos Feria</a>
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoDescuentoRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return isDescuento.test(object);
    }

    @Override
    public void modify(Object object) {
        Consumer<Descuento> consumer = descuento -> {
            String tipoDescuento;
            if (descuento.getTipoDescuento() == null) {
                tipoDescuento = Catalog53_DescuentoGlobal.DESCUENTO_GLOBAL_NO_AFECTA_BASE_IMPONIBLE_IGV_IVAP.getCode();
            } else {
                Catalog53_DescuentoGlobal catalog53 = Catalog
                        .valueOfCode(Catalog53_DescuentoGlobal.class, descuento.getTipoDescuento())
                        .orElseThrow(Catalog.invalidCatalogValue);
                tipoDescuento = catalog53.getCode();
            }

            descuento.setTipoDescuento(tipoDescuento);
        };
        whenDescuento.apply(object).ifPresent(consumer);
    }
}
