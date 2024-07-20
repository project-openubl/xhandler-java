package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header.invoice;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog5;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53_Anticipo;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog53_DescuentoGlobal;
import io.github.project.openubl.xbuilder.content.models.standard.general.Anticipo;
import io.github.project.openubl.xbuilder.content.models.standard.general.Descuento;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.general.TotalImpuestos;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import io.github.project.openubl.xbuilder.enricher.kie.rules.utils.DetalleUtils;
import io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Impuesto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class TotalImpuestosRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (isInvoice.test(object) && whenInvoice.apply(object)
                .map(documento -> documento.getTotalImpuestos() == null && documento.getDetalles() != null)
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = invoice -> {
            Impuesto ivap = DetalleUtils.calImpuestoByTipo(invoice.getDetalles(), Catalog5.IMPUESTO_ARROZ_PILADO);
            Impuesto exportacion = DetalleUtils.calImpuestoByTipo(invoice.getDetalles(), Catalog5.EXPORTACION);
            Impuesto gravado = DetalleUtils.calImpuestoByTipo(invoice.getDetalles(), Catalog5.IGV);
            Impuesto inafecto = DetalleUtils.calImpuestoByTipo(invoice.getDetalles(), Catalog5.INAFECTO);
            Impuesto exonerado = DetalleUtils.calImpuestoByTipo(invoice.getDetalles(), Catalog5.EXONERADO);
            Impuesto gratuito = DetalleUtils.calImpuestoByTipo(invoice.getDetalles(), Catalog5.GRATUITO);

            // ICB
            BigDecimal icbImporte = Stream.of(
                            ivap.getImporteIcb(),
                            exportacion.getImporteIcb(),
                            gravado.getImporteIcb(),
                            inafecto.getImporteIcb(),
                            exonerado.getImporteIcb(),
                            gratuito.getImporteIcb()
                    )
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ISC
            BigDecimal iscImporte = Stream.of(
                            ivap.getImporteIsc(),
                            exportacion.getImporteIsc(),
                            gravado.getImporteIsc(),
                            inafecto.getImporteIsc(),
                            exonerado.getImporteIsc(),
                            gratuito.getImporteIsc()
                    )
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal iscBaseImponible = invoice.getDetalles().stream()
                    .filter(d -> d.getTasaIsc().compareTo(BigDecimal.ZERO) != 0)
                    .map(DocumentoVentaDetalle::getIscBaseImponible)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // ANTICIPOS
            BigDecimal totalAnticiposGravados = invoice.getAnticipos().stream()
                    .filter(f -> {
                        Optional<Catalog53_Anticipo> catalog53_anticipo = Catalog.valueOfCode(Catalog53_Anticipo.class, f.getTipo());
                        return (catalog53_anticipo.isPresent() && catalog53_anticipo.get().equals(Catalog53_Anticipo.DESCUENTO_GLOBAL_POR_ANTICIPOS_GRAVADOS_AFECTA_BASE_IMPONIBLE_IGV_IVAP));
                    })
                    .map(Anticipo::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // DESCUENTOS
            Map<Catalog53_DescuentoGlobal, BigDecimal> descuentos = invoice.getDescuentos().stream()
                    .filter(descuento -> descuento.getTipoDescuento() != null && descuento.getMonto() != null)
                    .collect(Collectors.groupingBy(
                            descuento -> Catalog.valueOfCode(Catalog53_DescuentoGlobal.class, descuento.getTipoDescuento()).orElseThrow(Catalog.invalidCatalogValue),
                            Collectors.reducing(BigDecimal.ZERO, Descuento::getMonto, BigDecimal::add)
                    ));
            BigDecimal descuentosQueAfectanBaseImponible_sinImpuestos = descuentos.getOrDefault(Catalog53_DescuentoGlobal.DESCUENTO_GLOBAL_AFECTA_BASE_IMPONIBLE_IGV_IVAP, BigDecimal.ZERO);

            // Aplicar ANTICIPOS y DESCUENTOS
            BigDecimal gravadoBaseImponible = gravado.getBaseImponible()
                    .subtract(totalAnticiposGravados)
                    .subtract(descuentosQueAfectanBaseImponible_sinImpuestos);

            BigDecimal factor = BigDecimal.ONE;
            if (gravado.getBaseImponible().compareTo(BigDecimal.ZERO) > 0) {
                factor = gravadoBaseImponible.divide(gravado.getBaseImponible(), 10, RoundingMode.HALF_EVEN);
            }

            BigDecimal total = gravado.getImporte()
                    .add(ivap.getImporte())
                    .add(exportacion.getImporte())
                    .multiply(factor);

            // Set final values
            TotalImpuestos totalImpuestos = TotalImpuestos.builder()
                    // IVAP
                    .ivapImporte(ivap.getImporteIgv())
                    .ivapBaseImponible(ivap.getBaseImponible())

                    // EXPORTACION
                    .exportacionImporte(exportacion.getImporteIgv())
                    .exportacionBaseImponible(exportacion.getBaseImponible())

                    // ISC and GRAVADO go together
                    .iscImporte(iscImporte)
                    .iscBaseImponible(iscBaseImponible)
                    .gravadoImporte(gravado.getImporteIgv().multiply(factor)) //
                    .gravadoBaseImponible(gravadoBaseImponible) //

                    // INAFECTO, EXONERADO, GRATUITO
                    .inafectoImporte(inafecto.getImporteIgv())
                    .inafectoBaseImponible(inafecto.getBaseImponible())
                    .exoneradoImporte(exonerado.getImporteIgv())
                    .exoneradoBaseImponible(exonerado.getBaseImponible())
                    .gratuitoImporte(gratuito.getImporteIgv())
                    .gratuitoBaseImponible(gratuito.getBaseImponible())

                    // ICB
                    .icbImporte(icbImporte)

                    .total(total)
                    .build();

            invoice.setTotalImpuestos(totalImpuestos);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
