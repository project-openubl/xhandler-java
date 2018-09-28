package io.github.carlosthe19916.webservices.models;

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
