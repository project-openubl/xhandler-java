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
package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog12;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog12_Anticipo;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog5;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocumentLine;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoRelacionado;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImpuestos;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.github.project.openubl.xbuilder.content.jaxb.mappers.utils.MapperUtils.mapPorcentaje;

public interface SalesDocumentHelperMapper {

    default Map<String, String> mapLeyendas(List<XMLSalesDocument.Note> xml) {
        return Optional.ofNullable(xml)
                .map(o -> o.stream()
                        .filter(note -> note.getLanguageLocaleId() != null)
                        .collect(Collectors.toMap(XMLSalesDocument.Note::getLanguageLocaleId, XMLSalesDocument.Note::getValue))
                )
                .orElse(Collections.emptyMap());
    }

    default TotalImpuestos mapTotalImpuestos(XMLSalesDocument.TaxTotal xml) {
        if (xml == null) {
            return null;
        }

        TotalImpuestos.TotalImpuestosBuilder builder = TotalImpuestos.builder()
                .total(xml.getTaxAmount());

        for (XMLSalesDocument.TaxSubtotal taxSubtotal : xml.getTaxSubtotals()) {
            Catalog5 catalog5 = Catalog
                    .valueOfCode(Catalog5.class, taxSubtotal.getTaxCategory().getTaxScheme().getId())
                    .orElseThrow(Catalog.invalidCatalogValue);
            switch (catalog5) {
                case IGV:
                    builder = builder
                            .gravadoBaseImponible(taxSubtotal.getTaxableAmount())
                            .gravadoImporte(taxSubtotal.getTaxAmount());
                    break;
                case IMPUESTO_ARROZ_PILADO:
                    builder = builder
                            .ivapBaseImponible(taxSubtotal.getTaxableAmount())
                            .ivapImporte(taxSubtotal.getTaxAmount());
                    break;
                case ISC:
                    builder = builder
                            .iscBaseImponible(taxSubtotal.getTaxableAmount())
                            .iscImporte(taxSubtotal.getTaxAmount());
                    break;
                case EXPORTACION:
                    builder = builder
                            .exportacionBaseImponible(taxSubtotal.getTaxableAmount())
                            .exportacionImporte(taxSubtotal.getTaxAmount());
                    break;
                case GRATUITO:
                    builder = builder
                            .gratuitoBaseImponible(taxSubtotal.getTaxableAmount())
                            .gratuitoImporte(taxSubtotal.getTaxAmount());
                    break;
                case EXONERADO:
                    builder = builder
                            .exoneradoBaseImponible(taxSubtotal.getTaxableAmount())
                            .exoneradoImporte(taxSubtotal.getTaxAmount());
                    break;
                case INAFECTO:
                    builder = builder
                            .inafectoBaseImponible(taxSubtotal.getTaxableAmount())
                            .inafectoImporte(taxSubtotal.getTaxAmount());
                    break;
                case ICBPER:
                    builder = builder
                            .icbImporte(taxSubtotal.getTaxAmount());
                    break;
                case OTROS:
                    break;
            }
        }

        return builder.build();
    }

    default List<DocumentoRelacionado> mapDocumentosRelacionados(List<XMLSalesDocument.AdditionalDocumentReference> xml) {
        if (xml == null) {
            return Collections.emptyList();
        }

        return xml.stream()
                .filter(additionalDocumentReference -> Objects.nonNull(additionalDocumentReference.getDocumentTypeCode()))
                .filter(additionalDocumentReference -> {
                    Optional<Catalog12> catalog12 = Catalog
                            .valueOfCode(Catalog12.class, additionalDocumentReference.getDocumentTypeCode());
                    Optional<Catalog12_Anticipo> catalog12_anticipo = Catalog
                            .valueOfCode(Catalog12_Anticipo.class, additionalDocumentReference.getDocumentTypeCode());
                    return catalog12.isPresent() && catalog12_anticipo.isEmpty();
                })
                .map(despatchDocumentReference -> DocumentoRelacionado.builder()
                        .serieNumero(despatchDocumentReference.getId())
                        .tipoDocumento(despatchDocumentReference.getDocumentTypeCode())
                        .build())
                .collect(Collectors.toList());
    }

    default DocumentoVentaDetalle mapSalesDocumentDetalle(XMLSalesDocumentLine documentLine) {
        if (documentLine == null) {
            return null;
        }

        DocumentoVentaDetalle.DocumentoVentaDetalleBuilder builder = DocumentoVentaDetalle.builder();

        // Extract taxes
        XMLSalesDocumentLine.TaxTotalLine taxTotal = documentLine.getTaxTotal();
        Map<String, Optional<XMLSalesDocumentLine.TaxSubtotalLine>> subTotals = taxTotal.getTaxSubtotals().stream()
                .collect(Collectors.groupingBy(
                        taxSubtotalLine -> taxSubtotalLine.getTaxCategory().getTaxScheme().getId(),
                        Collectors.reducing((o, o2) -> o) // Only one element per type is expected
                ));

        // ISC
        Optional<XMLSalesDocumentLine.TaxSubtotalLine> iscTaxSubtotal = subTotals.getOrDefault(Catalog5.ISC.getCode(), Optional.empty());
        iscTaxSubtotal.ifPresent(taxSubtotalLine -> {
            builder.iscBaseImponible(taxSubtotalLine.getTaxableAmount());
            builder.isc(taxSubtotalLine.getTaxAmount());
            builder.tasaIsc(mapPorcentaje(taxSubtotalLine.getTaxCategory().getPercent()));
            builder.iscTipo(taxSubtotalLine.getTaxCategory().getTierRange());
        });

        // IGV
        Optional<XMLSalesDocumentLine.TaxSubtotalLine> igvTaxSubtotal = taxTotal.getTaxSubtotals().stream()
                .filter(line -> {
                    String code = line.getTaxCategory().getTaxScheme().getId();
                    return !Objects.equals(code, Catalog5.ISC.getCode()) && !Objects.equals(code, Catalog5.ICBPER.getCode());
                })
                .findFirst();
        igvTaxSubtotal.ifPresent(taxSubtotalLine -> {
            builder.igvBaseImponible(taxSubtotalLine.getTaxableAmount());
            builder.igv(taxSubtotalLine.getTaxAmount());
            builder.tasaIgv(mapPorcentaje(taxSubtotalLine.getTaxCategory().getPercent()));
            builder.igvTipo(taxSubtotalLine.getTaxCategory().getTaxExemptionReasonCode());
        });

        // ICB
        Optional<XMLSalesDocumentLine.TaxSubtotalLine> icbTaxSubtotal = subTotals.getOrDefault(Catalog5.ICBPER.getCode(), Optional.empty());
        icbTaxSubtotal.ifPresent(taxSubtotalLine -> {
            builder.icb(taxSubtotalLine.getTaxAmount());
            builder.tasaIcb(taxSubtotalLine.getTaxCategory().getPerUnitAmount());
            builder.icbAplica(taxSubtotalLine.getTaxAmount() != null && taxSubtotalLine.getTaxAmount().compareTo(BigDecimal.ZERO) > 0);
        });

        BigDecimal precioReferencia = Optional.ofNullable(documentLine.getPricingReference())
                .flatMap(pricingReference -> Optional.ofNullable(pricingReference.getAlternativeConditionPrice()))
                .map(XMLSalesDocumentLine.AlternativeConditionPrice::getAlternativeConditionPrice)
                .orElse(null);
        String precioReferenciaTipo = Optional.ofNullable(documentLine.getPricingReference())
                .flatMap(pricingReference -> Optional.ofNullable(pricingReference.getAlternativeConditionPrice()))
                .map(XMLSalesDocumentLine.AlternativeConditionPrice::getPriceTypeCode)
                .orElse(null);

        return builder
                .descripcion(documentLine.getItem().getDescription())
                .precio(documentLine.getPrice().getPriceAmount())
                .precioReferencia(precioReferencia)
                .precioReferenciaTipo(precioReferenciaTipo)
                .totalImpuestos(taxTotal.getTaxAmount())
                .build();
    }
}
