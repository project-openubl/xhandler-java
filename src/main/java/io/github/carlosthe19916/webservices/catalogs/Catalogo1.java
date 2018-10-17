/*
 * Copyright 2017 Carlosthe19916, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.carlosthe19916.webservices.catalogs;

import java.util.stream.Stream;

public enum Catalogo1 {

    FACTURA("01"),
    BOLETA("03"),
    NOTA_CREDITO("07"),
    NOTA_DEBITO("08"),
    GUIA_REMISION_REMITENTE("09"),
    TICKET_MAQUINA_REGISTRADORA("12"),
    DOCUMENTOS_FINANCIEROS("13"),
    DOCUMENTOS_AFP("18"),
    GUIA_REMISION_TRANSPORTISTA("31"),
    COMPROBANTE_PAGO_SEAE("56"),
    GUIA_REMISION_REMITENTE_COMPLEMENTARIA("71"),
    GUIA_REMISION_TRANSPORTISTA_COMPLEMENTARIA("72"),

    RETENCION("20"),
    PERCEPCION("40"),
    PERCEPCION_VENTA_INTERNA("21");

    private final String code;

    Catalogo1(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Catalogo1 valueOfCode(String code) {
        return Stream.of(Catalogo1.values())
                .filter(p -> p.code.equals(code))
                .findFirst()
                .orElse(null);
    }

}
