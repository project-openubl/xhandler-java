package unit.validator;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog18;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog20;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la validación de {@link GRERemitente}.
 */
public class GRERemitenteValidatorTest {

    private static GRERemitente.GRERemitenteBuilder minimalPrivado() {
        return GRERemitente.builder()
                .serie("T001")
                .numero(1)
                .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test S.A.C.").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                        .numeroDocumentoIdentidad("12345678").nombre("Cliente").build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PRIVADO.getCode())
                        .fechaTraslado(LocalDate.now())
                        .chofer(Driver.builder()
                                .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("11111111")
                                .nombres("J").apellidos("P").licencia("Q123").build())
                        .vehiculo(Vehicle.builder().placa("ABC-123").build())
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build());
    }

    private static GRERemitente.GRERemitenteBuilder minimalPublico() {
        return GRERemitente.builder()
                .serie("T001")
                .numero(1)
                .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test S.A.C.").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002").nombre("Cliente S.A.").build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(LocalDate.now())
                        .transportista(Transportista.builder()
                                .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20300030003")
                                .nombre("Transportes S.A.C.").build())
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build());
    }

    @Nested
    class ValidCases {

        @Test
        public void testGRERemitentePrivadoValido() {
            GRERemitente gre = minimalPrivado().build();
            List<String> errors = gre.validate();
            assertTrue(errors.isEmpty(), "Debería ser válido: " + errors);
        }

        @Test
        public void testGRERemitentePublicoValido() {
            GRERemitente gre = minimalPublico().build();
            List<String> errors = gre.validate();
            assertTrue(errors.isEmpty(), "Debería ser válido: " + errors);
        }

        @Test
        public void testVehiculoM1LSinConductor() {
            GRERemitente gre = GRERemitente.builder()
                    .serie("T001").numero(1)
                    .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                    .envio(Envio.builder()
                            .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
                            .indicador("SUNAT_Envio_IndicadorTrasladoVehiculoM1L")
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                    .build();
            List<String> errors = gre.validate();
            assertTrue(errors.isEmpty(), "M1/L no requiere conductor: " + errors);
        }
    }

    @Nested
    class SerieValidation {

        @Test
        public void testSerieFaltante() {
            GRERemitente gre = minimalPrivado().serie(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("serie")));
        }

        @Test
        public void testSerieInvalidaV() {
            GRERemitente gre = minimalPrivado().serie("V001").build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("inicie con 'T'")));
        }

        @Test
        public void testNumeroInvalido() {
            GRERemitente gre = minimalPrivado().numero(0).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("mayor a 0")));
        }
    }

    @Nested
    class PartyValidation {

        @Test
        public void testRemitenteFaltante() {
            GRERemitente gre = minimalPrivado().remitente(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("remitente")));
        }

        @Test
        public void testRUCInvalido() {
            GRERemitente gre = minimalPrivado()
                    .remitente(Remitente.builder().ruc("123").razonSocial("X").build())
                    .build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("11 dígitos")));
        }

        @Test
        public void testDestinatarioFaltante() {
            GRERemitente gre = minimalPrivado().destinatario(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("destinatario")));
        }
    }

    @Nested
    class TransportePrivadoValidation {

        @Test
        public void testSinConductor() {
            GRERemitente gre = GRERemitente.builder()
                    .serie("T001").numero(1)
                    .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                    .envio(Envio.builder()
                            .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
                            .vehiculo(Vehicle.builder().placa("ABC-123").build())
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                    .build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("conductor")));
        }

        @Test
        public void testSinVehiculo() {
            GRERemitente gre = GRERemitente.builder()
                    .serie("T001").numero(1)
                    .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                    .envio(Envio.builder()
                            .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
                            .chofer(Driver.builder().tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("11111111")
                                    .nombres("J").apellidos("P").licencia("Q123").build())
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                    .build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("vehículo")));
        }

        @Test
        public void testPrivadoConTransportista() {
            // Transporte privado NO debe tener transportista externo
            GRERemitente gre = minimalPrivado().envio(Envio.builder()
                    .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                    .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
                    .chofer(Driver.builder().tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("11111111")
                            .nombres("J").apellidos("P").licencia("Q123").build())
                    .vehiculo(Vehicle.builder().placa("ABC-123").build())
                    .transportista(Transportista.builder()
                            .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20300030003")
                            .nombre("Trans S.A.C.").build())
                    .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                    .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                    .build()).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("Transporte privado no debe consignar transportista")));
        }
    }

    @Nested
    class TransportePublicoValidation {

        @Test
        public void testSinTransportista() {
            GRERemitente gre = GRERemitente.builder()
                    .serie("T001").numero(1)
                    .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                    .envio(Envio.builder()
                            .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("01").fechaTraslado(LocalDate.now())
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
                    .build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("transportista")));
        }
    }

    @Nested
    class EnvioValidation {

        @Test
        public void testSinEnvio() {
            GRERemitente gre = minimalPrivado().envio(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("envío")));
        }

        @Test
        public void testSinDetalles() {
            GRERemitente gre = GRERemitente.builder()
                    .serie("T001").numero(1)
                    .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678").nombre("C").build())
                    .envio(Envio.builder()
                            .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
                            .chofer(Driver.builder().tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("11111111")
                                    .nombres("J").apellidos("P").licencia("Q123").build())
                            .vehiculo(Vehicle.builder().placa("ABC-123").build())
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("detalle")));
        }
    }

    @Nested
    class ConversionTests {

        @Test
        public void testToDespatchAdvice() {
            GRERemitente gre = minimalPrivado().build();
            DespatchAdvice da = gre.toDespatchAdvice();

            assertEquals("09", da.getTipoComprobante());
            assertEquals("T001", da.getSerie());
            assertEquals(1, da.getNumero());
            assertNotNull(da.getRemitente());
            assertNotNull(da.getDestinatario());
            assertNotNull(da.getEnvio());
            assertFalse(da.getDetalles().isEmpty());
        }

        @Test
        public void testToDespatchAdviceValidatedSuccess() {
            GRERemitente gre = minimalPrivado().build();
            DespatchAdvice da = gre.toDespatchAdviceValidated();
            assertNotNull(da);
            assertTrue(da.isGRERemitente());
        }

        @Test
        public void testToDespatchAdviceValidatedFails() {
            GRERemitente gre = minimalPrivado().serie("V001").build();
            assertThrows(IllegalStateException.class, gre::toDespatchAdviceValidated);
        }
    }
}
