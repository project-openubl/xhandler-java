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

public enum Catalog7 implements Catalog {
    GRAVADO_OPERACION_ONEROSA("10", true, Catalog5.IGV, Catalog7_1.GRAVADO),
    GRAVADO_RETIRO_POR_PREMIO("11", false, Catalog5.GRATUITO, Catalog7_1.GRAVADO),
    GRAVADO_RETIRO_POR_DONACION("12", false, Catalog5.GRATUITO, Catalog7_1.GRAVADO),
    GRAVADO_RETIRO("13", false, Catalog5.GRATUITO, Catalog7_1.GRAVADO),
    GRAVADO_RETIRO_POR_PUBLICIDAD("14", false, Catalog5.GRATUITO, Catalog7_1.GRAVADO),
    GRAVADO_BONIFICACIONES("15", false, Catalog5.GRATUITO, Catalog7_1.GRAVADO),
    GRAVADO_RETIRO_POR_ENTREGA_A_TRABAJADORES("16", false, Catalog5.GRATUITO, Catalog7_1.GRAVADO),
    GRAVADO_IVAP("17", true, Catalog5.IMPUESTO_ARROZ_PILADO, Catalog7_1.GRAVADO),

    EXONERADO_OPERACION_ONEROSA("20", true, Catalog5.EXONERADO, Catalog7_1.EXONERADO),
    EXONERADO_TRANSFERENCIA_GRATUITA("21", false, Catalog5.GRATUITO, Catalog7_1.EXONERADO),

    INAFECTO_OPERACION_ONEROSA("30", true, Catalog5.INAFECTO, Catalog7_1.INAFECTO),
    INAFECTO_RETIRO_POR_BONIFICACION("31", false, Catalog5.GRATUITO, Catalog7_1.INAFECTO),
    INAFECTO_RETIRO("32", false, Catalog5.GRATUITO, Catalog7_1.INAFECTO),
    INAFECTO_RETIRO_POR_MUESTRAS_MEDICAS("33", false, Catalog5.GRATUITO, Catalog7_1.INAFECTO),
    INAFECTO_RETIRO_POR_CONVENIO_COLECTIVO("34", false, Catalog5.GRATUITO, Catalog7_1.INAFECTO),
    INAFECTO_RETIRO_POR_PREMIO("35", false, Catalog5.GRATUITO, Catalog7_1.INAFECTO),
    INAFECTO_RETIRO_POR_PUBLICIDAD("36", false, Catalog5.GRATUITO, Catalog7_1.INAFECTO),
    EXPORTACION("40", true, Catalog5.EXPORTACION, Catalog7_1.EXPORTACION);

    private final String code;
    private final boolean operacionOnerosa;
    private final Catalog5 taxCategory;
    private final Catalog7_1 grupo;

    Catalog7(String code, boolean operacionOnerosa, Catalog5 taxCategory, Catalog7_1 grupo) {
        this.code = code;
        this.operacionOnerosa = operacionOnerosa;
        this.taxCategory = taxCategory;
        this.grupo = grupo;
    }

    @Override
    public String getCode() {
        return code;
    }

    public boolean isOperacionOnerosa() {
        return operacionOnerosa;
    }

    public Catalog5 getTaxCategory() {
        return taxCategory;
    }

    public Catalog7_1 getGrupo() {
        return grupo;
    }
}
