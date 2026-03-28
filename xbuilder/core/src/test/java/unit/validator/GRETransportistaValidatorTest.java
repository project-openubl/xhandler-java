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
 * Tests unitarios para la validación de {@link GRETransportista}.
 */
public class GRETransportistaValidatorTest {

    private static GRETransportista.GRETransportistaBuilder minimalTransportista() {
        return GRETransportista.builder()
                .serie("V001")
                .numero(1)
                .transportistaEmisor(Transportista.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20300030003").nombre("Transportes S.A.C.").build())
                .remitente(Tercero.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20100010001").nombre("Remitente S.A.C.").build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002").nombre("Destino S.A.").build())
                .conductor(Driver.builder()
                        .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("44444444")
                        .nombres("M").apellidos("T").licencia("Q444").build())
                .vehiculo(Vehicle.builder().placa("XYZ-789").build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(LocalDate.now())
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build());
    }

    @Nested
    class ValidCases {

        @Test
        public void testTransportistaValido() {
            GRETransportista gre = minimalTransportista().build();
            List<String> errors = gre.validate();
            assertTrue(errors.isEmpty(), "Debería ser válido: " + errors);
        }
    }

    @Nested
    class SerieValidation {

        @Test
        public void testSerieFaltante() {
            GRETransportista gre = minimalTransportista().serie(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("serie")));
        }

        @Test
        public void testSerieInvalidaT() {
            GRETransportista gre = minimalTransportista().serie("T001").build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("inicie con 'V'")));
        }

        @Test
        public void testNumeroInvalido() {
            GRETransportista gre = minimalTransportista().numero(0).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("mayor a 0")));
        }
    }

    @Nested
    class PartyValidation {

        @Test
        public void testTransportistaEmisorFaltante() {
            GRETransportista gre = minimalTransportista().transportistaEmisor(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("transportista emisor")));
        }

        @Test
        public void testRUCTransportistaInvalido() {
            GRETransportista gre = minimalTransportista()
                    .transportistaEmisor(Transportista.builder()
                            .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                            .numeroDocumentoIdentidad("123").nombre("X").build())
                    .build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("11 dígitos")));
        }

        @Test
        public void testRemitenteFaltante() {
            GRETransportista gre = minimalTransportista().remitente(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("remitente original")));
        }

        @Test
        public void testDestinatarioFaltante() {
            GRETransportista gre = minimalTransportista().destinatario(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("destinatario")));
        }
    }

    @Nested
    class ConductorVehiculoValidation {

        @Test
        public void testSinConductor() {
            GRETransportista gre = GRETransportista.builder()
                    .serie("V001").numero(1)
                    .transportistaEmisor(Transportista.builder()
                            .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                            .numeroDocumentoIdentidad("20300030003").nombre("T").build())
                    .remitente(Tercero.builder().tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20100010001").nombre("R").build())
                    .destinatario(Destinatario.builder().tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20200020002").nombre("D").build())
                    // Sin conductor
                    .vehiculo(Vehicle.builder().placa("XYZ-789").build())
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
            assertTrue(errors.stream().anyMatch(e -> e.contains("conductor")),
                    "Transportista siempre requiere conductor");
        }

        @Test
        public void testSinVehiculo() {
            GRETransportista gre = minimalTransportista().vehiculo(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("vehículo")),
                    "Transportista siempre requiere vehículo");
        }
    }

    @Nested
    class EnvioValidation {

        @Test
        public void testSinEnvio() {
            GRETransportista gre = minimalTransportista().envio(null).build();
            List<String> errors = gre.validate();
            assertTrue(errors.stream().anyMatch(e -> e.contains("envío")));
        }

        @Test
        public void testSinDetalles() {
            GRETransportista gre = GRETransportista.builder()
                    .serie("V001").numero(1)
                    .transportistaEmisor(Transportista.builder()
                            .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                            .numeroDocumentoIdentidad("20300030003").nombre("T").build())
                    .remitente(Tercero.builder().tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20100010001").nombre("R").build())
                    .destinatario(Destinatario.builder().tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20200020002").nombre("D").build())
                    .conductor(Driver.builder().tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("44444444")
                            .nombres("M").apellidos("T").licencia("Q444").build())
                    .vehiculo(Vehicle.builder().placa("XYZ-789").build())
                    .envio(Envio.builder()
                            .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("01").fechaTraslado(LocalDate.now())
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
            GRETransportista gre = minimalTransportista().build();
            DespatchAdvice da = gre.toDespatchAdvice();

            assertEquals("31", da.getTipoComprobante());
            assertEquals("V001", da.getSerie());
            assertEquals(1, da.getNumero());
            assertTrue(da.isGRETransportista());
            assertNotNull(da.getTercero(), "Tercero debe estar presente");
            assertNotNull(da.getEnvio().getChoferes(), "Conductores deben inyectarse en envío");
            assertFalse(da.getEnvio().getChoferes().isEmpty());
            assertNotNull(da.getEnvio().getVehiculo(), "Vehículo debe inyectarse en envío");
        }

        @Test
        public void testConductoresInyectadosEnEnvio() {
            GRETransportista gre = minimalTransportista()
                    .conductor(Driver.builder()
                            .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("99999999")
                            .nombres("Extra").apellidos("Driver").licencia("Q999").build())
                    .build();
            DespatchAdvice da = gre.toDespatchAdvice();
            assertEquals(2, da.getEnvio().getChoferes().size(),
                    "Ambos conductores deben inyectarse en el envío");
        }

        @Test
        public void testToDespatchAdviceValidatedSuccess() {
            GRETransportista gre = minimalTransportista().build();
            DespatchAdvice da = gre.toDespatchAdviceValidated();
            assertNotNull(da);
            assertTrue(da.isGRETransportista());
        }

        @Test
        public void testToDespatchAdviceValidatedFails() {
            GRETransportista gre = minimalTransportista().serie("T001").build();
            assertThrows(IllegalStateException.class, gre::toDespatchAdviceValidated);
        }
    }
}
