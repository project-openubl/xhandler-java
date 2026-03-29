package unit.validator;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog18;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog20;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link DespatchAdviceValidator}.
 * <p>
 * Valida las reglas de negocio SUNAT sin depender del renderizado XML.
 */
public class DespatchAdviceValidatorTest {

    private static DespatchAdvice.DespatchAdviceBuilder minimalGRERemitente() {
        return DespatchAdvice.builder()
                .serie("T001")
                .numero(1)
                .tipoComprobante("09")
                .remitente(Remitente.builder()
                        .ruc("12345678912")
                        .razonSocial("Test S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("12345678")
                        .nombre("Cliente")
                        .build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(BigDecimal.ONE)
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                        .fechaTraslado(LocalDate.now())
                        .chofer(Driver.builder()
                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                .numeroDocumentoIdentidad("11111111")
                                .nombres("Juan")
                                .apellidos("Perez")
                                .licencia("Q1234567")
                                .build())
                        .vehiculo(Vehicle.builder()
                                .placa("ABC-123")
                                .build())
                        .partida(Partida.builder().ubigeo("010101").direccion("Origen").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("Destino").build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(BigDecimal.ONE)
                        .unidadMedida("NIU")
                        .codigo("001")
                        .build());
    }

    @Test
    public void testValidGRERemitente() {
        DespatchAdvice da = minimalGRERemitente().build();
        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.isEmpty(), "GRE-Remitente válido: " + errors);
    }

    @Test
    public void testSerieInvalida_GRERemitente() {
        DespatchAdvice da = minimalGRERemitente()
                .serie("V001") // Serie V no corresponde a tipo 09
                .build();
        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.stream().anyMatch(e -> e.contains("GRE-Remitente (09) requiere serie que inicie con 'T'")),
                "Debe detectar serie inválida para GRE-Remitente");
    }

    @Test
    public void testSerieInvalida_GRETransportista() {
        DespatchAdvice da = minimalGRERemitente()
                .serie("T001")
                .tipoComprobante("31") // Transportista requiere V*
                .build();
        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(
                errors.stream().anyMatch(e -> e.contains("GRE-Transportista (31) requiere serie que inicie con 'V'")),
                "Debe detectar serie inválida para GRE-Transportista");
    }

    @Test
    public void testTransportePrivadoSinConductor() {
        DespatchAdvice da = DespatchAdvice.builder()
                .serie("T001")
                .numero(1)
                .tipoComprobante("09")
                .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                .envio(Envio.builder()
                        .tipoTraslado("01")
                        .pesoTotal(BigDecimal.ONE)
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode()) // "02"
                        .fechaTraslado(LocalDate.now())
                        // Sin conductor ni vehículo
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(
                        DespatchAdviceItem.builder().cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                .build();

        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.stream().anyMatch(e -> e.contains("conductor")),
                "Transporte privado requiere conductor");
        assertTrue(errors.stream().anyMatch(e -> e.contains("vehículo")),
                "Transporte privado requiere vehículo");
    }

    @Test
    public void testTransportePublicoSinTransportista() {
        DespatchAdvice da = DespatchAdvice.builder()
                .serie("T001")
                .numero(1)
                .tipoComprobante("09")
                .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                .envio(Envio.builder()
                        .tipoTraslado("01")
                        .pesoTotal(BigDecimal.ONE)
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode()) // "01"
                        .fechaTraslado(LocalDate.now())
                        // Sin transportista
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(
                        DespatchAdviceItem.builder().cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                .build();

        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.stream().anyMatch(e -> e.contains("transportista")),
                "Transporte público requiere datos del transportista");
    }

    @Test
    public void testGRETransportistaSinConductor() {
        DespatchAdvice da = DespatchAdvice.builder()
                .serie("V001")
                .numero(1)
                .tipoComprobante("31")
                .remitente(Remitente.builder().ruc("20123456789").razonSocial("Transportes S.A.C.").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20876543210").nombre("C").build())
                .tercero(Tercero.builder()
                        .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20555555555")
                        .nombre("Remitente Original").build())
                .envio(Envio.builder()
                        .tipoTraslado("01")
                        .pesoTotal(BigDecimal.ONE)
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(LocalDate.now())
                        // Sin conductor ni vehículo (obligatorio para transportista)
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(
                        DespatchAdviceItem.builder().cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                .build();

        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.stream().anyMatch(e -> e.contains("GRE-Transportista requiere al menos un conductor")),
                "GRE-Transportista siempre requiere conductor");
        assertTrue(errors.stream().anyMatch(e -> e.contains("GRE-Transportista requiere datos del vehículo")),
                "GRE-Transportista siempre requiere vehículo");
    }

    @Test
    public void testGRETransportistaSinTercero() {
        DespatchAdvice da = DespatchAdvice.builder()
                .serie("V001")
                .numero(1)
                .tipoComprobante("31")
                .remitente(Remitente.builder().ruc("20123456789").razonSocial("Transportes S.A.C.").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20876543210").nombre("C").build())
                // Sin tercero ni proveedor - se espera recomendación
                .envio(Envio.builder()
                        .tipoTraslado("01")
                        .pesoTotal(BigDecimal.ONE)
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado("01")
                        .fechaTraslado(LocalDate.now())
                        .chofer(Driver.builder()
                                .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("11111111")
                                .nombres("J").apellidos("P").licencia("Q1234567").build())
                        .vehiculo(Vehicle.builder().placa("ABC-123").build())
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(
                        DespatchAdviceItem.builder().cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                .build();

        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.stream().anyMatch(e -> e.contains("tercero")),
                "GRE-Transportista debe advertir que falta el tercero");
    }

    @Test
    public void testSinDetalles() {
        DespatchAdvice da = DespatchAdvice.builder()
                .serie("T001")
                .numero(1)
                .tipoComprobante("09")
                .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                .envio(Envio.builder()
                        .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
                        .chofer(Driver.builder().tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("11111111")
                                .nombres("J").apellidos("P").licencia("Q1234567").build())
                        .vehiculo(Vehicle.builder().placa("ABC-123").build())
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .build();

        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.stream().anyMatch(e -> e.contains("línea de detalle")),
                "Debe requerir al menos una línea de detalle");
    }

    @Test
    public void testRUCInvalido() {
        DespatchAdvice da = minimalGRERemitente().build();
        da.getRemitente().setRuc("123"); // RUC inválido

        List<String> errors = DespatchAdviceValidator.validate(da);
        assertTrue(errors.stream().anyMatch(e -> e.contains("11 dígitos")),
                "Debe rechazar RUC con longitud incorrecta");
    }

    @Test
    public void testHelperMethods() {
        DespatchAdvice remitente = DespatchAdvice.builder().tipoComprobante("09").build();
        assertTrue(remitente.isGRERemitente());
        assertFalse(remitente.isGRETransportista());

        DespatchAdvice transportista = DespatchAdvice.builder().tipoComprobante("31").build();
        assertFalse(transportista.isGRERemitente());
        assertTrue(transportista.isGRETransportista());
    }
}
