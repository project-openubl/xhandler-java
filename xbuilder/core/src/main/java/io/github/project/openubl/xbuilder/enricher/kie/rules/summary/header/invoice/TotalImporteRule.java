package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header.invoice;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53_DescuentoGlobal;
import io.github.project.openubl.xbuilder.content.models.standard.general.Anticipo;
import io.github.project.openubl.xbuilder.content.models.standard.general.Descuento;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImporteInvoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import io.github.project.openubl.xbuilder.enricher.kie.rules.utils.DetalleUtils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class TotalImporteRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isInvoice.test(object) && whenInvoice.apply(object)
                .map(invoice -> invoice.getTotalImporte() == null && invoice.getDetalles() != null)
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = invoice -> {
            BigDecimal totalImpuestos = DetalleUtils.getTotalImpuestos(invoice.getDetalles());

            BigDecimal importeSinImpuestos = DetalleUtils.getImporteSinImpuestos(invoice.getDetalles());
            BigDecimal importeConImpuestos = importeSinImpuestos.add(totalImpuestos);

            // Anticipos
            BigDecimal anticipos = invoice.getAnticipos().stream()
                    .map(Anticipo::getMonto)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal importeTotal = importeConImpuestos.subtract(anticipos);

            // Descuentos
            Map<Catalog53_DescuentoGlobal, BigDecimal> descuentos = invoice.getDescuentos().stream()
                    .filter(descuento -> descuento.getTipoDescuento() != null && descuento.getMonto() != null)
                    .collect(Collectors.groupingBy(
                            descuento -> Catalog.valueOfCode(Catalog53_DescuentoGlobal.class, descuento.getTipoDescuento()).orElseThrow(Catalog.invalidCatalogValue),
                            Collectors.reducing(BigDecimal.ZERO, Descuento::getMonto, BigDecimal::add)
                    ));

            BigDecimal descuentosQueAfectanBaseImponible_sinImpuestos = descuentos.getOrDefault(Catalog53_DescuentoGlobal.DESCUENTO_GLOBAL_AFECTA_BASE_IMPONIBLE_IGV_IVAP, BigDecimal.ZERO);
            BigDecimal descuentosQueAfectanBaseImponible_conImpuestos = descuentosQueAfectanBaseImponible_sinImpuestos.multiply(invoice.getTasaIgv().add(BigDecimal.ONE));

            BigDecimal descuentosQueNoAfectanBaseImponible_sinImpuestos = descuentos.getOrDefault(Catalog53_DescuentoGlobal.DESCUENTO_GLOBAL_NO_AFECTA_BASE_IMPONIBLE_IGV_IVAP, BigDecimal.ZERO);

            //
            importeSinImpuestos = importeSinImpuestos.subtract(descuentosQueAfectanBaseImponible_sinImpuestos);
            importeConImpuestos = importeConImpuestos.subtract(descuentosQueAfectanBaseImponible_conImpuestos);
            importeTotal = importeTotal
                    .subtract(descuentosQueAfectanBaseImponible_conImpuestos)
                    .subtract(descuentosQueNoAfectanBaseImponible_sinImpuestos);

            // Set final values
            invoice.setTotalImporte(TotalImporteInvoice.builder()
                    .importeSinImpuestos(importeSinImpuestos)
                    .importeConImpuestos(importeConImpuestos)
                    .descuentos(descuentosQueNoAfectanBaseImponible_sinImpuestos)
                    .anticipos(anticipos)
                    .importe(importeTotal)
                    .build()
            );
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
