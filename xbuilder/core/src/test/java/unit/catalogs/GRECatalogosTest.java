package unit.catalogs;

import io.github.project.openubl.xbuilder.content.catalogs.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para los catálogos SUNAT de la GRE.
 * Verifica que los códigos sean correctos según Anexo N.° 8.
 */
public class GRECatalogosTest {

    @Test
    public void testCatalog20_MotivoTraslado() {
        assertEquals("01", Catalog20.VENTA.getCode());
        assertEquals("02", Catalog20.COMPRA.getCode());
        assertEquals("03", Catalog20.CONSIGNACION.getCode());
        assertEquals("04", Catalog20.TRASLADO_ENTRE_ESTABLECIMIENTOS.getCode());
        assertEquals("05", Catalog20.DEVOLUCION.getCode());
        assertEquals("06", Catalog20.TRASLADO_TRANSFORMACION.getCode());
        assertEquals("07", Catalog20.RECOJO_BIENES_TRANSFORMADOS.getCode());
        assertEquals("08", Catalog20.IMPORTACION.getCode());
        assertEquals("09", Catalog20.EXPORTACION.getCode());
        assertEquals("10", Catalog20.IMPORTACION_CON_DAM.getCode());
        assertEquals("11", Catalog20.IMPORTACION_TEMPORAL.getCode());
        assertEquals("13", Catalog20.OTROS.getCode());
        assertEquals("14", Catalog20.VENTA_SUJETA_A_CONFIRMACION.getCode());
        assertEquals("15", Catalog20.TRASLADO_ZONA_IVAP.getCode());
        assertEquals("16", Catalog20.EXPORTACION_TEMPORAL.getCode());
        assertEquals("17", Catalog20.REEXPORTACION.getCode());
        assertEquals("18", Catalog20.TRASLADO_EMISOR_ITINERANTE_CP.getCode());
        assertEquals("19", Catalog20.TRASLADO_MERCANCIA_EXTRANJERA.getCode());

        // Verify lookup
        assertTrue(Catalog20.valueOfCode("10").isPresent());
        assertEquals(Catalog20.IMPORTACION_CON_DAM, Catalog20.valueOfCode("10").get());
        assertFalse(Catalog20.valueOfCode("99").isPresent());
    }

    @Test
    public void testCatalog21_DocumentoRelacionado() {
        assertEquals("01", Catalog21.NUMERACION_DAM.getCode());
        assertEquals("02", Catalog21.NUMERO_DE_ORDEN_DE_ENTREGA.getCode());
        assertEquals("03", Catalog21.NUMERO_SCOP.getCode());
        assertEquals("04", Catalog21.NUMERO_DE_MANIFIESTO_DE_CARGA.getCode());
        assertEquals("05", Catalog21.NUMERO_DE_CONSTANCIA_DE_DETRACCION.getCode());
        assertEquals("06", Catalog21.OTROS.getCode());
        assertEquals("09", Catalog21.GUIA_REMISION_REMITENTE.getCode());
        assertEquals("12", Catalog21.DECLARACION_SIMPLIFICADA.getCode());
        assertEquals("31", Catalog21.GUIA_REMISION_TRANSPORTISTA.getCode());
        assertEquals("49", Catalog21.TICKET_SALIDA.getCode());
        assertEquals("50", Catalog21.CODIGO_AUTORIZACION_SUNAT.getCode());

        assertTrue(Catalog21.valueOfCode("49").isPresent());
    }

    @Test
    public void testCatalog61_DocumentoTransporte() {
        assertEquals("01", Catalog61.FACTURA.getCode());
        assertEquals("04", Catalog61.GUIA_REMISION_REMITENTE.getCode());
        assertEquals("05", Catalog61.GUIA_REMISION_TRANSPORTISTA.getCode());
        assertEquals("50", Catalog61.DAM.getCode());
        assertEquals("52", Catalog61.DECLARACION_SIMPLIFICADA.getCode());
    }

    @Test
    public void testCatalog62_BienesNormalizados() {
        assertEquals("01", Catalog62.AZUCAR.getCode());
        assertEquals("02", Catalog62.ARROZ.getCode());
        assertEquals("03", Catalog62.ALCOHOL_ETILICO.getCode());
        assertEquals("04", Catalog62.CEMENTO.getCode());
    }

    @Test
    public void testCatalog63_Puertos() {
        assertEquals("CALLAO", Catalog63.CALLAO.getCode());
        assertEquals("PAITA", Catalog63.PAITA.getCode());
        assertEquals("CHANCAY", Catalog63.CHANCAY.getCode()); // RS 000240-2024

        assertTrue(Catalog63.valueOfCode("CALLAO").isPresent());
        assertTrue(Catalog63.valueOfCode("callao").isPresent()); // Case insensitive
        assertFalse(Catalog63.valueOfCode("INEXISTENTE").isPresent());
    }

    @Test
    public void testCatalog64_Aeropuertos() {
        assertEquals("LIM", Catalog64.JORGE_CHAVEZ.getCode());
        assertEquals("AQP", Catalog64.RODRIGUEZ_BALLON.getCode());
        assertEquals("CUZ", Catalog64.ALEJANDRO_VELASCO.getCode());

        assertTrue(Catalog64.valueOfCode("LIM").isPresent());
    }

    @Test
    public void testIndicadorEnvio() {
        assertEquals("SUNAT_Envio_IndicadorTrasladoTotalDAMDS",
                IndicadorEnvio.TRASLADO_TOTAL_DAM_DS.getCode());
        assertEquals("SUNAT_Envio_IndicadorBienNormalizado",
                IndicadorEnvio.BIEN_NORMALIZADO.getCode());
    }

    @Test
    public void testCatalog18_ModalidadTraslado() {
        assertEquals("01", Catalog18.TRANSPORTE_PUBLICO.getCode());
        assertEquals("02", Catalog18.TRANSPORTE_PRIVADO.getCode());
    }

    @Test
    public void testCatalog1Guia_TipoDocumento() {
        assertEquals("09", Catalog1_Guia.GUIA_REMISION_REMITENTE.getCode());
        assertEquals("31", Catalog1_Guia.GUIA_REMISION_TRANSPORTISTA.getCode());
    }
}
