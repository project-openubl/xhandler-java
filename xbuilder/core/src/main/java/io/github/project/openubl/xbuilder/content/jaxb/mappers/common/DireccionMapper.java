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
