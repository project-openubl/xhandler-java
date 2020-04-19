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

public class BillConsultModel {

    private final String ruc;
    private final String tipo;
    private final String serie;
    private final int numero;

    private BillConsultModel(Builder builder) {
        this.ruc = builder.ruc;
        this.tipo = builder.tipo;
        this.serie = builder.serie;
        this.numero = builder.numero;
    }

    public String getRuc() {
        return ruc;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSerie() {
        return serie;
    }

    public int getNumero() {
        return numero;
    }

    public static class Builder {
        private String ruc;
        private String tipo;
        private String serie;
        private Integer numero;

        public Builder ruc(String ruc) {
            this.ruc = ruc;
            return this;
        }

        public Builder tipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public Builder serie(String serie) {
            this.serie = serie;
            return this;
        }

        public Builder numero(int numero) {
            this.numero = numero;
            return this;
        }

        public BillConsultModel build() {
            return new BillConsultModel(this);
        }
    }
}
