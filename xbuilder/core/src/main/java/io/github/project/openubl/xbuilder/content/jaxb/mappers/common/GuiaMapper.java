package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSalesDocument;
import io.github.project.openubl.xbuilder.content.models.standard.general.Guia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GuiaMapper {

    @Mapping(target = "serieNumero", source = "id")
    @Mapping(target = "tipoDocumento", source = "documentTypeCode")
    Guia mapGuia(XMLSalesDocument.DespatchDocumentReference xml);

}
