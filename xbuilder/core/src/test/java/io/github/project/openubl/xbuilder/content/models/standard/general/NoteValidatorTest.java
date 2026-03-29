package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de validación para Notas de Crédito y Notas de Débito.
 */
@DisplayName("NoteValidator")
class NoteValidatorTest {

    private static Proveedor proveedor() {
        return Proveedor.builder()
                .ruc("20601234567")
                .razonSocial("EMPRESA SAC")
                .build();
    }

    private static Cliente cliente() {
        return Cliente.builder()
                .nombre("CLIENTE SAC")
                .numeroDocumentoIdentidad("20100047218")
                .tipoDocumentoIdentidad("6")
                .build();
    }

    private static DocumentoVentaDetalle linea() {
        return DocumentoVentaDetalle.builder()
                .descripcion("Producto devuelto")
                .cantidad(BigDecimal.ONE)
                .precio(BigDecimal.valueOf(100))
                .build();
    }

    @Nested
    @DisplayName("Nota de Crédito - casos válidos")
    class CreditNoteValidTests {

        @Test
        @DisplayName("NC estándar por anulación total")
        void ncAnulacionTotal() {
            CreditNote nc = CreditNote.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedor())
                    .firmante(Firmante.builder().ruc("20601234567").razonSocial("EMPRESA SAC").build())
                    .cliente(cliente())
                    .tipoNota("01")
                    .comprobanteAfectadoSerieNumero("F001-100")
                    .comprobanteAfectadoTipo("01")
                    .sustentoDescripcion("Anulación de la operación")
                    .detalle(linea())
                    .build();
            List<String> errors = NoteValidator.validate(nc);
            assertTrue(errors.isEmpty(), "NC válida: " + errors);
        }

        @Test
        @DisplayName("NC por corrección de nombre/razón social (motivo 10)")
        void ncCorreccionNombre() {
            CreditNote nc = CreditNote.builder()
                    .serie("B001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedor())
                    .firmante(Firmante.builder().ruc("20601234567").razonSocial("EMPRESA SAC").build())
                    .cliente(cliente())
                    .tipoNota("10")
                    .comprobanteAfectadoSerieNumero("B001-50")
                    .comprobanteAfectadoTipo("03")
                    .sustentoDescripcion("Corrección de denominación del receptor")
                    .detalle(linea())
                    .build();
            List<String> errors = NoteValidator.validate(nc);
            assertTrue(errors.isEmpty(), "NC motivo 10 válida: " + errors);
        }
    }

    @Nested
    @DisplayName("Nota de Débito - casos válidos")
    class DebitNoteValidTests {

        @Test
        @DisplayName("ND por intereses por mora (motivo 01)")
        void ndInteresesMora() {
            DebitNote nd = DebitNote.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedor())
                    .firmante(Firmante.builder().ruc("20601234567").razonSocial("EMPRESA SAC").build())
                    .cliente(cliente())
                    .tipoNota("01")
                    .comprobanteAfectadoSerieNumero("F001-100")
                    .comprobanteAfectadoTipo("01")
                    .sustentoDescripcion("Intereses por mora en pago")
                    .detalle(DocumentoVentaDetalle.builder()
                            .descripcion("Intereses por mora")
                            .cantidad(BigDecimal.ONE)
                            .precio(BigDecimal.valueOf(250))
                            .build())
                    .build();
            List<String> errors = NoteValidator.validate(nd);
            assertTrue(errors.isEmpty(), "ND válida: " + errors);
        }
    }

    @Nested
    @DisplayName("Nota - casos inválidos")
    class InvalidTests {

        @Test
        @DisplayName("NC sin comprobante afectado")
        void ncSinComprobanteAfectado() {
            CreditNote nc = CreditNote.builder()
                    .serie("F001")
                    .numero(1)
                    .proveedor(proveedor())
                    .cliente(cliente())
                    .tipoNota("01")
                    .sustentoDescripcion("Anulación")
                    .detalle(linea())
                    .build();
            List<String> errors = NoteValidator.validate(nc);
            assertTrue(errors.stream().anyMatch(e -> e.contains("comprobanteAfectadoSerieNumero")),
                    "Debe requerir comprobante afectado");
        }

        @Test
        @DisplayName("NC sin sustento")
        void ncSinSustento() {
            CreditNote nc = CreditNote.builder()
                    .serie("F001")
                    .numero(1)
                    .proveedor(proveedor())
                    .cliente(cliente())
                    .tipoNota("01")
                    .comprobanteAfectadoSerieNumero("F001-1")
                    .comprobanteAfectadoTipo("01")
                    .detalle(linea())
                    .build();
            List<String> errors = NoteValidator.validate(nc);
            assertTrue(errors.stream().anyMatch(e -> e.contains("sustento")),
                    "Debe requerir sustento descriptivo");
        }

        @Test
        @DisplayName("NC con tipo nota inválido")
        void ncTipoNotaInvalido() {
            CreditNote nc = CreditNote.builder()
                    .serie("F001")
                    .numero(1)
                    .proveedor(proveedor())
                    .cliente(cliente())
                    .tipoNota("99")
                    .comprobanteAfectadoSerieNumero("F001-1")
                    .comprobanteAfectadoTipo("01")
                    .sustentoDescripcion("Test")
                    .detalle(linea())
                    .build();
            List<String> errors = NoteValidator.validate(nc);
            assertTrue(errors.stream().anyMatch(e -> e.contains("Catálogo 09")),
                    "Debe validar tipo nota contra Catálogo 09");
        }

        @Test
        @DisplayName("ND con tipo nota inválido")
        void ndTipoNotaInvalido() {
            DebitNote nd = DebitNote.builder()
                    .serie("F001")
                    .numero(1)
                    .proveedor(proveedor())
                    .cliente(cliente())
                    .tipoNota("99")
                    .comprobanteAfectadoSerieNumero("F001-1")
                    .comprobanteAfectadoTipo("01")
                    .sustentoDescripcion("Test")
                    .detalle(linea())
                    .build();
            List<String> errors = NoteValidator.validate(nd);
            assertTrue(errors.stream().anyMatch(e -> e.contains("Catálogo 10")),
                    "Debe validar tipo nota contra Catálogo 10");
        }
    }
}
