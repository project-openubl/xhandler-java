package unit.validator;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog18;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog20;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.DespatchAdviceCommonValidator;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationMessage;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationResult;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationSeverity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para {@link DespatchAdviceCommonValidator} y el modelo de validación.
 * <p>
 * Verifica que las reglas centralizadas producen los mensajes correctos con la severidad adecuada (ERROR vs WARNING).
 */
public class DespatchAdviceCommonValidatorTest {

    // == Helpers para construir datos mínimos ==

    private static Envio minimalEnvio() {
        return Envio.builder()
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
                        .licencia("Q123")
                        .build())
                .vehiculo(Vehicle.builder().placa("ABC-123").build())
                .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                .build();
    }

    // ================================================================
    // ValidationMessage / ValidationResult tests
    // ================================================================

    @Nested
    class ValidationModelTests {

        @Test
        public void testValidationMessageError() {
            ValidationMessage msg = ValidationMessage.error("test error");
            assertTrue(msg.isError());
            assertFalse(msg.isWarning());
            assertEquals(ValidationSeverity.ERROR, msg.getSeverity());
            assertEquals("test error", msg.getMessage());
        }

        @Test
        public void testValidationMessageWarning() {
            ValidationMessage msg = ValidationMessage.warning("test warning");
            assertFalse(msg.isError());
            assertTrue(msg.isWarning());
            assertEquals(ValidationSeverity.WARNING, msg.getSeverity());
        }

        @Test
        public void testValidationResultEmpty() {
            ValidationResult result = new ValidationResult(new ArrayList<>());
            assertTrue(result.isValid());
            assertFalse(result.hasErrors());
            assertFalse(result.hasWarnings());
            assertTrue(result.getErrors().isEmpty());
            assertTrue(result.getWarnings().isEmpty());
        }

        @Test
        public void testValidationResultWithErrorsAndWarnings() {
            List<ValidationMessage> messages = List.of(
                    ValidationMessage.error("error 1"),
                    ValidationMessage.warning("warning 1"),
                    ValidationMessage.error("error 2"));
            ValidationResult result = new ValidationResult(messages);

            assertFalse(result.isValid());
            assertTrue(result.hasErrors());
            assertTrue(result.hasWarnings());
            assertEquals(2, result.getErrors().size());
            assertEquals(1, result.getWarnings().size());
            assertEquals(3, result.getMessages().size());
        }

        @Test
        public void testValidationResultOnlyWarnings() {
            List<ValidationMessage> messages = List.of(
                    ValidationMessage.warning("w1"),
                    ValidationMessage.warning("w2"));
            ValidationResult result = new ValidationResult(messages);

            assertTrue(result.isValid());
            assertFalse(result.hasErrors());
            assertTrue(result.hasWarnings());
        }

        @Test
        public void testValidationResultNullMessages() {
            ValidationResult result = new ValidationResult(null);
            assertTrue(result.isValid());
            assertTrue(result.getMessages().isEmpty());
        }
    }

    // ================================================================
    // BasicFields
    // ================================================================

    @Nested
    class BasicFieldsTests {

        @Test
        public void testSerieNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateBasicFields(null, 1, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("serie")));
        }

        @Test
        public void testSerieBlank() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateBasicFields("  ", 1, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("serie")));
        }

        @Test
        public void testNumeroNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateBasicFields("T001", null, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("número")));
        }

        @Test
        public void testNumeroZero() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateBasicFields("T001", 0, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("mayor a 0")));
        }

        @Test
        public void testValidBasicFields() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateBasicFields("T001", 1, msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // Serie coherence
    // ================================================================

    @Nested
    class SerieCoherenceTests {

        @Test
        public void testRemitenteSerieTOK() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateSerieCoherence("T001", "09", msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testRemitenteSerieVFails() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateSerieCoherence("V001", "09", msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("GRE-Remitente (09)")));
        }

        @Test
        public void testTransportistaSerieVOK() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateSerieCoherence("V001", "31", msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testTransportistaSerieTFails() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateSerieCoherence("T001", "31", msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("GRE-Transportista (31)")));
        }

        @Test
        public void testNullSerieSkips() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateSerieCoherence(null, "09", msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testNullTipoSkips() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateSerieCoherence("T001", null, msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // Remitente / Transportista Emisor
    // ================================================================

    @Nested
    class PartyTests {

        @Test
        public void testRemitenteNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateRemitente(null, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("remitente")));
        }

        @Test
        public void testRemitenteRucInvalido() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Remitente rem = Remitente.builder().ruc("123").razonSocial("X").build();
            DespatchAdviceCommonValidator.validateRemitente(rem, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("11 dígitos")));
        }

        @Test
        public void testRemitenteValido() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Remitente rem = Remitente.builder().ruc("12345678901").razonSocial("X").build();
            DespatchAdviceCommonValidator.validateRemitente(rem, msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testTransportistaEmisorNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateTransportistaEmisor(null, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("transportista emisor")));
        }

        @Test
        public void testTransportistaEmisorRucInvalido() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Transportista t = Transportista.builder()
                    .tipoDocumentoIdentidad("6")
                    .numeroDocumentoIdentidad("123")
                    .nombre("X")
                    .build();
            DespatchAdviceCommonValidator.validateTransportistaEmisor(t, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("11 dígitos")));
        }

        @Test
        public void testDestinatarioNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateDestinatario(null, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("destinatario")));
        }

        @Test
        public void testDestinatarioPresente() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Destinatario d = Destinatario.builder()
                    .tipoDocumentoIdentidad("1")
                    .numeroDocumentoIdentidad("12345678")
                    .nombre("C")
                    .build();
            DespatchAdviceCommonValidator.validateDestinatario(d, msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testTerceroNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateTerceroTransportista(null, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("remitente original")));
        }

        @Test
        public void testTerceroPresente() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Tercero t = Tercero.builder()
                    .tipoDocumentoIdentidad("6")
                    .numeroDocumentoIdentidad("20100010001")
                    .nombre("R")
                    .build();
            DespatchAdviceCommonValidator.validateTerceroTransportista(t, msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // Envio required fields
    // ================================================================

    @Nested
    class EnvioRequiredTests {

        @Test
        public void testEnvioNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateEnvioRequired(null, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("envío")));
        }

        @Test
        public void testEnvioSinMotivo() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .pesoTotal(BigDecimal.ONE)
                    .tipoModalidadTraslado("02")
                    .fechaTraslado(LocalDate.now())
                    .build();
            DespatchAdviceCommonValidator.validateEnvioRequired(envio, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("motivo de traslado")));
        }

        @Test
        public void testEnvioSinPeso() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .tipoModalidadTraslado("02")
                    .fechaTraslado(LocalDate.now())
                    .build();
            DespatchAdviceCommonValidator.validateEnvioRequired(envio, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("peso total")));
        }

        @Test
        public void testEnvioSinModalidad() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .pesoTotal(BigDecimal.ONE)
                    .fechaTraslado(LocalDate.now())
                    .build();
            DespatchAdviceCommonValidator.validateEnvioRequired(envio, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("modalidad de traslado")));
        }

        @Test
        public void testEnvioSinFecha() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .pesoTotal(BigDecimal.ONE)
                    .tipoModalidadTraslado("02")
                    .build();
            DespatchAdviceCommonValidator.validateEnvioRequired(envio, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("fecha de traslado")));
        }

        @Test
        public void testEnvioCompletoValido() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateEnvioRequired(minimalEnvio(), msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // Partida / Destino
    // ================================================================

    @Nested
    class PartidaDestinoTests {

        @Test
        public void testSinPartida() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                    .build();
            DespatchAdviceCommonValidator.validatePartidaDestino(envio, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("partida")));
        }

        @Test
        public void testSinDestino() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                    .build();
            DespatchAdviceCommonValidator.validatePartidaDestino(envio, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("destino")));
        }

        @Test
        public void testPartidaDestinoPresentes() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validatePartidaDestino(minimalEnvio(), msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testEnvioNullNoFalla() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validatePartidaDestino(null, msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // Modalidad Remitente
    // ================================================================

    @Nested
    class ModalidadRemitenteTests {

        @Test
        public void testPrivadoSinConductorNiVehiculo() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .pesoTotal(BigDecimal.ONE)
                    .tipoModalidadTraslado("02")
                    .fechaTraslado(LocalDate.now())
                    .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                    .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                    .build();
            DespatchAdviceCommonValidator.validateModalidadRemitente(envio, msgs);
            assertEquals(2, msgs.stream().filter(ValidationMessage::isError).count());
            assertTrue(msgs.stream().anyMatch(m -> m.getMessage().contains("conductor")));
            assertTrue(msgs.stream().anyMatch(m -> m.getMessage().contains("vehículo")));
        }

        @Test
        public void testPrivadoConIndicadorM1L() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .pesoTotal(BigDecimal.ONE)
                    .tipoModalidadTraslado("02")
                    .fechaTraslado(LocalDate.now())
                    .indicador("SUNAT_Envio_IndicadorTrasladoVehiculoM1L")
                    .build();
            DespatchAdviceCommonValidator.validateModalidadRemitente(envio, msgs);
            assertTrue(msgs.isEmpty(), "M1/L exime conductor y vehículo");
        }

        @Test
        public void testPrivadoConTransportista() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .pesoTotal(BigDecimal.ONE)
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
                    .transportista(Transportista.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20300030003")
                            .nombre("T")
                            .build())
                    .build();
            DespatchAdviceCommonValidator.validateModalidadRemitente(envio, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("Transporte privado no debe consignar transportista")));
        }

        @Test
        public void testPublicoSinTransportista() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .pesoTotal(BigDecimal.ONE)
                    .tipoModalidadTraslado("01")
                    .fechaTraslado(LocalDate.now())
                    .build();
            DespatchAdviceCommonValidator.validateModalidadRemitente(envio, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isError()
                            && m.getMessage().contains("transportista")));
        }

        @Test
        public void testPublicoConTransportista() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("01")
                    .pesoTotal(BigDecimal.ONE)
                    .tipoModalidadTraslado("01")
                    .fechaTraslado(LocalDate.now())
                    .transportista(Transportista.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20300030003")
                            .nombre("T")
                            .build())
                    .build();
            DespatchAdviceCommonValidator.validateModalidadRemitente(envio, msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testModalidadNullNoFalla() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder().tipoTraslado("01").build();
            DespatchAdviceCommonValidator.validateModalidadRemitente(envio, msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // Conductor/Vehículo Transportista
    // ================================================================

    @Nested
    class ConductorVehiculoTransportistaTests {

        @Test
        public void testSinConductorNiVehiculo() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateConductorVehiculoTransportista(null, null, msgs);
            assertEquals(2, msgs.stream().filter(ValidationMessage::isError).count());
        }

        @Test
        public void testConductorVacio() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateConductorVehiculoTransportista(
                    new ArrayList<>(), Vehicle.builder().placa("X").build(), msgs);
            assertEquals(1, msgs.stream().filter(ValidationMessage::isError).count());
            assertTrue(msgs.stream().anyMatch(m -> m.getMessage().contains("conductor")));
        }

        @Test
        public void testVehiculoNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateConductorVehiculoTransportista(
                    List.of(Driver.builder().build()), null, msgs);
            assertEquals(1, msgs.stream().filter(ValidationMessage::isError).count());
            assertTrue(msgs.stream().anyMatch(m -> m.getMessage().contains("vehículo")));
        }

        @Test
        public void testConductorYVehiculoPresentes() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateConductorVehiculoTransportista(
                    List.of(Driver.builder().build()),
                    Vehicle.builder().placa("X").build(), msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // Comercio Exterior (WARNINGS)
    // ================================================================

    @Nested
    class ComercioExteriorTests {

        @Test
        public void testImportacionSinDAM() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder().tipoTraslado("08").build();
            DespatchAdviceCommonValidator.validateComercioExterior(envio, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isWarning() && m.getMessage().contains("DAM/DS")),
                    "Debe generar warning por falta de DAM/DS");
        }

        @Test
        public void testExportacionSinPuerto() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder().tipoTraslado("09").build();
            DespatchAdviceCommonValidator.validateComercioExterior(envio, msgs);
            assertTrue(msgs.stream()
                    .anyMatch(m -> m.isWarning()
                            && m.getMessage().contains("puerto o aeropuerto")));
        }

        @Test
        public void testMotivoVentaSinAdvertencias() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado(Catalog20.VENTA.getCode())
                    .build();
            DespatchAdviceCommonValidator.validateComercioExterior(envio, msgs);
            assertTrue(msgs.isEmpty(), "Venta no requiere reglas de comercio exterior");
        }

        @Test
        public void testComercioExteriorConDAMYPuerto() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("08")
                    .declaracionAduanera(DeclaracionAduanera.builder()
                            .tipo("DAM")
                            .numero("118-2024-10-001")
                            .build())
                    .puerto(Puerto.builder().codigo("PECLL").nombre("Callao").build())
                    .build();
            DespatchAdviceCommonValidator.validateComercioExterior(envio, msgs);
            assertTrue(msgs.isEmpty(), "Con DAM y puerto no debe haber warnings");
        }

        @Test
        public void testComercioExteriorConAeropuerto() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder()
                    .tipoTraslado("09")
                    .declaracionAduanera(DeclaracionAduanera.builder()
                            .tipo("DAM")
                            .numero("118-2024-10-001")
                            .build())
                    .aeropuerto(Puerto.builder().codigo("SPJC").nombre("Jorge Chávez").build())
                    .build();
            DespatchAdviceCommonValidator.validateComercioExterior(envio, msgs);
            assertTrue(msgs.isEmpty(), "Aeropuerto satisface el requisito de puerto");
        }

        @Test
        public void testEnvioNullNoFalla() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateComercioExterior(null, msgs);
            assertTrue(msgs.isEmpty());
        }

        @Test
        public void testSeveridadEsWarningNoError() {
            List<ValidationMessage> msgs = new ArrayList<>();
            Envio envio = Envio.builder().tipoTraslado("10").build();
            DespatchAdviceCommonValidator.validateComercioExterior(envio, msgs);
            assertTrue(msgs.stream().allMatch(ValidationMessage::isWarning),
                    "Todas las reglas de comercio exterior deben ser WARNING, no ERROR");
            assertTrue(msgs.stream().noneMatch(ValidationMessage::isError));
        }
    }

    // ================================================================
    // Detalles
    // ================================================================

    @Nested
    class DetallesTests {

        @Test
        public void testDetallesNull() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateDetalles(null, msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("detalle")));
        }

        @Test
        public void testDetallesVacios() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateDetalles(new ArrayList<>(), msgs);
            assertTrue(msgs.stream().anyMatch(m -> m.isError() && m.getMessage().contains("detalle")));
        }

        @Test
        public void testDetallesConItems() {
            List<ValidationMessage> msgs = new ArrayList<>();
            DespatchAdviceCommonValidator.validateDetalles(
                    List.of(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE)
                            .unidadMedida("NIU")
                            .codigo("001")
                            .build()),
                    msgs);
            assertTrue(msgs.isEmpty());
        }
    }

    // ================================================================
    // validateDetailed integration via DespatchAdviceValidator
    // ================================================================

    @Nested
    class ValidateDetailedIntegration {

        @Test
        public void testValidateDetailedReturnsValidResult() {
            DespatchAdvice da = DespatchAdvice.builder()
                    .serie("T001")
                    .numero(1)
                    .tipoComprobante("09")
                    .remitente(Remitente.builder().ruc("12345678901").razonSocial("Test").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1")
                            .numeroDocumentoIdentidad("12345678")
                            .nombre("C")
                            .build())
                    .envio(minimalEnvio())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE)
                            .unidadMedida("NIU")
                            .codigo("001")
                            .build())
                    .build();

            ValidationResult result = DespatchAdviceValidator.validateDetailed(da);
            assertTrue(result.isValid(), "Debe ser válido: " + result.getErrors());
            assertFalse(result.hasErrors());
        }

        @Test
        public void testValidateDetailedReturnsWarningsForComercioExterior() {
            DespatchAdvice da = DespatchAdvice.builder()
                    .serie("T001")
                    .numero(1)
                    .tipoComprobante("09")
                    .remitente(Remitente.builder().ruc("12345678901").razonSocial("Test").build())
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

            ValidationResult result = DespatchAdviceValidator.validateDetailed(da);
            assertTrue(result.isValid(), "No debe tener errores: " + result.getErrors());
            assertTrue(result.hasWarnings(), "Debe tener warnings de comercio exterior");
            assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("DAM/DS")));
        }

        @Test
        public void testValidateReturnsOnlyErrors() {
            DespatchAdvice da = DespatchAdvice.builder()
                    .serie("T001")
                    .numero(1)
                    .tipoComprobante("09")
                    .remitente(Remitente.builder().ruc("12345678901").razonSocial("Test").build())
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

            List<String> errors = DespatchAdviceValidator.validate(da);
            assertTrue(errors.isEmpty(),
                    "validate() must not include warnings: " + errors);
        }
    }

    // ================================================================
    // GRERemitente.validateDetailed()
    // ================================================================

    @Nested
    class GRERemitenteValidateDetailedTests {

        @Test
        public void testValidateDetailedValid() {
            GRERemitente gre = GRERemitente.builder()
                    .serie("T001")
                    .numero(1)
                    .remitente(Remitente.builder().ruc("12345678901").razonSocial("Test").build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("1")
                            .numeroDocumentoIdentidad("12345678")
                            .nombre("C")
                            .build())
                    .envio(minimalEnvio())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE)
                            .unidadMedida("NIU")
                            .codigo("001")
                            .build())
                    .build();

            ValidationResult result = gre.validateDetailed();
            assertTrue(result.isValid(), "GRERemitente válido: " + result.getErrors());
        }

        @Test
        public void testValidateDetailedWithImportWarnings() {
            GRERemitente gre = GRERemitente.builder()
                    .serie("T001")
                    .numero(1)
                    .remitente(Remitente.builder().ruc("12345678901").razonSocial("Test").build())
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

            ValidationResult result = gre.validateDetailed();
            assertTrue(result.isValid(), "No errors: " + result.getErrors());
            assertTrue(result.hasWarnings());
        }
    }

    // ================================================================
    // GRETransportista.validateDetailed()
    // ================================================================

    @Nested
    class GRETransportistaValidateDetailedTests {

        @Test
        public void testValidateDetailedValid() {
            GRETransportista gre = GRETransportista.builder()
                    .serie("V001")
                    .numero(1)
                    .transportistaEmisor(Transportista.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20300030003")
                            .nombre("Transportes")
                            .build())
                    .remitente(Tercero.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20100010001")
                            .nombre("R")
                            .build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20200020002")
                            .nombre("D")
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
                            .tipoTraslado("01")
                            .pesoTotal(BigDecimal.ONE)
                            .pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("01")
                            .fechaTraslado(LocalDate.now())
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE)
                            .unidadMedida("NIU")
                            .codigo("001")
                            .build())
                    .build();

            ValidationResult result = gre.validateDetailed();
            assertTrue(result.isValid(), "Válido: " + result.getErrors());
        }

        @Test
        public void testValidateDetailedSinConductorEsError() {
            GRETransportista gre = GRETransportista.builder()
                    .serie("V001")
                    .numero(1)
                    .transportistaEmisor(Transportista.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20300030003")
                            .nombre("T")
                            .build())
                    .remitente(Tercero.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20100010001")
                            .nombre("R")
                            .build())
                    .destinatario(Destinatario.builder()
                            .tipoDocumentoIdentidad("6")
                            .numeroDocumentoIdentidad("20200020002")
                            .nombre("D")
                            .build())
                    // Sin conductor
                    .vehiculo(Vehicle.builder().placa("XYZ-789").build())
                    .envio(Envio.builder()
                            .tipoTraslado("01")
                            .pesoTotal(BigDecimal.ONE)
                            .pesoTotalUnidadMedida("KGM")
                            .tipoModalidadTraslado("01")
                            .fechaTraslado(LocalDate.now())
                            .partida(Partida.builder().ubigeo("010101").direccion("O").build())
                            .destino(Destino.builder().ubigeo("020202").direccion("D").build())
                            .build())
                    .detalle(DespatchAdviceItem.builder()
                            .cantidad(BigDecimal.ONE)
                            .unidadMedida("NIU")
                            .codigo("001")
                            .build())
                    .build();

            ValidationResult result = gre.validateDetailed();
            assertFalse(result.isValid());
            assertTrue(result.getErrors().stream().anyMatch(e -> e.contains("conductor")));
        }
    }
}
