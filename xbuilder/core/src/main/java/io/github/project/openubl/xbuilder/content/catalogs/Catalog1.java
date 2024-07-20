package io.github.project.openubl.xbuilder.content.catalogs;

import java.util.Optional;
import java.util.stream.Stream;

public enum Catalog1 implements Catalog {
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

    Catalog1(String code) {
        this.code = code;
    }

    public static Optional<Catalog1> valueOfCode(String code) {
        return Stream.of(Catalog1.values()).filter(p -> p.code.equals(code)).findFirst();
    }

    @Override
    public String getCode() {
        return code;
    }
}
