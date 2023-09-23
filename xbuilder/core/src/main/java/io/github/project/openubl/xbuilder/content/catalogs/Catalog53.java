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

public enum Catalog53 implements Catalog {
    DESCUENTO_AFECTA_BASE_IMPONIBLE_IGV_IVAP("00"),
    DESCUENTO_NO_AFECTA_BASE_IMPONIBLE_IGV_IVAP("01"),
    DESCUENTO_GLOBAL_AFECTA_BASE_IMPONIBLE_IGV_IVAP("02"),
    DESCUENTO_GLOBAL_NO_AFECTA_BASE_IMPONIBLE_IGV_IVAP("03"),
    DESCUENTO_GLOBAL_POR_ANTICIPOS_GRAVADOS_AFECTA_BASE_IMPONIBLE_IGV_IVAP("04"),
    DESCUENTO_GLOBAL_POR_ANTICIPOS_EXONERADOS("05"),
    DESCUENTO_GLOBAL_POR_ANTICIPOS_INAFECTOS("06"),
    FACTOR_DE_COMPENSACION("07"),
    ANTICIPO_DE_ISC("20"),
    FISE("45"),
    RECARGO_AL_CONSUMO_Y_O_PROPINAS("46"),
    CARGOS_QUE_AFECTAN_BASE_IMPONIBLE_IGV_IVAP("47"),
    CARGOS_QUE_NO_AFECTAN_BASE_IMPONIBLE_IGV_IVAP("48"),
    CARGOS_GLOBALES_QUE_AFECTAN_BASE_IMPONIBLE_IGV_IVAP("49"),
    CARGOS_GLOBALES_QUE_NO_AFECTAN_BASE_IMPONIBLE_IGV_IVAP("50"),
    PERCEPCION_VENTA_INTERNA("51"),
    PERCEPCION_A_LA_ADQUISICION_DE_COMBUSTIBLE("52"),
    PERCEPCION_REALIZADA_AL_AGENTE_DE_PERCEPCION_CON_TASA_ESPECIAL("53"),
    FACTOR_DE_APORTACION("54"),
    RETENCION_DE_RENTA_POR_ANTICIPOS("61"),
    RETENCION_DEL_IGV("62"),
    RETENCION_DE_RENTA_DE_SEGUNDA_CATEGORIA("62");

    private final String code;

    Catalog53(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
