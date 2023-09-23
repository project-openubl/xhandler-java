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

@SerieNumeroTranslator
public class SerieNumeroMapper {

    @SerieTranslator
    public String toSerie(String serieNumero) {
        if (serieNumero == null) {
            return null;
        }

        String[] split = serieNumero.split("-");
        return split.length >= 1 ? split[0] : null;
    }

    @Numero2Translator
    public Integer toNumero2(String serieNumero) {
        if (serieNumero == null) {
            return null;
        }

        String[] split = serieNumero.split("-");
        return split.length >= 2 ? Integer.parseInt(split[1]) : null;
    }

    @Numero3Translator
    public Integer toNumero3(String serieNumero) {
        if (serieNumero == null) {
            return null;
        }

        String[] split = serieNumero.split("-");
        return split.length >= 3 ? Integer.parseInt(split[2]) : null;
    }
}
