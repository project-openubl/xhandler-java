/**
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Eclipse Public License - v 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xmlsenderws.webservices.models;

import java.util.Optional;
import java.util.stream.Stream;

public enum GetStatusResponseType {

    EXISTE_Y_ACEPTADO("0001", "El comprobante existe y está aceptado"),
    EXISTE_PERO_RECHAZADO("0002", "El comprobante existe pero está rechazado"),
    EXISTE_PERO_DADO_DE_BAJA("0003", "El comprobante existe pero está de baja"),

    ERROR_CONSULTA1("0004", "Formato de RUC no es válido (debe de contener 11 caracteres numéricos)"),
    ERROR_CONSULTA2("0005", "Formato del tipo de comprobante no es válido (debe de contener 2 caracteres)"),
    ERROR_CONSULTA3("0006", "Formato de serie inválido (debe de contener 4 caracteres)"),
    ERROR_CONSULTA4("0007", "El numero de comprobante debe de ser mayor que cero"),
    ERROR_CONSULTA5("0008", "El número de RUC no está inscrito en los registros de la SUNAT"),
    ERROR_CONSULTA6("0009", "EL tipo de comprobante debe de ser (01, 07 o 08)"),
    ERROR_CONSULTA7("0010", "Sólo se puede consultar facturas, notas de crédito y debito electrónicas, cuya serie empieza con F"),
    ERROR_CONSULTA8("0011", "El comprobante de pago electrónico no existe"),
    ERROR_CONSULTA9("0012", "El comprobante de pago electrónico no le pertenece");

    private final String code;
    private final String description;

    GetStatusResponseType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Optional<GetStatusResponseType> searchByCode(String code) {
        return Stream.of(GetStatusResponseType.values()).filter(p -> p.getCode().equals(code)).findFirst();
    }

}
