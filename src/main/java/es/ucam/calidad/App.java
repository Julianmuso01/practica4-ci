package es.ucam.calidad;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Ejemplo más completo para practicar CI:
 * - Validaciones de entrada
 * - Parseo de texto
 * - Cálculos con BigDecimal
 * - Uso de excepciones y mensajes claros
 */
public class App {

    public static void main(String[] args) {
        System.out.println("Utilidades de Calidad del Software - Practica 4");
        System.out.println("Ejemplo: " + formatearFactura("2026-01-05", "  19.99 ", 3, "21"));
    }

    /**
     * Construye un texto de factura a partir de datos en texto, validando entradas.
     * Formato de salida (ejemplo):
     * "Factura 2026-01-05 | base=19.99 | uds=3 | iva=21% | total=72.56"
     */
    public static String formatearFactura(String fechaIso, String precioUnitarioTexto, int unidades, String ivaTexto) {
        LocalDate fecha = parsearFechaISO(fechaIso);
        BigDecimal precio = parsearImporte(precioUnitarioTexto);
        int iva = parsearPorcentajeIVA(ivaTexto);

        if (unidades <= 0) {
            throw new IllegalArgumentException("Las unidades deben ser > 0");
        }

        BigDecimal base = precio.multiply(BigDecimal.valueOf(unidades));
        BigDecimal total = aplicarIVA(base, iva);

        return "Factura " + fecha
                + " | base=" + base.setScale(2, RoundingMode.HALF_UP)
                + " | uds=" + unidades
                + " | iva=" + iva + "%"
                + " | total=" + total.setScale(2, RoundingMode.HALF_UP);
    }

    public static LocalDate parsearFechaISO(String fechaIso) {
        if (fechaIso == null || fechaIso.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        try {
            return LocalDate.parse(fechaIso.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha inválida (formato esperado YYYY-MM-DD): " + fechaIso, e);
        }
    }

    public static BigDecimal parsearImporte(String importeTexto) {
        if (importeTexto == null || importeTexto.trim().isEmpty()) {
            throw new IllegalArgumentException("El importe no puede estar vacío")
        }
        String limpio = importeTexto.trim().replace(",", ".");
        try {
            BigDecimal valor = new BigDecimal(limpio);
            if (valor.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El importe no puede ser negativo");
            }
            // Redondeo interno a 2 decimales
            return valor.setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Importe inválido: " + importeTexto, e);
        }
    }

    public static int parsearPorcentajeIVA(String ivaTexto) {
        if (ivaTexto == null || ivaTexto.trim().isEmpty()) {
            throw new IllegalArgumentException("El IVA no puede estar vacío");
        }
        String limpio = ivaTexto.trim().replace("%", "");
        int iva;
        try {
            iva = Integer.parseInt(limpio);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("IVA inválido: " + ivaTexto, e);
        }
        if (iva < 0 || iva > 100) {
            throw new IllegalArgumentException("IVA fuera de rango [0..100]: " + iva);
        }
        return iva;
    }

    public static BigDecimal aplicarIVA(BigDecimal base, int iva) {
        Objects.requireNonNull(base, "La base no puede ser null");
        if (base.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La base no puede ser negativa");
        }
        BigDecimal factor = BigDecimal.ONE.add(BigDecimal.valueOf(iva).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        return base.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
