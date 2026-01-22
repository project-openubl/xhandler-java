package e2e.renderer.despatchadvice;

import e2e.AbstractTest;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog1;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog18;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog20;
import io.github.project.openubl.xbuilder.content.models.standard.guia.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class DespatchAdviceCarrierTest extends AbstractTest {

    @Test
    public void testCarrierData() throws Exception {
        // Given
        DespatchAdvice input = DespatchAdvice.builder()
                .serie("V001")
                .numero(1)
                .version("2.1")
                .tipoComprobante(Catalog1.GUIA_REMISION_TRANSPORTISTA.getCode()) // 31
                .remitente(Remitente.builder() // En una 31, el remitente es el Transportista emitente
                        .ruc("20123456789")
                        .razonSocial("Transportes Veloz S.A.C.")
                        .numeroRegistroMTC("MTC-654321")
                        .build()
                )
                .destinatario(Destinatario.builder()
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20876543210")
                        .nombre("Cliente Final S.A.")
                        .build()
                )
                .tercero(Tercero.builder() // El Remitente original (el que solicita el transporte)
                        .tipoDocumentoIdentidad(Catalog6.RUC.getCode())
                        .numeroDocumentoIdentidad("20555555555")
                        .nombre("Empresa Vendedora S.A.C.")
                        .build()
                )
                .envio(Envio.builder()
                        .tipoTraslado(Catalog20.VENTA.getCode())
                        .pesoTotal(new BigDecimal("500.00"))
                        .pesoTotalUnidadMedida("KGM")
                        .tipoModalidadTraslado(Catalog18.TRANSPORTE_PUBLICO.getCode())
                        .fechaTraslado(dateProvider.now())
                        .chofer(Driver.builder()
                                .tipoDocumentoIdentidad(Catalog6.DNI.getCode())
                                .numeroDocumentoIdentidad("44444444")
                                .nombres("Carlos")
                                .apellidos("Guerrero")
                                .licencia("L5555555")
                                .build())
                        .vehiculo(Vehicle.builder()
                                .placa("XYZ-789")
                                .numeroCirculacion("TUC-V001")
                                .build())
                        .partida(Partida.builder()
                                .direccion("Almacen Principal")
                                .ubigeo("150101")
                                .build())
                        .destino(Destino.builder()
                                .direccion("Tienda Centro")
                                .ubigeo("150102")
                                .build()
                        )
                        .build()
                )
                .detalle(DespatchAdviceItem.builder()
                        .cantidad(new BigDecimal("10.00"))
                        .unidadMedida("NIU")
                        .codigo("PROD-99")
                        .descripcion("Mercaderia variada")
                        .build()
                )
                .build();

        // When/Then
        assertInput(input, "carrierData.xml");
    }
}
