package io.github.project.openubl.xbuilder.content.models.standard.general;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog51;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de validación para Factura Electrónica y Boleta de Venta Electrónica.
 * <p>
 * Casos de prueba basados en:
 * <ul>
 * <li>Guía XML Factura 2.1 – SUNAT</li>
 * <li>Guía XML Boleta 2.1 – SUNAT</li>
 * <li>Reglas de Validación CPE vigentes</li>
 * </ul>
 */
@DisplayName("InvoiceValidator")
class InvoiceValidatorTest {

    // ── Fixtures de datos reales ─────────────────────────────────

    private static Proveedor proveedorValido() {
        return Proveedor.builder()
                .ruc("20601234567")
                .razonSocial("EMPRESA DE PRUEBA SAC")
                .build();
    }

    private static Firmante firmanteValido() {
        return Firmante.builder()
                .ruc("20601234567")
                .razonSocial("EMPRESA DE PRUEBA SAC")
                .build();
    }

    private static Cliente clienteRuc() {
        return Cliente.builder()
                .nombre("ACME CORPORATION SAC")
                .numeroDocumentoIdentidad("20100047218")
                .tipoDocumentoIdentidad("6")
                .build();
    }

    private static Cliente clienteDni() {
        return Cliente.builder()
                .nombre("JUAN PEREZ GARCIA")
                .numeroDocumentoIdentidad("44556677")
                .tipoDocumentoIdentidad("1")
                .build();
    }

    private static DocumentoVentaDetalle lineaGravada() {
        return DocumentoVentaDetalle.builder()
                .descripcion("Laptop HP ProBook 450 G8")
                .cantidad(BigDecimal.valueOf(2))
                .precio(BigDecimal.valueOf(3500.00))
                .codigoProducto("LAPTOP-HP-450")
                .codigoProductoSunat("43211507")
                .igvTipo("10")
                .build();
    }

    private static DocumentoVentaDetalle lineaExonerada() {
        return DocumentoVentaDetalle.builder()
                .descripcion("Consulta médica general")
                .cantidad(BigDecimal.ONE)
                .precio(BigDecimal.valueOf(150.00))
                .igvTipo("20")
                .build();
    }

    private static DocumentoVentaDetalle lineaGratuita() {
        return DocumentoVentaDetalle.builder()
                .descripcion("Muestra gratis - Producto promocional")
                .cantidad(BigDecimal.ONE)
                .precio(BigDecimal.ZERO)
                .igvTipo("31")
                .build();
    }

    private static DocumentoVentaDetalle lineaConIcbper() {
        return DocumentoVentaDetalle.builder()
                .descripcion("Bolsa plástica")
                .cantidad(BigDecimal.valueOf(3))
                .precio(BigDecimal.valueOf(0.50))
                .igvTipo("10")
                .icbAplica(true)
                .build();
    }

    private static Invoice facturaBase() {
        return Invoice.builder()
                .serie("F001")
                .numero(1)
                .fechaEmision(LocalDate.now())
                .moneda("PEN")
                .proveedor(proveedorValido())
                .firmante(firmanteValido())
                .cliente(clienteRuc())
                .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                .detalle(lineaGravada())
                .build();
    }

    private static Invoice boletaBase() {
        return Invoice.builder()
                .serie("B001")
                .numero(1)
                .fechaEmision(LocalDate.now())
                .moneda("PEN")
                .proveedor(proveedorValido())
                .firmante(firmanteValido())
                .cliente(clienteDni())
                .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                .detalle(lineaGravada())
                .build();
    }

    // ══════════════════════════════════════════════════════════════
    // Factura Electrónica (01) — Casos válidos
    // ══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Factura válida - casos comunes")
    class FacturaValidaTests {

        @Test
        @DisplayName("Factura gravada estándar - venta interna con IGV")
        void facturaGravadaEstandar() {
            Invoice invoice = facturaBase();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.isEmpty(), "Factura gravada estándar debe ser válida: " + errors);
        }

        @Test
        @DisplayName("Factura con operación exonerada (IGV tipo 20)")
        void facturaExonerada() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(clienteRuc())
                    .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                    .detalle(lineaExonerada())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.isEmpty(), "Factura exonerada debe ser válida: " + errors);
        }

        @Test
        @DisplayName("Factura con líneas mixtas (gravada + exonerada + ICBPER)")
        void facturaMixta() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(clienteRuc())
                    .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                    .detalles(List.of(lineaGravada(), lineaExonerada(), lineaConIcbper()))
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.isEmpty(), "Factura mixta debe ser válida: " + errors);
        }

        @Test
        @DisplayName("Factura con detracción (tipo operación 1001)")
        void facturaConDetraccion() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(clienteRuc())
                    .tipoOperacion(Catalog51.OPERACION_SUJETA_A_DETRACCION.getCode())
                    .detalle(lineaGravada())
                    .detraccion(Detraccion.builder()
                            .medioDePago("001")
                            .cuentaBancaria("00-123-456789")
                            .tipoBienDetraido("004")
                            .porcentaje(BigDecimal.valueOf(0.08))
                            .monto(BigDecimal.valueOf(500))
                            .build())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.isEmpty(), "Factura con detracción debe ser válida: " + errors);
        }

        @Test
        @DisplayName("Factura con percepción (tipo operación 2001)")
        void facturaConPercepcion() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(clienteRuc())
                    .tipoOperacion(Catalog51.OPERACION_SUJETA_A_PERCEPCION.getCode())
                    .detalle(lineaGravada())
                    .percepcion(Percepcion.builder()
                            .tipo("51")
                            .porcentaje(BigDecimal.valueOf(0.02))
                            .build())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.isEmpty(), "Factura con percepción debe ser válida: " + errors);
        }

        @Test
        @DisplayName("Factura al crédito con cuotas")
        void facturaCredito() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(clienteRuc())
                    .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                    .detalle(lineaGravada())
                    .formaDePago(FormaDePago.builder()
                            .tipo("Credito")
                            .total(BigDecimal.valueOf(4130.00))
                            .cuota(CuotaDePago.builder()
                                    .importe(BigDecimal.valueOf(2065.00))
                                    .fechaPago(LocalDate.now().plusDays(30))
                                    .build())
                            .cuota(CuotaDePago.builder()
                                    .importe(BigDecimal.valueOf(2065.00))
                                    .fechaPago(LocalDate.now().plusDays(60))
                                    .build())
                            .build())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.isEmpty(), "Factura al crédito debe ser válida: " + errors);
        }

        @Test
        @DisplayName("Factura con transferencia gratuita (IGV tipo 31)")
        void facturaGratuita() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(clienteRuc())
                    .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                    .detalle(lineaGratuita())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.isEmpty(), "Factura gratuita debe ser válida: " + errors);
        }
    }

    // ══════════════════════════════════════════════════════════════
    // Boleta de Venta (03) — Casos válidos
    // ══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Boleta válida - casos comunes")
    class BoletaValidaTests {

        @Test
        @DisplayName("Boleta estándar con DNI")
        void boletaEstandarConDni() {
            Invoice boleta = boletaBase();
            List<String> errors = InvoiceValidator.validate(boleta);
            assertTrue(errors.isEmpty(), "Boleta con DNI debe ser válida: " + errors);
        }

        @Test
        @DisplayName("Boleta sin documento de identidad (monto bajo)")
        void boletaSinDocumento() {
            Invoice boleta = Invoice.builder()
                    .serie("B001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(Cliente.builder().nombre("CONSUMIDOR FINAL").build())
                    .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                    .detalle(lineaGravada())
                    .build();
            ValidationResult result = InvoiceValidator.validateDetailed(boleta);
            assertTrue(result.getErrors().isEmpty(),
                    "Boleta sin documento para montos bajos no debe tener errores: " + result.getErrors());
        }

        @Test
        @DisplayName("Boleta con ICBPER (bolsas de plástico)")
        void boletaConIcbper() {
            Invoice boleta = Invoice.builder()
                    .serie("B001")
                    .numero(1)
                    .fechaEmision(LocalDate.now())
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .firmante(firmanteValido())
                    .cliente(clienteDni())
                    .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                    .detalles(List.of(lineaGravada(), lineaConIcbper()))
                    .build();
            List<String> errors = InvoiceValidator.validate(boleta);
            assertTrue(errors.isEmpty(), "Boleta con ICBPER debe ser válida: " + errors);
        }
    }

    // ══════════════════════════════════════════════════════════════
    // Casos inválidos — Errores de validación
    // ══════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("Casos inválidos")
    class CasosInvalidosTests {

        @Test
        @DisplayName("Serie incorrecta para factura (B en lugar de F)")
        void serieIncorrectaFactura() {
            Invoice invoice = Invoice.builder()
                    .serie("B001")
                    .numero(1)
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .cliente(clienteRuc())
                    .tipoComprobante("01")
                    .tipoOperacion(Catalog51.VENTA_INTERNA.getCode())
                    .detalle(lineaGravada())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertFalse(errors.isEmpty(), "Factura con serie B debe ser inválida");
            assertTrue(errors.stream().anyMatch(e -> e.contains("serie")),
                    "Debe mencionar error de serie");
        }

        @Test
        @DisplayName("Factura sin cliente")
        void facturaSinCliente() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .detalle(lineaGravada())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.stream().anyMatch(e -> e.contains("cliente")),
                    "Debe requerir cliente");
        }

        @Test
        @DisplayName("Factura sin detalles")
        void facturaSinDetalles() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .cliente(clienteRuc())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.stream().anyMatch(e -> e.contains("detalle")),
                    "Debe requerir al menos una línea");
        }

        @Test
        @DisplayName("Detracción en boleta debe ser error")
        void detraccionEnBoleta() {
            Invoice boleta = Invoice.builder()
                    .serie("B001")
                    .numero(1)
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .cliente(clienteDni())
                    .tipoComprobante("03")
                    .tipoOperacion("1001")
                    .detalle(lineaGravada())
                    .detraccion(Detraccion.builder()
                            .medioDePago("001")
                            .cuentaBancaria("00-123-456789")
                            .tipoBienDetraido("004")
                            .porcentaje(BigDecimal.valueOf(0.08))
                            .monto(BigDecimal.valueOf(100))
                            .build())
                    .build();
            List<String> errors = InvoiceValidator.validate(boleta);
            assertTrue(
                    errors.stream()
                            .anyMatch(e -> e.toLowerCase().contains("detracción")
                                    || e.toLowerCase().contains("detraccion")),
                    "Detracción en boleta debe ser error");
        }

        @Test
        @DisplayName("Detracción sin cuenta bancaria")
        void detraccionSinCuenta() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .cliente(clienteRuc())
                    .tipoOperacion("1001")
                    .detalle(lineaGravada())
                    .detraccion(Detraccion.builder()
                            .medioDePago("001")
                            .tipoBienDetraido("004")
                            .porcentaje(BigDecimal.valueOf(0.08))
                            .build())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.stream().anyMatch(e -> e.contains("cuentaBancaria")),
                    "Debe requerir cuenta bancaria");
        }

        @Test
        @DisplayName("Línea sin descripción")
        void lineaSinDescripcion() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .moneda("PEN")
                    .proveedor(proveedorValido())
                    .cliente(clienteRuc())
                    .detalle(DocumentoVentaDetalle.builder()
                            .cantidad(BigDecimal.ONE)
                            .precio(BigDecimal.TEN)
                            .build())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.stream().anyMatch(e -> e.contains("descripción")),
                    "Debe requerir descripción en línea");
        }

        @Test
        @DisplayName("RUC del proveedor inválido")
        void rucProveedorInvalido() {
            Invoice invoice = Invoice.builder()
                    .serie("F001")
                    .numero(1)
                    .moneda("PEN")
                    .proveedor(Proveedor.builder().ruc("12345").razonSocial("TEST").build())
                    .cliente(clienteRuc())
                    .detalle(lineaGravada())
                    .build();
            List<String> errors = InvoiceValidator.validate(invoice);
            assertTrue(errors.stream().anyMatch(e -> e.contains("11 dígitos")),
                    "Debe validar longitud de RUC");
        }
    }
}
