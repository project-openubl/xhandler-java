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
package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.body.voidedDocumentItem;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog1;
import io.github.project.openubl.xbuilder.content.models.sunat.baja.VoidedDocumentsItem;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractBodyRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;

import java.util.function.Consumer;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isVoidedDocumentsItem;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenVoidedDocumentsItem;

@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class TipoComprobanteRule extends AbstractBodyRule {

    @Override
    public boolean test(Object object) {
        return (
                isVoidedDocumentsItem.test(object) &&
                        whenVoidedDocumentsItem.apply(object).map(item -> item.getTipoComprobante() == null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<VoidedDocumentsItem> consumer = item -> {
            String newTipoComprobante = null;
            if (item.getSerie().matches("^[F|f].*$")) {
                newTipoComprobante = Catalog1.FACTURA.getCode();
            } else if (item.getSerie().matches("^[B|b].*$")) {
                newTipoComprobante = Catalog1.BOLETA.getCode();
            } else if (item.getSerie().matches("^[T|t].*$")) {
                newTipoComprobante = Catalog1.GUIA_REMISION_TRANSPORTISTA.getCode();
            } else if (item.getSerie().matches("^[P|p].*$")) {
                newTipoComprobante = Catalog1.PERCEPCION.getCode();
            } else if (item.getSerie().matches("^[R|r].*$")) {
                newTipoComprobante = Catalog1.RETENCION.getCode();
            }

            item.setTipoComprobante(newTipoComprobante);
        };
        whenVoidedDocumentsItem.apply(object).ifPresent(consumer);
    }
}
