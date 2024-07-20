package e2e.homologacion;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog6;
import io.github.project.openubl.xbuilder.content.models.common.Cliente;
import io.github.project.openubl.xbuilder.content.models.common.Proveedor;

public class HomologacionConstants {

    public static final Proveedor proveedor = Proveedor.builder()
            .ruc("12345678912")
            .razonSocial("Softgreen S.A.C.")
            .build();
    public static final Cliente cliente = Cliente.builder()
            .nombre("Carlos Feria")
            .numeroDocumentoIdentidad("12121212121")
            .tipoDocumentoIdentidad(Catalog6.RUC.toString())
            .build();
}
