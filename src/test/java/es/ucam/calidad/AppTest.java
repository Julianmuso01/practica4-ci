package es.ucam.calidad;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class AppTest {

    // =======================
    // Tests de parsearFechaISO
    // =======================

    @Test
    public void parsearFechaISO_ok() {
        LocalDate fecha = App.parsearFechaISO("2026-01-05");
        assertEquals(LocalDate.of(2026, 1, 5), fecha);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsearFechaISO_vacia_falla() {
        App.parsearFechaISO("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsearFechaISO_formato_incorrecto_falla() {
        App.parsearFechaISO("05/01/2026");
    }

    // =======================
    // Tests de parsearImporte
    // =======================

    @Test
    public void parsearImporte_ok_con_punto() {
        assertEquals(new BigDecimal("19.99"), App.parsearImporte("19.99"));
    }

    @Test
    public void parsearImporte_ok_con_coma() {
        assertEquals(new BigDecimal("19.99"), App.parsearImporte("19,99"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsearImporte_negativo_falla() {
        App.parsearImporte("-1.00");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsearImporte_texto_invalido_falla() {
        App.parsearImporte("abc");
    }

    // =======================
    // Tests de parsearPorcentajeIVA
    // =======================

    @Test
    public void parsearPorcentajeIVA_ok_con_porcentaje() {
        assertEquals(21, App.parsearPorcentajeIVA("21%"));
    }

    @Test
    public void parsearPorcentajeIVA_ok_sin_porcentaje() {
        assertEquals(10, App.parsearPorcentajeIVA("10"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsearPorcentajeIVA_fuera_de_rango_falla() {
        App.parsearPorcentajeIVA("150");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsearPorcentajeIVA_invalido_falla() {
        App.parsearPorcentajeIVA("IVA21");
    }

    // =======================
    // Tests de aplicarIVA
    // =======================

    @Test
    public void aplicarIVA_ok() {
        BigDecimal total = App.aplicarIVA(new BigDecimal("100.00"), 21);
        assertEquals(new BigDecimal("121.00"), total);
    }

    @Test
    public void aplicarIVA_iva_cero() {
        BigDecimal total = App.aplicarIVA(new BigDecimal("50.00"), 0);
        assertEquals(new BigDecimal("50.00"), total);
    }

    // =======================
    // Tests integrados: formatearFactura
    // =======================

    @Test
    public void formatearFactura_ok() {
        String s = App.formatearFactura("2026-01-05", "19.99", 3, "21");
        // base = 59.97, total = 72.56
        assertTrue(s.contains("Factura 2026-01-05"));
        assertTrue(s.contains("base=59.97"));
        assertTrue(s.contains("iva=21%"));
        assertTrue(s.contains("total=72.56"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void formatearFactura_unidades_cero_falla() {
        App.formatearFactura("2026-01-05", "10", 0, "21");
    }
}
