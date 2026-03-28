package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo para Guía de Remisión Electrónica - Remitente (tipo 09).
 * <p>
 * Emitida por el remitente de los bienes. Serie TXXX.
 * <p>
 * Reglas según RS 000123-2022/SUNAT:
 * <ul>
 * <li>Serie debe iniciar con 'T'</li>
 * <li>Transporte privado (02): requiere conductor y vehículo</li>
 * <li>Transporte público (01): requiere datos del transportista</li>
 * <li>El remitente es quien envía los bienes (DespatchSupplierParty)</li>
 * </ul>
 * <p>
 * Uso:
 * 
 * <pre>{@code
 * GRERemitente gre = GRERemitente.builder()
 *         .serie("T001").numero(1)
 *         .remitente(Remitente.builder().ruc("20100010001").razonSocial("Mi Empresa").build())
 *         .destinatario(Destinatario.builder()
 *                 .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20200020002").nombre("Cliente").build())
 *         .envio(Envio.builder()
 *                 .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
 *                 .tipoModalidadTraslado("02").fechaTraslado(LocalDate.now())
 *                 .chofer(Driver.builder().tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678")
 *                         .nombres("Juan").apellidos("Perez").licencia("Q123").build())
 *                 .vehiculo(Vehicle.builder().placa("ABC-123").build())
 *                 .partida(Partida.builder().ubigeo("150101").direccion("Origen").build())
 *                 .destino(Destino.builder().ubigeo("150102").direccion("Destino").build())
 *                 .build())
 *         .detalle(DespatchAdviceItem.builder().cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
 *         .build();
 *
 * DespatchAdvice da = gre.toDespatchAdvice();
 * }</pre>
 *
 * @see GRETransportista
 * @see DespatchAdvice
 */
@Data
@Builder
public class GRERemitente {

    // == Datos del comprobante ==

    private String serie;
    private Integer numero;
    private String version;
    private LocalDate fechaEmision;
    private LocalTime horaEmision;
    private String observaciones;

    // == Partes ==

    private Remitente remitente;
    private Destinatario destinatario;
    private Comprador comprador;
    private Firmante firmante;

    // == Documentos relacionados ==

    private DocumentoBaja documentoBaja;
    private DocumentoRelacionado documentoRelacionado;

    @Singular("documentoRelacionadoAdicional")
    private List<DocumentoRelacionado> documentosRelacionados;

    @Singular("documentoAdicional")
    private List<DocumentoAdicional> documentosAdicionales;

    // == Datos de envío ==

    private Envio envio;

    // == Detalle de bienes ==

    @Singular
    private List<DespatchAdviceItem> detalles;

    /**
     * Valida las reglas de negocio para GRE-Remitente y retorna los errores.
     *
     * @return lista de errores (vacía si es válido)
     */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        // Serie
        if (serie == null || serie.isBlank()) {
            errors.add("La serie es requerida");
        } else if (!serie.toUpperCase().startsWith("T")) {
            errors.add("GRE-Remitente requiere serie que inicie con 'T'. Serie actual: " + serie);
        }

        // Número
        if (numero == null || numero < 1) {
            errors.add("El número debe ser mayor a 0");
        }

        // Remitente
        if (remitente == null) {
            errors.add("El remitente es requerido");
        } else if (remitente.getRuc() == null || remitente.getRuc().length() != 11) {
            errors.add("El RUC del remitente debe tener 11 dígitos");
        }

        // Destinatario
        if (destinatario == null) {
            errors.add("El destinatario es requerido");
        }

        // Envío
        if (envio == null) {
            errors.add("Los datos de envío son requeridos");
        } else {
            validateEnvio(envio, errors);
        }

        // Detalles
        if (detalles == null || detalles.isEmpty()) {
            errors.add("Se requiere al menos una línea de detalle");
        }

        return errors;
    }

    private void validateEnvio(Envio envio, List<String> errors) {
        if (envio.getTipoTraslado() == null || envio.getTipoTraslado().isBlank()) {
            errors.add("El motivo de traslado (Catálogo 20) es requerido");
        }
        if (envio.getPesoTotal() == null) {
            errors.add("El peso total es requerido");
        }
        if (envio.getTipoModalidadTraslado() == null) {
            errors.add("La modalidad de traslado (Catálogo 18) es requerida");
        }
        if (envio.getFechaTraslado() == null) {
            errors.add("La fecha de traslado es requerida");
        }
        if (envio.getPartida() == null) {
            errors.add("El punto de partida es requerido");
        }
        if (envio.getDestino() == null) {
            errors.add("El punto de destino es requerido");
        }

        String modalidad = envio.getTipoModalidadTraslado();
        if (modalidad == null)
            return;

        boolean tieneIndicadorM1L = envio.getIndicadores() != null &&
                envio.getIndicadores().contains("SUNAT_Envio_IndicadorTrasladoVehiculoM1L");

        if ("02".equals(modalidad)) {
            // Transporte privado: requiere conductor y vehículo
            if (!tieneIndicadorM1L) {
                if (envio.getChoferes() == null || envio.getChoferes().isEmpty()) {
                    errors.add("Transporte privado requiere al menos un conductor (salvo vehículo categoría M1/L)");
                }
                if (envio.getVehiculo() == null) {
                    errors.add("Transporte privado requiere datos del vehículo (salvo vehículo categoría M1/L)");
                }
            }
            // Transporte privado NO debe tener transportista externo
            if (envio.getTransportista() != null) {
                errors.add(
                        "Transporte privado no debe consignar transportista externo (usar modalidad pública si subcontrata)");
            }
        } else if ("01".equals(modalidad)) {
            // Transporte público: requiere transportista
            if (envio.getTransportista() == null) {
                errors.add("Transporte público requiere datos del transportista");
            }
        }
    }

    /**
     * Convierte este modelo a {@link DespatchAdvice} para renderizado XML.
     * El tipo de comprobante se fija a "09" (GRE-Remitente).
     *
     * @return DespatchAdvice listo para enriquecer y renderizar
     */
    public DespatchAdvice toDespatchAdvice() {
        DespatchAdvice.DespatchAdviceBuilder builder = DespatchAdvice.builder()
                .serie(serie)
                .numero(numero)
                .version(version)
                .fechaEmision(fechaEmision)
                .horaEmision(horaEmision)
                .tipoComprobante("09")
                .observaciones(observaciones)
                .remitente(remitente)
                .destinatario(destinatario)
                .comprador(comprador)
                .firmante(firmante)
                .documentoBaja(documentoBaja)
                .documentoRelacionado(documentoRelacionado)
                .envio(envio);

        if (documentosRelacionados != null) {
            documentosRelacionados.forEach(builder::documentoRelacionadoAdicional);
        }
        if (documentosAdicionales != null) {
            documentosAdicionales.forEach(builder::documentoAdicional);
        }
        if (detalles != null) {
            detalles.forEach(builder::detalle);
        }

        return builder.build();
    }

    /**
     * Valida y convierte a DespatchAdvice.
     *
     * @return DespatchAdvice validado
     * @throws IllegalStateException si hay errores de validación
     */
    public DespatchAdvice toDespatchAdviceValidated() {
        List<String> errors = validate();
        if (!errors.isEmpty()) {
            throw new IllegalStateException(
                    "GRE-Remitente inválido:\n- " + String.join("\n- ", errors));
        }
        return toDespatchAdvice();
    }
}
