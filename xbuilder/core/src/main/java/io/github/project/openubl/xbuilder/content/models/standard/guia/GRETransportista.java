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
 * Modelo para Guía de Remisión Electrónica - Transportista (tipo 31).
 * <p>
 * Emitida por el transportista que presta el servicio. Serie VXXX.
 * <p>
 * Reglas según RS 000123-2022/SUNAT:
 * <ul>
 * <li>Serie debe iniciar con 'V'</li>
 * <li>Siempre requiere al menos un conductor</li>
 * <li>Siempre requiere vehículo</li>
 * <li>El remitente/emisor del XML es el transportista
 * (DespatchSupplierParty)</li>
 * <li>El tercero (SellerSupplierParty) es el remitente original de los
 * bienes</li>
 * </ul>
 * <p>
 * Uso:
 * 
 * <pre>{@code
 * GRETransportista gre = GRETransportista.builder()
 *         .serie("V001").numero(1)
 *         .transportistaEmisor(Transportista.builder()
 *                 .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20300030003")
 *                 .nombre("Transportes S.A.C.").numeroRegistroMTC("MTC-123").build())
 *         .remitente(Tercero.builder()
 *                 .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20100010001")
 *                 .nombre("Empresa Remitente S.A.C.").build())
 *         .destinatario(Destinatario.builder()
 *                 .tipoDocumentoIdentidad("6").numeroDocumentoIdentidad("20200020002").nombre("Cliente").build())
 *         .conductor(Driver.builder()
 *                 .tipoDocumentoIdentidad("1").numeroDocumentoIdentidad("12345678")
 *                 .nombres("Juan").apellidos("Perez").licencia("Q123").build())
 *         .vehiculo(Vehicle.builder().placa("ABC-123").build())
 *         .envio(Envio.builder()
 *                 .tipoTraslado("01").pesoTotal(BigDecimal.ONE).pesoTotalUnidadMedida("KGM")
 *                 .tipoModalidadTraslado("01").fechaTraslado(LocalDate.now())
 *                 .partida(Partida.builder().ubigeo("150101").direccion("Origen").build())
 *                 .destino(Destino.builder().ubigeo("150102").direccion("Destino").build())
 *                 .build())
 *         .detalle(DespatchAdviceItem.builder().cantidad(BigDecimal.ONE).unidadMedida("NIU").codigo("001").build())
 *         .build();
 *
 * DespatchAdvice da = gre.toDespatchAdvice();
 * }</pre>
 *
 * @see GRERemitente
 * @see DespatchAdvice
 */
@Data
@Builder
public class GRETransportista {

    // == Datos del comprobante ==

    private String serie;
    private Integer numero;
    private String version;
    private LocalDate fechaEmision;
    private LocalTime horaEmision;
    private String observaciones;

    // == Partes ==

    /**
     * El transportista que emite la guía.
     * Se convierte a {@link Remitente} para mapear a DespatchSupplierParty en el
     * XML.
     * <p>
     * Se usa {@link Transportista} en lugar de {@link Remitente} porque
     * semánticamente
     * SUNAT distingue remitente y transportista como sujetos diferentes.
     */
    private Transportista transportistaEmisor;

    /**
     * El remitente original de los bienes (quien contrata el transporte).
     * Se mapea a SellerSupplierParty (tercero) en el XML.
     */
    private Tercero remitente;

    /**
     * El destinatario de los bienes.
     */
    private Destinatario destinatario;

    private Comprador comprador;
    private Firmante firmante;

    // == Conductor(es) y vehículo - siempre requeridos para transportista ==

    /**
     * Conductor(es) del transporte. Al menos uno es obligatorio.
     * El primero es el conductor principal.
     */
    @Singular("conductor")
    private List<Driver> conductores;

    /**
     * Vehículo principal del transporte. Obligatorio.
     */
    private Vehicle vehiculo;

    // == Documentos relacionados ==

    private DocumentoBaja documentoBaja;

    @Singular("documentoRelacionadoAdicional")
    private List<DocumentoRelacionado> documentosRelacionados;

    @Singular("documentoAdicional")
    private List<DocumentoAdicional> documentosAdicionales;

    // == Datos de envío ==

    /**
     * Datos de envío. El conductor y vehículo se inyectan automáticamente
     * desde los campos {@code conductores} y {@code vehiculo} de este modelo.
     */
    private Envio envio;

    // == Detalle de bienes ==

    @Singular
    private List<DespatchAdviceItem> detalles;

    /**
     * Valida las reglas de negocio para GRE-Transportista y retorna los errores.
     *
     * @return lista de errores (vacía si es válido)
     */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        // Serie
        if (serie == null || serie.isBlank()) {
            errors.add("La serie es requerida");
        } else if (!serie.toUpperCase().startsWith("V")) {
            errors.add("GRE-Transportista requiere serie que inicie con 'V'. Serie actual: " + serie);
        }

        // Número
        if (numero == null || numero < 1) {
            errors.add("El número debe ser mayor a 0");
        }

        // Transportista emisor
        if (transportistaEmisor == null) {
            errors.add("El transportista emisor es requerido");
        } else {
            if (transportistaEmisor.getNumeroDocumentoIdentidad() == null
                    || transportistaEmisor.getNumeroDocumentoIdentidad().length() != 11) {
                errors.add("El RUC del transportista emisor debe tener 11 dígitos");
            }
        }

        // Remitente original (tercero)
        if (remitente == null) {
            errors.add("El remitente original (tercero) es requerido para GRE-Transportista");
        }

        // Destinatario
        if (destinatario == null) {
            errors.add("El destinatario es requerido");
        }

        // Conductor - siempre obligatorio
        if (conductores == null || conductores.isEmpty()) {
            errors.add("GRE-Transportista requiere al menos un conductor");
        }

        // Vehículo - siempre obligatorio
        if (vehiculo == null) {
            errors.add("GRE-Transportista requiere datos del vehículo");
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
    }

    /**
     * Convierte este modelo a {@link DespatchAdvice} para renderizado XML.
     * El tipo de comprobante se fija a "31" (GRE-Transportista).
     * Los conductores y vehículo se inyectan en el envío automáticamente.
     *
     * @return DespatchAdvice listo para enriquecer y renderizar
     */
    public DespatchAdvice toDespatchAdvice() {
        // Build envio with conductores and vehiculo injected
        Envio envioConTransporte = envio != null ? Envio.builder()
                .tipoTraslado(envio.getTipoTraslado())
                .motivoTraslado(envio.getMotivoTraslado())
                .pesoTotal(envio.getPesoTotal())
                .pesoTotalUnidadMedida(envio.getPesoTotalUnidadMedida())
                .pesoItems(envio.getPesoItems())
                .sustentoPeso(envio.getSustentoPeso())
                .numeroDeBultos(envio.getNumeroDeBultos())
                .tipoModalidadTraslado(envio.getTipoModalidadTraslado())
                .fechaTraslado(envio.getFechaTraslado())
                .contenedores(envio.getContenedores())
                .puerto(envio.getPuerto())
                .aeropuerto(envio.getAeropuerto())
                .choferes(conductores)
                .vehiculo(vehiculo)
                .indicadores(envio.getIndicadores())
                .partida(envio.getPartida())
                .destino(envio.getDestino())
                .numeroManifiesto(envio.getNumeroManifiesto())
                .declaracionesAduaneras(envio.getDeclaracionesAduaneras())
                .build() : null;

        DespatchAdvice.DespatchAdviceBuilder builder = DespatchAdvice.builder()
                .serie(serie)
                .numero(numero)
                .version(version)
                .fechaEmision(fechaEmision)
                .horaEmision(horaEmision)
                .tipoComprobante("31")
                .observaciones(observaciones)
                .remitente(transportistaEmisor != null ? Remitente.builder()
                        .ruc(transportistaEmisor.getNumeroDocumentoIdentidad())
                        .razonSocial(transportistaEmisor.getNombre())
                        .numeroRegistroMTC(transportistaEmisor.getNumeroRegistroMTC())
                        .build() : null)
                .destinatario(destinatario)
                .comprador(comprador)
                .tercero(remitente)
                .firmante(firmante)
                .documentoBaja(documentoBaja)
                .envio(envioConTransporte);

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
                    "GRE-Transportista inválido:\n- " + String.join("\n- ", errors));
        }
        return toDespatchAdvice();
    }
}
