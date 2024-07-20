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
