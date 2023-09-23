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

public enum Catalog59 implements Catalog {
    DEPOSITO_EN_CUENTA("001"),
    GIRO("002"),
    TRANSFERENCIA_DE_FONDOS("003"),
    ORDEN_DE_PAGO("004"),
    TARJETA_DE_DEBITO("005"),
    TARJETA_DE_CREDITO_EMITIDA_EN_EL_PAIS_POR_UNA_EMPRESA_DEL_SISTEMA_FINANCIERO("006"),
    CHEQUES_CON_LA_CLAUSULA_DE_NONEGOCIABLE_INTRANSFERIBLE_NOALAORDEN_U_OTRA_EQUIVALENTE("007"),
    EFECTIVO_POR_OPERACIONES_EN_LAS_QUE_NO_EXISTE_OBLIGACION_DE_UTILIZAR_MEDIO_DE_PAGO("008"),
    EFECTIVO_EN_LOS_DEMAS_CASOS("009"),
    MEDIOS_DE_PAGO_USADOS_EN_COMERCIO_EXTERIOR("010"),
    DOCUMENTOS_EMITIDOS_POR_LAS_EDPYMES_Y_LAS_COOPERATIVAS("011"),
    TARJETA_DE_CREDITO_EMITIDA_EN_EL_PAIS_O_EN_EXTERIOR_EMITIDA_POR_EMPRESA_NO_PERTENECIENTE_AL_SISTEMA_FINANCIERO("012"),
    TARJETAS_DE_CREDITO_EMITIDAS_EN_EL_EXTERIOR_POR_EMPRESAS_BANCARIAS_O_FINANCIERAS_NO_DOMICILIADAS("013"),
    TRANSFERENCIAS_COMERCIO_EXTERIOR("101"),
    CHEQUES_BANCARIOS_COMERCIO_EXTERIOR("102"),
    ORDEN_DE_PAGO_SIMPLE_COMERCIO_EXTERIOR("103"),
    ORDEN_DE_PAGO_DOCUMENTARIO_COMERCIO_EXTERIOR("104"),
    REMESA_SIMPLE_COMERCIO_EXTERIOR("105"),
    REMESA_DOCUMENTARIA_COMERCIO_EXTERIOR("106"),
    CARTA_DE_CREDITO_SIMPLE_COMERCIO_EXTERIOR("107"),
    CARTA_DE_CREDITO_DOCUMENTARIO_COMERCIO_EXTERIOR("108"),
    OTROS_MEDIOS_DE_PAGO("999");

    private final String code;

    Catalog59(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
