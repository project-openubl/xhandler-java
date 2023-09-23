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
package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header.note;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog5;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.SalesDocument;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImpuestos;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import io.github.project.openubl.xbuilder.enricher.kie.rules.utils.DetalleUtils;
import io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Impuesto;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isNote;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenNote;

@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class TotalImpuestosRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isNote.test(object) && whenNote
                .apply(object)
                .map(documento -> documento.getTotalImpuestos() == null &&
                        documento.getDetalles() != null
                )
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<SalesDocument> consumer = document -> {
            Impuesto ivap = DetalleUtils.calImpuestoByTipo(document.getDetalles(), Catalog5.IMPUESTO_ARROZ_PILADO);
            Impuesto exportacion = DetalleUtils.calImpuestoByTipo(document.getDetalles(), Catalog5.EXPORTACION);
            Impuesto gravado = DetalleUtils.calImpuestoByTipo(document.getDetalles(), Catalog5.IGV);
            Impuesto inafecto = DetalleUtils.calImpuestoByTipo(document.getDetalles(), Catalog5.INAFECTO);
            Impuesto exonerado = DetalleUtils.calImpuestoByTipo(document.getDetalles(), Catalog5.EXONERADO);
            Impuesto gratuito = DetalleUtils.calImpuestoByTipo(document.getDetalles(), Catalog5.GRATUITO);

            // ICB
            BigDecimal icbImporte = Stream.of(
                            ivap.getImporteIcb(),
                            exportacion.getImporteIcb(),
                            gravado.getImporteIcb(),
                            inafecto.getImporteIcb(),
                            exonerado.getImporteIcb(),
                            gratuito.getImporteIcb()
                    )
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ISC
            BigDecimal iscImporte = Stream.of(
                            ivap.getImporteIsc(),
                            exportacion.getImporteIsc(),
                            gravado.getImporteIsc(),
                            inafecto.getImporteIsc(),
                            exonerado.getImporteIsc(),
                            gratuito.getImporteIsc()
                    )
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal iscBaseImponible = document.getDetalles().stream()
                    .filter(d -> d.getTasaIsc().compareTo(BigDecimal.ZERO) != 0)
                    .map(DocumentoVentaDetalle::getIscBaseImponible)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Set final values
            BigDecimal total = gravado.getImporte()
                    .add(ivap.getImporte())
                    .add(exportacion.getImporte());

            TotalImpuestos totalImpuestos = TotalImpuestos.builder()
                    // IVAP
                    .ivapImporte(ivap.getImporteIgv())
                    .ivapBaseImponible(ivap.getBaseImponible())

                    // EXPORTACION
                    .exportacionImporte(exportacion.getImporteIgv())
                    .exportacionBaseImponible(exportacion.getBaseImponible())

                    // ISC and GRAVADO go together
                    .iscImporte(iscImporte)
                    .iscBaseImponible(iscBaseImponible)
                    .gravadoImporte(gravado.getImporteIgv()) //
                    .gravadoBaseImponible(gravado.getBaseImponible()) //

                    // INAFECTO, EXONERADO, GRATUITO
                    .inafectoImporte(inafecto.getImporte())
                    .inafectoBaseImponible(inafecto.getBaseImponible())
                    .exoneradoImporte(exonerado.getImporte())
                    .exoneradoBaseImponible(exonerado.getBaseImponible())
                    .gratuitoImporte(gratuito.getImporte())
                    .gratuitoBaseImponible(gratuito.getBaseImponible())

                    // ICB
                    .icbImporte(icbImporte)

                    .total(total)
                    .build();

            document.setTotalImpuestos(totalImpuestos);
        };
        whenNote.apply(object).ifPresent(consumer);
    }
}
