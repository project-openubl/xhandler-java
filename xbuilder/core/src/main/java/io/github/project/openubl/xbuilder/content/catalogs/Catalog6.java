package io.github.project.openubl.xbuilder.content.catalogs;

public enum Catalog6 implements Catalog {
    DOC_TRIB_NO_DOM_SIN_RUC("0"),
    DNI("1"),
    EXTRANJERIA("4"),
    RUC("6"),
    PASAPORTE("7"),
    DEC_DIPLOMATICA("A"),
    DOC_IDENT_PAIS_RESIDENCIA_NO_D("B"),
    TAX_IDENTIFICATION_NUMBER_TIN_DOC_TRIB_PP_NN("C"),
    IDENTIFICATION_NUMBER_IN_DOC_TRIB_PP_JJ("D"),
    TAM_TARJETA_ANDINA_DE_MIGRACION("E"),
    PERMISO_TEMPORAL_DE_PERMANENCIA_PTP("F"),
    SALVOCONDUCTO("G");

    private final String code;

    Catalog6(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
