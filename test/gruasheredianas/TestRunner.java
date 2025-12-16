package gruasheredianas;

import gruasheredianas.inventario.InventarioTest;
import gruasheredianas.gps.SeguimientoGPSTest;
import gruasheredianas.polizas.PolizasTest;

/**
 * Ejecutor de todas las pruebas unitarias del proyecto.
 * 
 * @author franco
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║                                                       ║");
        System.out.println("║       SUITE DE PRUEBAS - GRUAS HEREDIANAS             ║");
        System.out.println("║                                                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        int pruebasExitosas = 0;
        int pruebasFallidas = 0;
        
        // Ejecutar pruebas de Inventario
        try {
            InventarioTest.ejecutarPruebas();
            pruebasExitosas++;
        } catch (Exception e) {
            System.out.println("\n✗ Módulo Inventario: FALLÓ");
            e.printStackTrace();
            pruebasFallidas++;
        }
        
        // Ejecutar pruebas de GPS
        try {
            SeguimientoGPSTest.ejecutarPruebas();
            pruebasExitosas++;
        } catch (Exception e) {
            System.out.println("\n✗ Módulo GPS: FALLÓ");
            e.printStackTrace();
            pruebasFallidas++;
        }
        
        // Ejecutar pruebas de Pólizas
        try {
            PolizasTest.ejecutarPruebas();
            pruebasExitosas++;
        } catch (Exception e) {
            System.out.println("\n✗ Módulo Pólizas: FALLÓ");
            e.printStackTrace();
            pruebasFallidas++;
        }
        
        // Resumen
        System.out.println("\n\n╔═══════════════════════════════════════════════════════╗");
        System.out.println("║                   RESUMEN DE PRUEBAS                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        System.out.println("\n  Módulos exitosos: " + pruebasExitosas + "/3");
        System.out.println("  Módulos fallidos: " + pruebasFallidas + "/3");
        
        if (pruebasFallidas == 0) {
            System.out.println("\n  ✓ TODAS LAS PRUEBAS PASARON EXITOSAMENTE\n");
        } else {
            System.out.println("\n  ✗ ALGUNAS PRUEBAS FALLARON\n");
            System.exit(1);
        }
    }
}
