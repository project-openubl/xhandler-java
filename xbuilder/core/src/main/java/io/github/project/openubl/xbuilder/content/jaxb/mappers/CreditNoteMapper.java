package io.github.project.openubl.xbuilder.content.jaxb.mappers;

import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentHelperMapper;
import io.github.project.openubl.xbuilder.content.jaxb.mappers.common.SalesDocumentMapper;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLCreditNote;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLCreditNoteLine;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument;
import io.github.project.openubl.xbuilder.content.models.standard.general.CreditNote;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteNote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(config = SalesDocumentMapper.class,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface CreditNoteMapper extends SalesDocumentHelperMapper {

    @Mapping(target = "comprobanteAfectadoSerieNumero", source = "discrepancyResponse.referenceID")
    @Mapping(target = "comprobanteAfectadoTipo", source = "discrepancyResponse.responseCode")
    @Mapping(target = "sustentoDescripcion", source = "discrepancyResponse.description")

    @Mapping(target = "totalImporte", source = "monetaryTotal")
    @Mapping(target = "detalles", source = "lines")
    CreditNote map(XMLCreditNote xml);

    @Mapping(target = "importe", source = "payableAmount")
    @Mapping(target = "importeSinImpuestos", source = "lineExtensionAmount")
    @Mapping(target = "importeConImpuestos", source = "taxInclusiveAmount")
    TotalImporteNote mapTotalImporteNote(XMLSalesDocument.MonetaryTotal xml);

    default DocumentoVentaDetalle mapDocumentoVentaDetalle(XMLCreditNoteLine xml) {
        DocumentoVentaDetalle documentoVentaDetalle = mapSalesDocumentDetalle(xml);
        documentoVentaDetalle.setCantidad(xml.getQuantity().getValue());
        documentoVentaDetalle.setUnidadMedida(xml.getQuantity().getUnitCode());

        return documentoVentaDetalle;
    }
}
