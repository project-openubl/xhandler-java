package e2e.renderer.despatchadvice;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog18;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog20;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * Tests de GRE-Remitente para operaciones de comercio exterior.
 * <p>
 * Escenarios cubiertos:
 * <ul>
 * <li>Importación con DAM y traslado total</li>
 * <li>Exportación con DAM/DS</li>
 * <li>Mercancía extranjera (motivo 19) desde puerto a depósito temporal</li>
 * <li>Contenedores con precinto</li>
 * </ul>
 * <p>
 * Fuentes normativas:
 * - RS 000240-2024/SUNAT (trazabilidad comercio exterior)
 * - RS 000133-2025/SUNAT (prórroga al 01-jul-2026)
 * - FAQ #24, #30, #31, #32, #33, #40 de SUNAT CPE
 */
public class DespatchAdviceComercioExteriorTest extends AbstractTest {

    /**
     * GRE-Remitente: Importación con DAM y traslado TOTAL.
     * <p>
     * Según FAQ #24: cuando es traslado total de la DAM/DS,
     * no se requiere detalle de bienes, pero UBL exige al menos una línea vacía.
     * Se marca el indicador SUNAT_Envio_IndicadorTrasladoTotalDAMDS.
     */
    @Test
    public void testImportacionConDAMTrasladoTotal() throws Exception {
        DespatchAdvice input = DespatchAdvice.builder()
                .serie("T001")
                .numero(200)
                .tipoComprobante(Catalog1.GUIA_REMISION_REMITENTE.getCode())
                .remitente(Remitente.builder()
                        .ruc("20100010001")
                        .razonSocial("Importadora Nacional S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20100010001")
                        .nombre("Importadora Nacional S.A.C.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.IMPORTACION_CON_DAM.getCode()) // "10"
                        .pesoTotal(new BigDecimal("5000.00"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .transportista(Transportista.builder()
                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                .numeroDocumentoIdentidad("20300030003")
                                .nombre("Transportes Pesados S.A.C.")
                                .numeroRegistroMTC("MTC-123456")
                                .build())
                        .indicador("SUNAT_Envio_IndicadorTrasladoTotalDAMDS")
                        .contenedor(Contenedor.builder()
                                .numero("CONT-2024-001")
                                .precinto("PREC-001")
                                .build())
                        .contenedor(Contenedor.builder()
                                .numero("CONT-2024-002")
                                .precinto("PREC-002")
                                .build())
                        .puerto(Puerto.builder()
                                .codigo("CALLAO")
                                .nombre("Puerto del Callao")
                                .build())
                        .declaracionAduanera(DeclaracionAduanera.builder()
                                .tipo("DAM")
                                .numero("118-2024-10-000123")
                                .rucEmisor("20100010001")
                                .build())
                        .partida(Partida.builder()
                                .direccion("Terminal Portuario del Callao")
                                .ubigeo("070101")
                                .build())
                        .destino(Destino.builder()
                                .direccion("Almacen Deposito Temporal S.A.")
                                .ubigeo("150101")
                                .build())
                        .build())
                // FAQ #24: línea vacía mínima requerida por UBL cuando traslado total
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("1.00"))
                        .unidadMedida("ZZ")
                        .codigo("-")
                        .build())
                .build();

        assertInput(input, "importacionDAMTotal.xml");
    }

    /**
     * GRE-Remitente: Exportación sin DAM numerada aún.
     * <p>
     * FAQ #32: cuando no se cuenta con la DAM/DS de exportación numerada,
     * se debe emitir GRE con motivo "otros" (13) y especificar en observaciones.
     */
    @Test
    public void testExportacionSinDAM() throws Exception {
        DespatchAdvice input = DespatchAdvice.builder()
                .serie("T001")
                .numero(201)
                .tipoComprobante(Catalog1.GUIA_REMISION_REMITENTE.getCode())
                .observaciones("Traslado para exportación, DAM en trámite")
                .remitente(Remitente.builder()
                        .ruc("20500050005")
                        .razonSocial("Exportadora Perú S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20500050005")
                        .nombre("Exportadora Perú S.A.C.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.OTROS.getCode()) // "13" por FAQ #32
                        .motivoTraslado("Traslado para exportación sin DAM numerada")
                        .pesoTotal(new BigDecimal("2000.00"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .chofer(Driver.builder()
                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                .numeroDocumentoIdentidad("12345678")
                                .nombres("Pedro")
                                .apellidos("Gonzales")
                                .licencia("Q9876543")
                                .tipo("Principal")
                                .build())
                        .vehiculo(Vehicle.builder()
                                .placa("ABC-123")
                                .build())
                        .partida(Partida.builder()
                                .direccion("Planta de producción")
                                .ubigeo("150101")
                                .build())
                        .destino(Destino.builder()
                                .direccion("Depósito Temporal para exportación")
                                .ubigeo("070101")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("100.00"))
                        .unidadMedida("KGM")
                        .codigo("EXPORT-001")
                        .descripcion("Quinua orgánica para exportación")
                        .build())
                .build();

        assertInput(input, "exportacionSinDAM.xml");
    }

    /**
     * GRE-Remitente: Mercancía extranjera motivo 19 desde puerto a depósito
     * temporal.
     * <p>
     * FAQ #40: Cuando se traslada del puerto un contenedor con carga consolidada
     * con destino a un depósito temporal, corresponde emitir GRE-Remitente
     * motivo "19 Traslado de mercancía extranjera" a cargo del DT.
     * <p>
     * Nota: La obligatoriedad plena del motivo 19 en reemplazo del ticket de salida
     * fue pospuesta al 01-jul-2026 por RS 000133-2025/SUNAT.
     * Actualmente se puede usar tanto ticket de salida como GRE.
     */
    @Test
    public void testMercanciaExtranjeraMotivo19() throws Exception {
        DespatchAdvice input = DespatchAdvice.builder()
                .serie("T001")
                .numero(202)
                .tipoComprobante(Catalog1.GUIA_REMISION_REMITENTE.getCode())
                .remitente(Remitente.builder()
                        .ruc("20600060006")
                        .razonSocial("Depósito Temporal del Callao S.A.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20600060006")
                        .nombre("Depósito Temporal del Callao S.A.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.TRASLADO_MERCANCIA_EXTRANJERA.getCode()) // "19"
                        .pesoTotal(new BigDecimal("10000.00"))
                        .pesoTotalUnidadMedida("KGM")
                        .numeroDeBultos(1)
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .transportista(Transportista.builder()
                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                .numeroDocumentoIdentidad("20700070007")
                                .nombre("Transportes Portuarios S.A.C.")
                                .build())
                        .contenedor(Contenedor.builder()
                                .numero("MSKU1234567")
                                .precinto("SEAL-ABC123")
                                .build())
                        .puerto(Puerto.builder()
                                .codigo("CALLAO")
                                .nombre("Puerto del Callao")
                                .build())
                        .numeroManifiesto("2024-000456")
                        .partida(Partida.builder()
                                .direccion("Terminal Portuario del Callao")
                                .ubigeo("070101")
                                .build())
                        .destino(Destino.builder()
                                .direccion("Depósito Temporal del Callao")
                                .ubigeo("070106")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("1.00"))
                        .unidadMedida("ZZ")
                        .codigo("CONT-CONSOLIDADO")
                        .descripcion("Contenedor carga consolidada")
                        .build())
                .build();

        assertInput(input, "mercanciaExtranjera.xml");
    }
}
