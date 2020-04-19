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

package io.github.projectopenubl.xmlsenderlib.webservices.models;

public class BillValidModel {

    private final String rucEmisor;
    private final String tipoCDP;
    private final String serieCDP;
    private final String numeroCDP;
    private final String tipoDocIdReceptor;
    private final String numeroDocIdReceptor;
    private final String fechaEmision;
    private final double importeTotal;
    private final String nroAutorizacion;

    private BillValidModel(Builder builder) {
        this.rucEmisor = builder.rucEmisor;
        this.tipoCDP = builder.tipoCDP;
        this.serieCDP = builder.serieCDP;
        this.numeroCDP = builder.numeroCDP;
        this.tipoDocIdReceptor = builder.tipoDocIdReceptor;
        this.numeroDocIdReceptor = builder.numeroDocIdReceptor;
        this.fechaEmision = builder.fechaEmision;
        this.importeTotal = builder.importeTotal;
        this.nroAutorizacion = builder.nroAutorizacion;
    }

    public String getRucEmisor() {
        return rucEmisor;
    }

    public String getTipoCDP() {
        return tipoCDP;
    }

    public String getSerieCDP() {
        return serieCDP;
    }

    public String getNumeroCDP() {
        return numeroCDP;
    }

    public String getTipoDocIdReceptor() {
        return tipoDocIdReceptor;
    }

    public String getNumeroDocIdReceptor() {
        return numeroDocIdReceptor;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public String getNroAutorizacion() {
        return nroAutorizacion;
    }

    public static class Builder {
        private String rucEmisor;
        private String tipoCDP;
        private String serieCDP;
        private String numeroCDP;
        private String tipoDocIdReceptor;
        private String numeroDocIdReceptor;
        private String fechaEmision;
        private double importeTotal;
        private String nroAutorizacion;

        public Builder rucEmisor(String rucEmisor) {
            this.rucEmisor = rucEmisor;
            return this;
        }

        public Builder tipoCDP(String tipoCDP) {
            this.tipoCDP = tipoCDP;
            return this;
        }

        public Builder serieCDP(String serieCDP) {
            this.serieCDP = serieCDP;
            return this;
        }

        public Builder numeroCDP(String numeroCDP) {
            this.numeroCDP = numeroCDP;
            return this;
        }

        public Builder tipoDocIdReceptor(String tipoDocIdReceptor) {
            this.tipoDocIdReceptor = tipoDocIdReceptor;
            return this;
        }

        public Builder numeroDocIdReceptor(String numeroDocIdReceptor) {
            this.numeroDocIdReceptor = numeroDocIdReceptor;
            return this;
        }

        public Builder fechaEmision(String fechaEmision) {
            this.fechaEmision = fechaEmision;
            return this;
        }

        public Builder nroAutorizacion(String nroAutorizacion) {
            this.nroAutorizacion = nroAutorizacion;
            return this;
        }

        public BillValidModel build() {
            return new BillValidModel(this);
        }
    }
}
