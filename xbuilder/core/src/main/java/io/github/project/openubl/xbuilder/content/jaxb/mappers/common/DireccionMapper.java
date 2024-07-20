package io.github.project.openubl.xbuilder.content.jaxb.mappers.common;

import io.github.project.openubl.xbuilder.content.jaxb.models.XMLAddress;
import io.github.project.openubl.xbuilder.content.models.common.Direccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DireccionMapper {

    @Mapping(target = "ubigeo", source = "id")
    @Mapping(target = "codigoLocal", source = "addressTypeCode")
    @Mapping(target = "urbanizacion", source = "citySubdivisionName")
    @Mapping(target = "provincia", source = "cityName")
    @Mapping(target = "departamento", source = "countrySubEntity")
    @Mapping(target = "distrito", source = "district")
    @Mapping(target = "codigoPais", source = "country.identificationCode")
    @Mapping(target = "direccion", source = "addressLine.line")
    Direccion map(XMLAddress xml);

}
