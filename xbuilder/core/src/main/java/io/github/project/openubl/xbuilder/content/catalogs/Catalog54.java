package io.github.project.openubl.xbuilder.content.catalogs;

/**
 * Catálogo 54: Códigos de bienes y servicios sujetos a detracción.
 * <p>
 * Fuente: SUNAT – RS 183-2004/SUNAT y modificatorias (RS 071-2018, RS 082-2018, etc.). Usado en
 * {@code cbc:PaymentMeansID/@schemeName="Codigo de detraccion SUNAT"}.
 * </p>
 *
 * @see <a href="https://cpe.sunat.gob.pe/guias-y-manuales">Guía XML Factura 2.1 – Catálogo 54</a>
 */
public enum Catalog54 implements Catalog {

    // ── Venta de bienes (Anexo 1) ─────────────────────────────────
    AZUCAR("001"),
    ALCOHOL_ETILICO("003"),
    RECURSOS_HIDROBIOLOGICOS("004"),
    MAIZ_AMARILLO_DURO("005"),
    ALGODON("006"),
    CANA_DE_AZUCAR("007"),
    MADERA("008"),
    ARENA_Y_PIEDRA("009"),
    RESIDUOS_SUBPRODUCTOS("010"),
    BIENES_GRAVADOS_CON_IGV_RENUNCIANDO_EXONERACION("011"),
    INTERMEDIACION_LABORAL("012"),
    ANIMALES_VIVOS("013"),
    CARNES_Y_DESPOJOS("014"),
    ABONOS_CUEROS_PIELES("015"),
    ACEITE_DE_PESCADO("016"),
    HARINA_POLVO_PELLETS_PESCADO("017"),
    EMBARCACIONES_PESQUERAS("018"),
    LECHE("019"),
    ARROZ_PILADO("020"),
    MINERALES_METALICOS_NO_METALICOS("021"),
    BIENES_EXONERADOS_IGV("022"),
    ORO_DEMAS_MINERALES("023"),
    MINERALES_NO_METALICOS("024"),
    ORO_AMALGAMA("025"),
    PLOMO("026"),
    PIMIENTO_PIQUILLO("027"),
    ESPARRAGOS("028"),
    ZINC("029"),
    JUREL_Y_CABALLA("030"),
    PAPA("031"),

    // ── Servicios (Anexo 3) ───────────────────────────────────────
    SERVICIOS_TRANSPORTE_BIENES_VIA_TERRESTRE("012"),
    SERVICIOS_TRANSPORTE_PASAJEROS_VIA_TERRESTRE("020"),
    ARRENDAMIENTO_BIENES("014"),
    MANTENIMIENTO_REPARACION("017"),
    MOVIMIENTO_DE_CARGA("019"),
    OTROS_SERVICIOS_EMPRESARIALES("022"),
    FABRICACION_ENCARGO("023"),
    SERVICIO_TRANSPORTE_PERSONAS("027"),
    CONTRATOS_CONSTRUCCION("037"),
    DEMAS_SERVICIOS_GRAVADOS_IGV("012");

    private final String code;

    Catalog54(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
