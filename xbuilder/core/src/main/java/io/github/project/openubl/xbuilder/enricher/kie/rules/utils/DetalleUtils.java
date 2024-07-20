package io.github.project.openubl.xbuilder.enricher.kie.rules.utils;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog5;
import io.github.project.openubl.xbuilder.content.catalogs.Catalog7;
import io.github.project.openubl.xbuilder.content.models.standard.general.DocumentoVentaDetalle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DetalleUtils {

    public static BigDecimal getImporteSinImpuestos(List<DocumentoVentaDetalle> detalles) {
        return detalles.stream()
                .filter(item -> {
                    Catalog7 catalog7 = Catalog
                            .valueOfCode(Catalog7.class, item.getIgvTipo())
                            .orElseThrow(Catalog.invalidCatalogValue);
                    return !catalog7.getTaxCategory().equals(Catalog5.GRATUITO);
                })
                .map(detalle -> detalle.getIscBaseImponible() != null && detalle.getIscBaseImponible().compareTo(BigDecimal.ZERO) > 0 ?
                        detalle.getIscBaseImponible() :
                        detalle.getIgvBaseImponible()
                )
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal getTotalImpuestos(List<DocumentoVentaDetalle> detalles) {
        return detalles.stream()
                .map(DocumentoVentaDetalle::getTotalImpuestos)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Impuesto calImpuestoByTipo(List<DocumentoVentaDetalle> detalle, Catalog5 categoria) {
        Supplier<Stream<DocumentoVentaDetalle>> stream = () -> detalle.stream()
                .filter($il -> {
                    Catalog7 catalog7 = Catalog
                            .valueOfCode(Catalog7.class, $il.getIgvTipo())
                            .orElseThrow(Catalog.invalidCatalogValue);
                    return catalog7.getTaxCategory().equals(categoria);
                });

        BigDecimal baseImponible = stream.get()
                .map(DocumentoVentaDetalle::getIscBaseImponible)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal importe = stream.get()
                .map(DocumentoVentaDetalle::getTotalImpuestos)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal importeIsc = stream.get()
                .map(DocumentoVentaDetalle::getIsc)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal importeIgv = stream.get()
                .map(DocumentoVentaDetalle::getIgv)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal importeIcb = stream.get()
                .map(DocumentoVentaDetalle::getIcb)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Impuesto.builder()
                .baseImponible(baseImponible)
                .importe(importe)
                .importeIsc(importeIsc)
                .importeIgv(importeIgv)
                .importeIcb(importeIcb)
                .build();
    }
}
