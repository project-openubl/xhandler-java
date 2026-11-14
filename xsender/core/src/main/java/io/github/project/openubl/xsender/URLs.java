package io.github.project.openubl.xsender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class URLs {

    private final String invoice;
    private final String perceptionRetention;
    private final String despatch;

    /**
     * SUNAT beta (testing) services. SUNAT has no beta for guías de remisión, so
     * {@code despatch} is null.
     */
    public static URLs beta() {
        return new URLs(
                "https://e-beta.sunat.gob.pe/ol-ti-itcpfegem-beta/billService",
                "https://e-beta.sunat.gob.pe/ol-ti-itemision-otroscpe-gem-beta/billService",
                null
        );
    }

    /**
     * SUNAT production services.
     */
    public static URLs production() {
        return new URLs(
                "https://e-factura.sunat.gob.pe/ol-ti-itcpfegem/billService",
                "https://e-factura.sunat.gob.pe/ol-ti-itemision-otroscpe-gem/billService",
                "https://api-cpe.sunat.gob.pe/v1/contribuyente/gem"
        );
    }
}
