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
package io.github.project.openubl.xbuilder.content.jaxb.mappers;

import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentHelperMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentMapper;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDebitNote;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLDebitNoteLine;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument;
import io.github.project.openubl.xbuilder.content.models.standard.general.DebitNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(config = SalesDocumentMapper.class,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface DebitNoteMapper extends SalesDocumentHelperMapper {

    @Mapping(target = "comprobanteAfectadoSerieNumero", source = "discrepancyResponse.referenceID")
    @Mapping(target = "comprobanteAfectadoTipo", source = "discrepancyResponse.responseCode")
    @Mapping(target = "sustentoDescripcion", source = "discrepancyResponse.description")

    @Mapping(target = "totalImporte", source = "monetaryTotal")
    @Mapping(target = "detalles", source = "lines")
    DebitNote map(XMLDebitNote xml);

    @Mapping(target = "importe", source = "payableAmount")
    @Mapping(target = "importeSinImpuestos", source = "lineExtensionAmount")
    @Mapping(target = "importeConImpuestos", source = "taxInclusiveAmount")
    TotalImporteNote mapTotalImporteNote(XMLSalesDocument.MonetaryTotal xml);

    default DocumentoVentaDetalle mapDocumentoVentaDetalle(XMLDebitNoteLine xml) {
        DocumentoVentaDetalle documentoVentaDetalle = mapSalesDocumentDetalle(xml);
        documentoVentaDetalle.setCantidad(xml.getQuantity().getValue());
        documentoVentaDetalle.setUnidadMedida(xml.getQuantity().getUnitCode());

        return documentoVentaDetalle;
    }
}
