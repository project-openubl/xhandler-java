package io.github.project.openubl.quickstart.xbuilder.jee;

import io.github.project.openubl.xbuilder.models.Cliente;
import io.github.project.openubl.xbuilder.models.Detalle;
import io.github.project.openubl.xbuilder.models.Invoice;
import io.github.project.openubl.xbuilder.models.Proveedor;

import java.math.BigDecimal;
import java.util.List;

public class UBLService {

    public Invoice createInvoice(String clientName) {
        return Invoice.builder()
                .serieNumero("F001-1")
                .proveedor(Proveedor.builder()
                        .ruc("12345678912")
                        .razonSocial("Softgreen S.A.C.")
                        .build())
                .cliente(Cliente.builder()
                        .nombre(clientName)
                        .numeroDocumentoIdentidad("12121212121")
                        .tipoDocumentoIdentidad("6") // RUC (Catalog6)
                        .build())
                .detalles(List.of(
                        Detalle.builder().descripcion("Item1").cantidad(new BigDecimal("10")).precio(new BigDecimal("100")).unidadMedida("KGM").build(),
                        Detalle.builder().descripcion("Item2").cantidad(new BigDecimal("10")).precio(new BigDecimal("100")).unidadMedida("KGM").build()))
                .build();
    }
}
