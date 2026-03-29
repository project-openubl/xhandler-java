package io.github.project.openubl.xbuilder.content.models.standard.guia;

import io.github.project.openubl.xbuilder.content.models.common.Firmante;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.DespatchAdviceCommonValidator;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationMessage;
import io.github.project.openubl.xbuilder.content.models.standard.guia.validation.ValidationResult;
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
 *         .serie("T001")
 *         .numero(1)
 *         .remitente(Remitente.builder().ruc("20100010001").razonSocial("Mi Empresa").build())
 *         .destinatario(Destinatario.builder()
 *                 .tipoDocumentoIdentidad("6")
 *                 .numeroDocumentoIdentidad("20200020002")
 *                 .nombre("Cliente")
 *                 .build())
 *         .envio(Envio.builder()
 *                 .tipoTraslado("01")
 *                 .pesoTotal(BigDecimal.ONE)
 *                 .pesoTotalUnidadMedida("KGM")
 *                 .tipoModalidadTraslado("02")
 *                 .fechaTraslado(LocalDate.now())
 *                 .chofer(Driver.builder()
 *                         .tipoDocumentoIdentidad("1")
 *                         .numeroDocumentoIdentidad("12345678")
 *                         .nombres("Juan")
 *                         .apellidos("Perez")
 *                         .licencia("Q123")
 *                         .build())
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
     * <p>
     * Delega las validaciones comunes a {@link DespatchAdviceCommonValidator} y aplica solo las reglas específicas del
     * tipo 09 (serie T*, modalidad remitente).
     * <p>
     * Solo retorna errores (severidad {@code ERROR}). Para obtener errores y advertencias diferenciados, use
     * {@link #validateDetailed()}.
     *
     * @return lista de errores (vacía si es válido)
     * @see #validateDetailed()
     */
    public List<String> validate() {
        return validateDetailed().getErrors();
    }

    /**
     * Valida las reglas de negocio y retorna un {@link ValidationResult} con errores y advertencias diferenciados por
     * severidad.
     *
     * @return resultado de validación con errores y advertencias
     * @since 5.2.0
     */
    public ValidationResult validateDetailed() {
        List<ValidationMessage> messages = new ArrayList<>();

        // Campos básicos (serie, número)
        DespatchAdviceCommonValidator.validateBasicFields(serie, numero, messages);

        // Serie específica remitente: debe iniciar con 'T'
        DespatchAdviceCommonValidator.validateSerieRemitente(serie, messages);

        // Partes
        DespatchAdviceCommonValidator.validateRemitente(remitente, messages);
        DespatchAdviceCommonValidator.validateDestinatario(destinatario, messages);

        // Envío
        DespatchAdviceCommonValidator.validateEnvioRequired(envio, messages);
        DespatchAdviceCommonValidator.validatePartidaDestino(envio, messages);

        // Modalidad específica remitente (privado/público)
        DespatchAdviceCommonValidator.validateModalidadRemitente(envio, messages);

        // Comercio exterior (advertencias)
        DespatchAdviceCommonValidator.validateComercioExterior(envio, messages);

        // Detalles
        DespatchAdviceCommonValidator.validateDetalles(detalles, messages);

        return new ValidationResult(messages);
    }

    /**
     * Convierte este modelo a {@link DespatchAdvice} para renderizado XML. El tipo de comprobante se fija a "09"
     * (GRE-Remitente).
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
