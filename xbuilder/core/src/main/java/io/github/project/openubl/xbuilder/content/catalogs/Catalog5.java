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
package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog5 implements Catalog {
    IGV("1000", "VAT", "S", "IGV"),
    IMPUESTO_ARROZ_PILADO("1016", "VAT", "S", "IVAP"),
    ISC("2000", "EXC", "S", "ISC"),
    EXPORTACION("9995", "FRE", "S", "EXP"),
    GRATUITO("9996", "FRE", "S", "GRA"),
    EXONERADO("9997", "VAT", "S", "EXO"),
    INAFECTO("9998", "FRE", "S", "INA"),
    ICBPER("7152", "OTH", "S", "ICBPER"),
    OTROS("9999", "OTH", "S", "OTROS");

    private final String code;
    private final String tipo;
    private final String categoria;
    private final String nombre;

    Catalog5(String code, String tipo, String categoria, String nombre) {
        this.code = code;
        this.tipo = tipo;
        this.categoria = categoria;
        this.nombre = nombre;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }
}
