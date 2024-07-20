package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.jaxb.models.XMLCustomer;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        ContactoMapper.class,
        DireccionMapper.class
})
public interface ClienteMapper {

    @Mapping(target = "tipoDocumentoIdentidad", source = "partyIdentification.id.schemeID")
    @Mapping(target = "numeroDocumentoIdentidad", source = "partyIdentification.id.value")
    @Mapping(target = "nombre", source = "partyLegalEntity.registrationName")
    @Mapping(target = "direccion", source = "partyLegalEntity.address")
    @Mapping(target = "contacto", source = "contact")
    Cliente map(XMLCustomer xml);

}
