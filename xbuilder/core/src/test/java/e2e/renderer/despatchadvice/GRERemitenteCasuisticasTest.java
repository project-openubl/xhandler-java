package e2e.renderer.despatchadvice;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog18;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog20;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.catalogs.IndicadorEnvio;
import io.github.project.openubl.xbuilder.content.catalogs.TipoConductor;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * Tests de GRE-Remitente usando el modelo {@link GRERemitente}.
 * <p>
 * Casuísticas cubiertas:
 * <ol>
 * <li>Transporte privado básico (conductor + vehículo, solo placa)</li>
 * <li>Transporte público (transportista contratado consignado en
 * GRE-Remitente)</li>
 * <li>Transporte privado con múltiples conductores (relevo)</li>
 * <li>Transporte privado con vehículo principal + secundarios (TUC
 * condicional)</li>
 * <li>Transporte público con transportista consignado en GRE-Remitente —
 * NO confundir con GRE-Transportista (que emite su propia guía V*)</li>
 * <li>Vehículo categoría M1/L — indicador
 * SUNAT_Envio_IndicadorTrasladoVehiculoM1L
 * (sin conductor ni vehículo, distinto de transbordo programado)</li>
 * <li>Mercancía extranjera motivo 19 desde zona primaria (contenedores +
 * precintos)</li>
 * </ol>
 */
public class GRERemitenteCasuisticasTest extends AbstractTest {

    /**
     * Casuística 1: Transporte PRIVADO básico.
     * <p>
     * El remitente traslada sus propios bienes con su propio vehículo.
     * Requiere: conductor principal + vehículo (solo placa, TUC no es
     * obligatorio salvo inscripción ante el MTC).
     */
    @Test
    public void testTransportePrivadoBasico() throws Exception {
        GRERemitente gre = GRERemitente.builder()
                .serie("T001")
                .numero(1)
                .remitente(Remitente.builder()
                        .ruc("20100010001")
                        .razonSocial("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Almacén Destino S.A.C.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("50.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .chofer(Driver.builder()
                                .tipo(TipoConductor.PRINCIPAL.getCode())
                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                .numeroDocumentoIdentidad("12345678")
                                .nombres("Carlos")
                                .apellidos("Ramirez")
                                .licencia("Q1234567")
                                .build())
                        .vehiculo(Vehicle.builder()
                                .placa("ABC-123")
                                .build())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Av. Industrial 456, Lima")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("150102")
                                .direccion("Jr. Comercio 789, Rímac")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("10.00"))
                        .unidadMedida("NIU")
                        .codigo("PROD-001")
                        .descripcion("Cajas de producto terminado")
                        .build())
                .build();

        assertInput(gre, "transportePrivadoBasico.xml");
    }

    /**
     * Casuística 2: Transporte PÚBLICO.
     * <p>
     * El remitente contrata a un transportista.
     * Requiere: datos del transportista (RUC, razón social).
     * No requiere conductor/vehículo del remitente (lo provee el transportista).
     */
    @Test
    public void testTransportePublico() throws Exception {
        GRERemitente gre = GRERemitente.builder()
                .serie("T001")
                .numero(2)
                .remitente(Remitente.builder()
                        .ruc("20100010001")
                        .razonSocial("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Cliente Final S.A.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("200.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .transportista(Transportista.builder()
                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                .numeroDocumentoIdentidad("20300030003")
                                .nombre("Transportes Nacionales S.A.C.")
                                .numeroRegistroMTC("MTC-001234")
                                .build())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Almacén Central, Lima")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("040101")
                                .direccion("Sucursal Arequipa")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("50.00"))
                        .unidadMedida("NIU")
                        .codigo("PROD-002")
                        .descripcion("Mercadería general")
                        .build())
                .build();

        assertInput(gre, "transportePublico.xml");
    }

    /**
     * Casuística 3: Transporte privado con MÚLTIPLES CONDUCTORES.
     * <p>
     * Según SUNAT, se pueden consignar conductores adicionales
     * (copiloto, relevo) además del conductor principal.
     */
    @Test
    public void testMultiplesConductores() throws Exception {
        GRERemitente gre = GRERemitente.builder()
                .serie("T001")
                .numero(3)
                .remitente(Remitente.builder()
                        .ruc("20100010001")
                        .razonSocial("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Destino S.A.C.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.TRASLADO_ENTRE_ESTABLECIMIENTOS.getCode())
                        .pesoTotal(new BigDecimal("1500.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .chofer(Driver.builder()
                                .tipo(TipoConductor.PRINCIPAL.getCode())
                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                .numeroDocumentoIdentidad("11111111")
                                .nombres("Juan")
                                .apellidos("Perez")
                                .licencia("Q1111111")
                                .build())
                        .chofer(Driver.builder()
                                .tipo(TipoConductor.SECUNDARIO.getCode())
                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                .numeroDocumentoIdentidad("22222222")
                                .nombres("Pedro")
                                .apellidos("Gomez")
                                .licencia("Q2222222")
                                .build())
                        .vehiculo(Vehicle.builder()
                                .placa("DEF-456")
                                .build())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Planta Principal, Lima")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("130101")
                                .direccion("Sucursal Tacna")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("100.00"))
                        .unidadMedida("KGM")
                        .codigo("MAT-001")
                        .descripcion("Materia prima")
                        .build())
                .build();

        assertInput(gre, "multiplesConductores.xml");
    }

    /**
     * Casuística 4: Transporte privado con VEHÍCULO + CARRETA (secundarios).
     * <p>
     * El vehículo principal puede llevar vehículos secundarios adjuntos
     * (carretas, semirremolques) con sus propias placas.
     * <p>
     * El TUC (Tarjeta Única de Circulación) es condicional: solo se consigna
     * cuando el vehículo tiene obligación de inscripción ante el MTC
     * (vehículos de transporte de carga pesada). No es dato universal.
     */
    @Test
    public void testVehiculoConCarreta() throws Exception {
        GRERemitente gre = GRERemitente.builder()
                .serie("T001")
                .numero(4)
                .remitente(Remitente.builder()
                        .ruc("20100010001")
                        .razonSocial("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Destino S.A.C.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("8000.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .chofer(Driver.builder()
                                .tipo(TipoConductor.PRINCIPAL.getCode())
                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                .numeroDocumentoIdentidad("33333333")
                                .nombres("Roberto")
                                .apellidos("Silva")
                                .licencia("Q3333333")
                                .build())
                        .vehiculo(Vehicle.builder()
                                .placa("GHI-789")
                                .numeroCirculacion("TUC-MAIN") // TUC: vehículo inscrito ante MTC
                                .numeroAutorizacion("AUTH-001") // Dato de ejemplo condicional, no obligatorio en todo
                                                                // vehículo
                                .codigoEmisor("MTC") // Dato de ejemplo condicional, asociado a numeroAutorizacion
                                .secundario(Vehicle.builder()
                                        .placa("CAR-001")
                                        .numeroCirculacion("TUC-SEC1") // Carreta también inscrita
                                        .build())
                                .secundario(Vehicle.builder()
                                        .placa("CAR-002")
                                        // Sin TUC: carreta sin obligación de inscripción
                                        .build())
                                .build())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Centro de distribución, Lima")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("060101")
                                .direccion("Depósito Cajamarca")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("500.00"))
                        .unidadMedida("KGM")
                        .codigo("FERT-001")
                        .descripcion("Fertilizantes")
                        .build())
                .build();

        assertInput(gre, "vehiculoConCarreta.xml");
    }

    /**
     * Casuística 5: Transporte público — transportista consignado en GRE-Remitente.
     * <p>
     * El remitente contrata un servicio de transporte público y consigna los
     * datos del transportista contratado (CarrierParty) en su GRE-Remitente.
     * <p>
     * <b>IMPORTANTE:</b> Este XML refleja solo la perspectiva del remitente.
     * El transportista efectivo que realiza el traslado debe emitir su propia
     * GRE-Transportista (serie V*) con conductor, vehículo y referencia a esta GRE.
     * En transporte público, la GRE-Remitente y la GRE-Transportista pueden
     * coexistir para el mismo traslado, según corresponda al sujeto obligado
     * y al flujo operativo aplicable.
     * traslado.
     * <p>
     * Ref: RS 000123-2022/SUNAT — relación entre GRE-Remitente y GRE-Transportista.
     */
    @Test
    public void testTransporteSubcontratado() throws Exception {
        GRERemitente gre = GRERemitente.builder()
                .serie("T001")
                .numero(5)
                .remitente(Remitente.builder()
                        .ruc("20100010001")
                        .razonSocial("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Tienda Destino S.A.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("3000.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .transportista(Transportista.builder()
                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                .numeroDocumentoIdentidad("20400040004")
                                .nombre("Logística Express S.A.C.")
                                .numeroRegistroMTC("MTC-567890")
                                .build())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Almacén Principal, Lima")
                                .codigoLocal("0001")
                                .ruc("20100010001")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("150132")
                                .direccion("Tienda San Isidro")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("200.00"))
                        .unidadMedida("NIU")
                        .codigo("ELEC-001")
                        .descripcion("Equipos electrónicos")
                        .build())
                .build();

        assertInput(gre, "transporteSubcontratado.xml");
    }

    /**
     * Casuística 6: Transporte privado en vehículo de categoría M1 o L.
     * <p>
     * Cuando el traslado se realiza en vehículos de categoría M1 (automóviles)
     * o categoría L (motocicletas, mototaxis), SUNAT permite no consignar
     * conductor ni datos del vehículo.
     * <p>
     * Se utiliza el indicador {@code SUNAT_Envio_IndicadorTrasladoVehiculoM1L}.
     * <p>
     * <b>PENDIENTE DE VALIDACIÓN DOCUMENTAL:</b> El token exacto
     * {@code SUNAT_Envio_IndicadorTrasladoVehiculoM1L} debe confirmarse contra
     * el Anexo N.° 14 o el catálogo interno del portal SUNAT. La separación
     * conceptual respecto de {@code SUNAT_Envio_IndicadorTransbordoProgramado}
     * es correcta, pero el literal del indicador M1/L queda sujeto a
     * verificación final antes de considerar este caso como cerrado.
     * <p>
     * Ref: Anexo N.° 14 UBL 2.1, RS 000123-2022/SUNAT.
     */
    @Test
    public void testVehiculoM1L() throws Exception {
        GRERemitente gre = GRERemitente.builder()
                .serie("T001")
                .numero(6)
                .remitente(Remitente.builder()
                        .ruc("20100010001")
                        .razonSocial("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("87654321")
                        .nombre("Persona Natural")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("5.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .indicador(IndicadorEnvio.VEHICULO_M1_L.getCode())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Tienda Lima Centro")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("150101")
                                .direccion("Domicilio Cliente")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("1.00"))
                        .unidadMedida("NIU")
                        .codigo("PEQ-001")
                        .descripcion("Paquete pequeño")
                        .build())
                .build();

        assertInput(gre, "vehiculoM1L.xml");
    }

    /**
     * Casuística 7: Mercancía extranjera — motivo 19 desde zona primaria.
     * <p>
     * RS 000240-2024/SUNAT creó el motivo "19 - Traslado de mercancía extranjera"
     * específicamente para la trazabilidad de mercancía que sale de zona primaria
     * (puerto/aeropuerto) hacia depósito temporal, sin destinación aduanera o sin
     * levante.
     * <p>
     * Los campos contenedor, precinto y el indicador de zona primaria son
     * condicionales al motivo 19 del Catálogo 20 — no aplican a cualquier
     * motivo de importación.
     * <p>
     * <b>NOTA:</b> La obligatoriedad plena del motivo 19 (en reemplazo del ticket
     * de salida) fue pospuesta al 01-jul-2026 por RS 000133-2025/SUNAT.
     * Hasta esa fecha coexisten el ticket de salida y la GRE con motivo 19.
     */
    @Test
    public void testMercanciaExtranjeraMotivo19() throws Exception {
        GRERemitente gre = GRERemitente.builder()
                .serie("T001")
                .numero(7)
                .remitente(Remitente.builder()
                        .ruc("20500050005")
                        .razonSocial("Depósito Temporal del Callao S.A.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20500050005")
                        .nombre("Depósito Temporal del Callao S.A.")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.TRASLADO_MERCANCIA_EXTRANJERA.getCode()) // "19"
                        .pesoTotal(new BigDecimal("12000.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .transportista(Transportista.builder()
                                .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                                .numeroDocumentoIdentidad("20600060006")
                                .nombre("Transporte Portuario S.A.C.")
                                .build())
                        .indicador(IndicadorEnvio.TRASLADO_TOTAL_DAM_DS.getCode())
                        .contenedor(Contenedor.builder()
                                .numero("MSKU-2024-001")
                                .precinto("SEAL-001")
                                .build())
                        .contenedor(Contenedor.builder()
                                .numero("MSKU-2024-002")
                                .precinto("SEAL-002")
                                .build())
                        .puerto(Puerto.builder()
                                .codigo("CALLAO")
                                .nombre("Puerto del Callao")
                                .build())
                        .partida(Partida.builder()
                                .ubigeo("070101")
                                .direccion("Terminal Portuario del Callao")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("150101")
                                .direccion("Almacén Depósito Temporal")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("1.00"))
                        .unidadMedida("ZZ")
                        .codigo("CONT-CONSOLIDADO")
                        .descripcion("Contenedor carga consolidada - mercancía extranjera")
                        .build())
                .build();

        assertInput(gre, "mercanciaExtranjeraMotivo19.xml");
    }
}
