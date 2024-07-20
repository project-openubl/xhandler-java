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
