package unit.validator;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog18;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog20;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración que verifican la coherencia entre los tres validadores (DespatchAdviceValidator, GRERemitente,
 * GRETransportista) después de la centralización en DespatchAdviceCommonValidator.
 * <p>
 * Asegura que:
 * <ul>
 * <li>La misma regla produce el mismo resultado en los tres puntos de entrada</li>
 * <li>Los mensajes de error tienen el texto esperado</li>
 * <li>La conversión toDespatchAdvice() + validación produce resultados coherentes</li>
 * <li>validate() y validateDetailed() son consistentes</li>
 * </ul>
 */
public class ValidationCoherenceTest {

    // ================================================================
    // Helpers
    // ================================================================

    private static GRERemitente.GRERemitenteBuilder minimalRemitente() {
        return GRERemitente.builder()
                .serie("T001")
                .numero(1)
                .remitente(Remitente.builder().ruc("12345678912").razonSocial("Test S.A.C.").build())
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
                                .tipoDocumentoIdentidad("1")
                                .numeroDocumentoIdentidad("11111111")
                                .nombres("J")
                                .apellidos("P")
                                .licencia("Q123")
                                .build())
                        .vehiculo(Vehicle.builder().placa("ABC-123").build())
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(BigDecimal.ONE)
                        .unidadMedida("NIU")
                        .codigo("001")
                        .build());
    }

    private static GRETransportista.GRETransportistaBuilder minimalTransportista() {
        return GRETransportista.builder()
                .serie("V001")
                .numero(1)
                .transportistaEmisor(Transportista.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20300030003")
                        .nombre("Transportes S.A.C.")
                        .build())
                .remitente(Tercero.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20100010001")
                        .nombre("Remitente S.A.C.")
                        .build())
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20200020002")
                        .nombre("Destino S.A.")
                        .build())
                .conductor(Driver.builder()
                        .tipoDocumentoIdentidad("1")
                        .numeroDocumentoIdentidad("44444444")
                        .nombres("M")
                        .apellidos("T")
                        .licencia("Q444")
                        .build())
                .vehiculo(Vehicle.builder().placa("XYZ-789").build())
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(BigDecimal.ONE)
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(LocalDate.now())
                        .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                        .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                        .build())
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(BigDecimal.ONE)
                        .unidadMedida("NIU")
                        .codigo("001")
                        .build());
    }

    // ================================================================
    // Coherencia: GRERemitente.validate() ↔ DespatchAdviceValidator.validate()
    // ================================================================

    @Nested
    class RemitenteCoherence {

        @Test
        public void testValidRemitentePassesBothValidators() {
            GRERemitente gre = minimalRemitente().build();

            // GRERemitente propio
            List<String> remitenteErrors = gre.validate();
            assertTrue(remitenteErrors.isEmpty(), "GRERemitente.validate: " + remitenteErrors);

            // Post-conversión
            DespatchAdvice da = gre.toDespatchAdvice();
            List<String> daErrors = DespatchAdviceValidator.validate(da);
            assertTrue(daErrors.isEmpty(), "DespatchAdviceValidator.validate: " + daErrors);
        }

        @Test
        public void testInvalidSerieDetectedByBoth() {
            GRERemitente gre = minimalRemitente().serie("V001").build();

            List<String> remitenteErrors = gre.validate();
            assertTrue(remitenteErrors.stream().anyMatch(e -> e.contains("'T'")),
                    "GRERemitente debe detectar serie inválida");

            DespatchAdvice da = gre.toDespatchAdvice();
            List<String> daErrors = DespatchAdviceValidator.validate(da);
            assertTrue(daErrors.stream().anyMatch(e -> e.contains("GRE-Remitente (09)")),
                    "DespatchAdviceValidator debe detectar serie incoherente con tipo 09");
        }

        @Test
        public void testRucInvalidoDetectedByBoth() {
            GRERemitente gre = minimalRemitente()
                    .remitente(Remitente.builder().ruc("123").razonSocial("X").build())
                    .build();

            List<String> remitenteErrors = gre.validate();
            assertTrue(remitenteErrors.stream().anyMatch(e -> e.contains("11 dígitos")));

            DespatchAdvice da = gre.toDespatchAdvice();
            List<String> daErrors = DespatchAdviceValidator.validate(da);
            assertTrue(daErrors.stream().anyMatch(e -> e.contains("11 dígitos")));
        }

        @Test
        public void testValidateAndValidateDetailedAreConsistent() {
            GRERemitente gre = minimalRemitente().serie(null).build();

            List<String> errors = gre.validate();
            ValidationResult result = gre.validateDetailed();

            assertEquals(errors, result.getErrors(),
                    "validate() y validateDetailed().getErrors() deben coincidir");
        }
    }

    // ================================================================
    // Coherencia: GRETransportista.validate() ↔ DespatchAdviceValidator.validate()
    // ================================================================

    @Nested
    class TransportistaCoherence {

        @Test
        public void testValidTransportistaPassesBothValidators() {
            GRETransportista gre = minimalTransportista().build();

            List<String> transportistaErrors = gre.validate();
            assertTrue(transportistaErrors.isEmpty(),
                    "GRETransportista.validate: " + transportistaErrors);

            DespatchAdvice da = gre.toDespatchAdvice();
            List<String> daErrors = DespatchAdviceValidator.validate(da);
            assertTrue(daErrors.isEmpty(),
                    "DespatchAdviceValidator.validate: " + daErrors);
        }

        @Test
        public void testInvalidSerieDetectedByBoth() {
            GRETransportista gre = minimalTransportista().serie("T001").build();

            List<String> transportistaErrors = gre.validate();
            assertTrue(transportistaErrors.stream().anyMatch(e -> e.contains("'V'")));

            DespatchAdvice da = gre.toDespatchAdvice();
            List<String> daErrors = DespatchAdviceValidator.validate(da);
            assertTrue(daErrors.stream().anyMatch(e -> e.contains("GRE-Transportista (31)")));
        }

        @Test
        public void testConversionInjectsConductoresAndVehiculo() {
            GRETransportista gre = minimalTransportista()
                    .conductor(Driver.builder()
                            .tipoDocumentoIdentidad("1")
                            .numeroDocumentoIdentidad("99999999")
                            .nombres("Extra")
                            .apellidos("D")
                            .licencia("Q999")
                            .build())
                    .build();

            DespatchAdvice da = gre.toDespatchAdvice();
            assertNotNull(da.getEnvio().getChoferes());
            assertEquals(2, da.getEnvio().getChoferes().size(),
                    "Conductores deben inyectarse en envío");
            assertNotNull(da.getEnvio().getVehiculo(),
                    "Vehículo debe inyectarse en envío");
        }

        @Test
        public void testValidateAndValidateDetailedAreConsistent() {
            GRETransportista gre = minimalTransportista()
                    .transportistaEmisor(null)
                    .build();

            List<String> errors = gre.validate();
            ValidationResult result = gre.validateDetailed();

            assertEquals(errors, result.getErrors());
        }
    }

    // ================================================================
    // toDespatchAdviceValidated() integration
    // ================================================================

    @Nested
    class ValidatedConversion {

        @Test
        public void testRemitenteValidatedSuccess() {
            GRERemitente gre = minimalRemitente().build();
            DespatchAdvice da = gre.toDespatchAdviceValidated();
            assertNotNull(da);
            assertEquals("09", da.getTipoComprobante());
            assertTrue(da.isGRERemitente());
        }

        @Test
        public void testRemitenteValidatedFails() {
            GRERemitente gre = minimalRemitente().serie("V001").build();
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    gre::toDespatchAdviceValidated);
            assertTrue(ex.getMessage().contains("GRE-Remitente inválido"));
        }

        @Test
        public void testTransportistaValidatedSuccess() {
            GRETransportista gre = minimalTransportista().build();
            DespatchAdvice da = gre.toDespatchAdviceValidated();
            assertNotNull(da);
            assertEquals("31", da.getTipoComprobante());
            assertTrue(da.isGRETransportista());
        }

        @Test
        public void testTransportistaValidatedFails() {
            GRETransportista gre = minimalTransportista().serie("T001").build();
            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    gre::toDespatchAdviceValidated);
            assertTrue(ex.getMessage().contains("GRE-Transportista inválido"));
        }
    }

    // ================================================================
    // Severidad uniformidad
    // ================================================================

    @Nested
    class SeverityUniformity {

        @Test
        public void testComercioExteriorWarningsInAllThreeValidators() {
            // DespatchAdviceValidator
            DespatchAdvice da = DespatchAdvice.builder()
                    .serie("T001")
                    .numero(1)
                    .tipoComprobante("09")
                    .remitente(Remitente.builder().ruc("12345678912").razonSocial("T").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1")
                            .numeroDocumentoIdentidad("12345678")
                            .nombre("C")
                            .build())
                    .envio(Envio.builder()
                            .tipoTraslado("08") // Importación
                            .pesoTotal(BigDecimal.ONE)
                            .pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("02")
                            .fechaTraslado(LocalDate.now())
                            .chofer(Driver.builder()
                                    .tipoDocumentoIdentidad("1")
                                    .numeroDocumentoIdentidad("11111111")
                                    .nombres("J")
                                    .apellidos("P")
                                    .licencia("Q123")
                                    .build())
                            .vehiculo(Vehicle.builder().placa("ABC-123").build())
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE)
                            .unidadMedida("NIU")
                            .codigo("001")
                            .build())
                    .build();

            ValidationResult daResult = DespatchAdviceValidator.validateDetailed(da);
            assertTrue(daResult.isValid(), "Importación sin DAM: valid (warnings only)");
            assertTrue(daResult.hasWarnings(), "Debe tener warnings");
            assertTrue(daResult.getWarnings().stream().anyMatch(w -> w.contains("DAM/DS")));

            // GRERemitente: same
            GRERemitente gre = minimalRemitente().envio(Envio.builder()
                    .tipoTraslado("08")
                    .pesoTotal(BigDecimal.ONE)
                    .pesoTotalUnidadMedida("KGM")
                    .tipoModalidadTraslado("02")
                    .fechaTraslado(LocalDate.now())
                    .chofer(Driver.builder()
                            .tipoDocumentoIdentidad("1")
                            .numeroDocumentoIdentidad("11111111")
                            .nombres("J")
                            .apellidos("P")
                            .licencia("Q123")
                            .build())
                    .vehiculo(Vehicle.builder().placa("ABC-123").build())
                    .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                    .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                    .build()).build();

            ValidationResult greResult = gre.validateDetailed();
            assertTrue(greResult.isValid());
            assertTrue(greResult.hasWarnings());
            assertTrue(greResult.getWarnings().stream().anyMatch(w -> w.contains("DAM/DS")));
        }

        @Test
        public void testHardErrorsNeverDowngradedToWarning() {
            // Missing serie: must be ERROR everywhere
            GRERemitente gre = minimalRemitente().serie(null).build();
            ValidationResult result = gre.validateDetailed();
            assertTrue(result.hasErrors());
            assertTrue(result.getMessages()
                    .stream()
                    .filter(m -> m.getMessage().contains("serie"))
                    .allMatch(m -> m
                            .getSeverity() == io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationSeverity.ERROR));
        }
    }
}
