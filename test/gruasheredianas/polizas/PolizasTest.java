package gruasheredianas.polizas;

import gruasheredianas.polizas.model.PolizaSeguro;
import java.util.Calendar;
import java.util.Date;

/**
 * Pruebas unitarias para el módulo de pólizas y seguros.
 * 
 * @author franco
 */
public class PolizasTest {
    
    /**
     * Prueba la creación de una póliza de seguro.
     */
    public static void testCrearPoliza() {
        System.out.println("\n=== Test: Crear Póliza ===");
        
        Calendar cal = Calendar.getInstance();
        Date fechaInicio = cal.getTime();
        
        cal.add(Calendar.YEAR, 1);
        Date fechaVencimiento = cal.getTime();
        
        PolizaSeguro poliza = new PolizaSeguro();
        poliza.setCamionId(1);
        poliza.setNumeroPoliza("POL-2024-001");
        poliza.setAseguradora("INS Costa Rica");
        poliza.setFechaInicio(fechaInicio);
        poliza.setFechaVencimiento(fechaVencimiento);
        poliza.setTipoCobertura("TOTAL");
        poliza.setMontoCobertura(50000.00);
        poliza.setPrimaMensual(150.00);
        poliza.setEstado("ACTIVA");
        poliza.setObservaciones("Cobertura completa");
        
        assert poliza.getNumeroPoliza().equals("POL-2024-001");
        assert poliza.getAseguradora().equals("INS Costa Rica");
        assert poliza.getTipoCobertura().equals("TOTAL");
        assert poliza.getEstado().equals("ACTIVA");
        
        System.out.println("✓ Póliza creada correctamente");
        System.out.println("  Número: " + poliza.getNumeroPoliza());
        System.out.println("  Aseguradora: " + poliza.getAseguradora());
        System.out.println("  Cobertura: $" + poliza.getMontoCobertura());
        System.out.println("  Prima mensual: $" + poliza.getPrimaMensual());
    }
    
    /**
     * Prueba el cálculo de días hasta el vencimiento.
     */
    public static void testDiasHastaVencimiento() {
        System.out.println("\n=== Test: Días Hasta Vencimiento ===");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 15); // Vence en 15 días
        
        PolizaSeguro poliza = new PolizaSeguro();
        poliza.setFechaVencimiento(cal.getTime());
        
        long diasRestantes = poliza.getDiasHastaVencimiento();
        
        // Debe ser aproximadamente 15 días (puede variar por horas)
        assert diasRestantes >= 14 && diasRestantes <= 16;
        
        System.out.println("✓ Días hasta vencimiento calculados correctamente");
        System.out.println("  Fecha vencimiento: " + poliza.getFechaVencimiento());
        System.out.println("  Días restantes: " + diasRestantes);
    }
    
    /**
     * Prueba la verificación de póliza por vencer.
     */
    public static void testPolizaPorVencer() {
        System.out.println("\n=== Test: Póliza Por Vencer ===");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 20); // Vence en 20 días
        
        PolizaSeguro poliza = new PolizaSeguro();
        poliza.setFechaVencimiento(cal.getTime());
        
        boolean porVencer = poliza.estaPorVencer();
        
        // Debe estar por vencer (dentro de 30 días)
        assert porVencer == true;
        
        System.out.println("✓ Verificación de póliza por vencer correcta");
        System.out.println("  Días restantes: " + poliza.getDiasHastaVencimiento());
        System.out.println("  Por vencer: " + porVencer);
    }
    
    /**
     * Prueba la verificación de póliza vencida.
     */
    public static void testPolizaVencida() {
        System.out.println("\n=== Test: Póliza Vencida ===");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -10); // Venció hace 10 días
        
        PolizaSeguro poliza = new PolizaSeguro();
        poliza.setFechaVencimiento(cal.getTime());
        
        boolean vencida = poliza.estaVencida();
        
        assert vencida == true;
        
        System.out.println("✓ Verificación de póliza vencida correcta");
        System.out.println("  Días desde vencimiento: " + Math.abs(poliza.getDiasHastaVencimiento()));
        System.out.println("  Vencida: " + vencida);
    }
    
    /**
     * Prueba el cálculo del costo anual.
     */
    public static void testCalcularCostoAnual() {
        System.out.println("\n=== Test: Calcular Costo Anual ===");
        
        PolizaSeguro poliza = new PolizaSeguro();
        poliza.setPrimaMensual(150.00);
        
        double costoAnual = poliza.getCostoAnual();
        
        assert costoAnual == 1800.00;
        
        System.out.println("✓ Costo anual calculado correctamente");
        System.out.println("  Prima mensual: $" + poliza.getPrimaMensual());
        System.out.println("  Costo anual: $" + costoAnual);
    }
    
    /**
     * Prueba póliza vigente (no por vencer ni vencida).
     */
    public static void testPolizaVigente() {
        System.out.println("\n=== Test: Póliza Vigente ===");
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 6); // Vence en 6 meses
        
        PolizaSeguro poliza = new PolizaSeguro();
        poliza.setFechaVencimiento(cal.getTime());
        
        boolean vencida = poliza.estaVencida();
        boolean porVencer = poliza.estaPorVencer();
        
        assert vencida == false;
        assert porVencer == false;
        
        System.out.println("✓ Verificación de póliza vigente correcta");
        System.out.println("  Días restantes: " + poliza.getDiasHastaVencimiento());
        System.out.println("  Vencida: " + vencida);
        System.out.println("  Por vencer: " + porVencer);
    }
    
    /**
     * Ejecuta todas las pruebas del módulo de pólizas.
     */
    public static void ejecutarPruebas() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  PRUEBAS UNITARIAS - MÓDULO PÓLIZAS   ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        try {
            testCrearPoliza();
            testDiasHastaVencimiento();
            testPolizaPorVencer();
            testPolizaVencida();
            testCalcularCostoAnual();
            testPolizaVigente();
            
            System.out.println("\n✓ Todas las pruebas del módulo de pólizas pasaron exitosamente");
        } catch (AssertionError e) {
            System.out.println("\n✗ Error en las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ejecutarPruebas();
    }
}
