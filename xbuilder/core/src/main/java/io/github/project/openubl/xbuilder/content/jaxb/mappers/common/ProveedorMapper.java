package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplier;
import io.github.project.openubl.xbuilder.content.jaxb.models.XMLSupplierSunat;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {
        ContactoMapper.class,
        DireccionMapper.class
})
public interface ProveedorMapper {

    @Mapping(target = "ruc", source = "partyIdentification.id")
    @Mapping(target = "nombreComercial", source = "partyName.name")
    @Mapping(target = "razonSocial", source = "partyLegalEntity.registrationName")
    @Mapping(target = "direccion", source = "partyLegalEntity.address")
    @Mapping(target = "contacto", source = "contact")
    Proveedor map(XMLSupplier xml);

    @Mapping(target = "ruc", source = "customerAssignedAccountID")
    @Mapping(target = "nombreComercial", source = "partyName.name")
    @Mapping(target = "razonSocial", source = "party.partyLegalEntity.registrationName")
    @Mapping(target = "direccion", source = "party.partyLegalEntity.address")
    @Mapping(target = "contacto", source = "contact")
    Proveedor map(XMLSupplierSunat xml);
}
