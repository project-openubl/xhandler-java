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
 * Tests de GRE-Transportista usando el modelo {@link GRETransportista}.
 * <p>
 * Casuísticas cubiertas:
 * <ol>
 * <li>Transportista básico (un conductor, un vehículo)</li>
 * <li>Transportista con múltiples conductores (relevo)</li>
 * <li>Transportista con vehículo + carreta</li>
 * <li>Transportista en comercio exterior con contenedores</li>
 * </ol>
 */
public class GRETransportistaCasuisticasTest extends AbstractTest {

    /**
     * Casuística 1: GRE-Transportista BÁSICA.
     * <p>
     * El transportista emite la guía para trasladar bienes de un cliente.
     * En esta casuística básica se consigna conductor y vehículo.
     */
    @Test
    public void testTransportistaBasico() throws Exception {
        GRETransportista gre = GRETransportista.builder()
                .serie("V001")
                .numero(1)
                .transportistaEmisor(Transportista.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20300030003")
                        .nombre("Transportes Rápidos S.A.C.")
                        .numeroRegistroMTC("MTC-001234")
                        .build())
                .remitente(Tercero.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20100010001")
                        .nombre("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Cliente Final S.A.")
                        .build())
                .conductor(Driver.builder()
                        .tipo(TipoConductor.PRINCIPAL.getCode())
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("44444444")
                        .nombres("Miguel")
                        .apellidos("Torres")
                        .licencia("Q4444444")
                        .build())
                .vehiculo(Vehicle.builder()
                        .placa("JKL-012")
                        .numeroCirculacion("TUC-JKL") // TUC: transportista inscrito ante MTC
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("300.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Almacén Remitente, Lima")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("040101")
                                .direccion("Sucursal Arequipa")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("25.00"))
                        .unidadMedida("NIU")
                        .codigo("MER-001")
                        .descripcion("Mercadería general")
                        .build())
                .build();

        assertInput(gre, "transportistaBasico.xml");
    }

    /**
     * Casuística 2: GRE-Transportista con MÚLTIPLES CONDUCTORES.
     * <p>
     * Para viajes largos se requiere conductor de relevo.
     */
    @Test
    public void testTransportistaMultiplesConductores() throws Exception {
        GRETransportista gre = GRETransportista.builder()
                .serie("V001")
                .numero(2)
                .transportistaEmisor(Transportista.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20300030003")
                        .nombre("Transportes Rápidos S.A.C.")
                        .numeroRegistroMTC("MTC-001234")
                        .build())
                .remitente(Tercero.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20100010001")
                        .nombre("Empresa Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Destino S.A.")
                        .build())
                .conductor(Driver.builder()
                        .tipo(TipoConductor.PRINCIPAL.getCode())
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("55555555")
                        .nombres("Luis")
                        .apellidos("Fernandez")
                        .licencia("Q5555555")
                        .build())
                .conductor(Driver.builder()
                        .tipo(TipoConductor.SECUNDARIO.getCode())
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("66666666")
                        .nombres("Mario")
                        .apellidos("Vargas")
                        .licencia("Q6666666")
                        .build())
                .vehiculo(Vehicle.builder()
                        .placa("MNO-345")
                        .numeroCirculacion("TUC-MNO") // TUC: transportista inscrito ante MTC
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("5000.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .partida(Partida.builder()
                                .ubigeo("150101")
                                .direccion("Centro de Carga Lima")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("130101")
                                .direccion("Terminal Tacna")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("300.00"))
                        .unidadMedida("KGM")
                        .codigo("CARG-001")
                        .descripcion("Carga general")
                        .build())
                .build();

        assertInput(gre, "transportistaMultiplesConductores.xml");
    }

    /**
     * Casuística 3: GRE-Transportista con VEHÍCULO + CARRETA.
     * <p>
     * Vehículo principal con semirremolque adjunto. Los datos de autorización
     * y código de entidad autorizadora se consignan como datos de ejemplo
     * para esta casuística; son campos contextuales según el tipo de vehículo
     * y autorización vigente, no universalmente obligatorios.
     */
    @Test
    public void testTransportistaConCarreta() throws Exception {
        GRETransportista gre = GRETransportista.builder()
                .serie("V001")
                .numero(3)
                .transportistaEmisor(Transportista.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20300030003")
                        .nombre("Transportes Pesados S.A.C.")
                        .numeroRegistroMTC("MTC-PESADOS")
                        .build())
                .remitente(Tercero.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20100010001")
                        .nombre("Minera del Sur S.A.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Fundición Norte S.A.")
                        .build())
                .conductor(Driver.builder()
                        .tipo(TipoConductor.PRINCIPAL.getCode())
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("77777777")
                        .nombres("Andres")
                        .apellidos("Quispe")
                        .licencia("Q7777777")
                        .build())
                .vehiculo(Vehicle.builder()
                        .placa("PQR-678")
                        .numeroCirculacion("TUC-PQR")
                        .numeroAutorizacion("AUTH-PESADOS") // Dato de ejemplo condicional, no obligatorio en todo
                                                            // vehículo
                        .codigoEmisor("MTC") // Dato de ejemplo condicional, asociado a numeroAutorizacion
                        .secundario(Vehicle.builder()
                                .placa("REM-001")
                                .numeroCirculacion("TUC-REM")
                                .build())
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("20000.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .numeroDeBultos(1)
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .partida(Partida.builder()
                                .ubigeo("040101")
                                .direccion("Mina del Sur, Arequipa")
                                .build())
                        .destino(Destino.builder()
                                .ubigeo("060101")
                                .direccion("Planta procesadora, Cajamarca")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("20000.00"))
                        .unidadMedida("KGM")
                        .codigo("MIN-001")
                        .descripcion("Concentrado de cobre")
                        .build())
                .build();

        assertInput(gre, "transportistaConCarreta.xml");
    }

    /**
     * Casuística 4: GRE-Transportista en COMERCIO EXTERIOR — mercancía extranjera
     * motivo 19.
     * <p>
     * Traslado de contenedores desde zona primaria (puerto) a depósito temporal.
     * El motivo 19 fue introducido por RS 000240-2024/SUNAT dentro del esquema de
     * trazabilidad de comercio exterior. RS 000133-2025/SUNAT movió al
     * 01-jul-2026 la entrada en vigor de la disposición derogatoria del uso
     * exclusivo del ticket de salida, vinculada a este motivo.
     * <p>
     * Este caso valida el soporte de modelo/render para: contenedores con precinto,
     * puerto e indicador de traslado total. Es una casuística de modelado soportada
     * y no un ejemplo exhaustivo de todos los campos posibles del motivo 19.
     * <p>
     * Nota: la inclusión de {@code declaracionAduanera} en este escenario
     * específico
     * depende del mapeo exacto del Anexo 14 para la variante motivo 19 con traslado
     * total desde zona primaria. Se omite aquí hasta confirmar su obligatoriedad
     * para esta casuística concreta.
     */
    @Test
    public void testTransportistaComercioExterior() throws Exception {
        GRETransportista gre = GRETransportista.builder()
                .serie("V001")
                .numero(4)
                .transportistaEmisor(Transportista.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20700070007")
                        .nombre("Transportes Portuarios S.A.C.")
                        .numeroRegistroMTC("MTC-PORT-001")
                        .build())
                .remitente(Tercero.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20500050005")
                        .nombre("Importadora Nacional S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20500050005")
                        .nombre("Importadora Nacional S.A.C.")
                        .build())
                .conductor(Driver.builder()
                        .tipo(TipoConductor.PRINCIPAL.getCode())
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("88888888")
                        .nombres("Fernando")
                        .apellidos("Ruiz")
                        .licencia("Q8888888")
                        .build())
                .vehiculo(Vehicle.builder()
                        .placa("STU-901")
                        .numeroCirculacion("TUC-STU") // TUC: transportista inscrito ante MTC
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.TRASLADO_MERCANCIA_EXTRANJERA.getCode()) // "19"
                        .pesoTotal(new BigDecimal("15000.000"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .indicador(IndicadorEnvio.TRASLADO_TOTAL_DAM_DS.getCode())
                        .contenedor(Contenedor.builder()
                                .numero("CONT-IMP-001")
                                .precinto("SEAL-IMP-001")
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
                                .direccion("Almacén Lima")
                                .build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("1.00"))
                        .unidadMedida("ZZ")
                        .codigo("CARGA-EXT-001")
                        .descripcion("Carga consolidada comercio exterior")
                        .build())
                .build();

        assertInput(gre, "transportistaComercioExterior.xml");
    }
}
