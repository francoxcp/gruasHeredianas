package gruasheredianas.gps;

import gruasheredianas.gps.model.UbicacionGPS;
import gruasheredianas.gps.model.RutaHistorial;
import java.util.Date;

/**
 * Pruebas unitarias para el módulo de seguimiento GPS.
 * 
 * @author franco
 */
public class SeguimientoGPSTest {
    
    /**
     * Prueba la creación de una ubicación GPS.
     */
    public static void testCrearUbicacionGPS() {
        System.out.println("\n=== Test: Crear Ubicación GPS ===");
        
        UbicacionGPS ubicacion = new UbicacionGPS();
        ubicacion.setCamionId(1);
        ubicacion.setLatitud(9.9326);
        ubicacion.setLongitud(-84.0769);
        ubicacion.setVelocidad(45.5);
        ubicacion.setEstado("EN_SERVICIO");
        ubicacion.setDireccion("Centro de San José, Costa Rica");
        ubicacion.setFechaHora(new Date());
        
        assert ubicacion.getCamionId() == 1;
        assert ubicacion.getLatitud() == 9.9326;
        assert ubicacion.getLongitud() == -84.0769;
        assert ubicacion.getEstado().equals("EN_SERVICIO");
        
        System.out.println("✓ Ubicación GPS creada correctamente");
        System.out.println("  Grúa ID: " + ubicacion.getCamionId());
        System.out.println("  Latitud: " + ubicacion.getLatitud());
        System.out.println("  Longitud: " + ubicacion.getLongitud());
        System.out.println("  Estado: " + ubicacion.getEstado());
    }
    
    /**
     * Prueba el cálculo de distancia entre dos ubicaciones.
     */
    public static void testCalcularDistancia() {
        System.out.println("\n=== Test: Calcular Distancia ===");
        
        // San José, Costa Rica
        UbicacionGPS ubicacion1 = new UbicacionGPS();
        ubicacion1.setLatitud(9.9326);
        ubicacion1.setLongitud(-84.0769);
        
        // Heredia, Costa Rica (aproximadamente 10 km)
        UbicacionGPS ubicacion2 = new UbicacionGPS();
        ubicacion2.setLatitud(9.9989);
        ubicacion2.setLongitud(-84.1177);
        
        double distancia = ubicacion1.calcularDistancia(ubicacion2);
        
        // La distancia debe ser aproximadamente 10 km
        assert distancia > 5 && distancia < 15;
        
        System.out.println("✓ Distancia calculada correctamente");
        System.out.println("  Punto 1: " + ubicacion1.getLatitud() + ", " + ubicacion1.getLongitud());
        System.out.println("  Punto 2: " + ubicacion2.getLatitud() + ", " + ubicacion2.getLongitud());
        System.out.println("  Distancia: " + String.format("%.2f km", distancia));
    }
    
    /**
     * Prueba la creación de una ruta en el historial.
     */
    public static void testCrearRutaHistorial() {
        System.out.println("\n=== Test: Crear Ruta Historial ===");
        
        RutaHistorial ruta = new RutaHistorial();
        ruta.setCamionId(1);
        ruta.setFechaInicio(new Date());
        ruta.setFechaFin(new Date(System.currentTimeMillis() + 3600000)); // +1 hora
        ruta.setLatitudInicio(9.9326);
        ruta.setLongitudInicio(-84.0769);
        ruta.setLatitudFin(9.9989);
        ruta.setLongitudFin(-84.1177);
        ruta.setDistanciaRecorrida(10.5);
        ruta.setTiempoServicio(45); // 45 minutos
        ruta.setTipoServicio("TRASLADO");
        
        assert ruta.getCamionId() == 1;
        assert ruta.getDistanciaRecorrida() == 10.5;
        assert ruta.getTiempoServicio() == 45;
        assert ruta.getTipoServicio().equals("TRASLADO");
        
        System.out.println("✓ Ruta creada correctamente");
        System.out.println("  Grúa ID: " + ruta.getCamionId());
        System.out.println("  Distancia: " + ruta.getDistanciaRecorrida() + " km");
        System.out.println("  Tiempo: " + ruta.getTiempoServicio() + " min");
        System.out.println("  Tipo: " + ruta.getTipoServicio());
    }
    
    /**
     * Prueba el cálculo de velocidad promedio.
     */
    public static void testCalcularVelocidadPromedio() {
        System.out.println("\n=== Test: Calcular Velocidad Promedio ===");
        
        RutaHistorial ruta = new RutaHistorial();
        ruta.setDistanciaRecorrida(30.0); // 30 km
        ruta.setTiempoServicio(60); // 60 minutos (1 hora)
        
        double velocidadPromedio = ruta.getVelocidadPromedio();
        
        // 30 km en 1 hora = 30 km/h
        assert velocidadPromedio == 30.0;
        
        System.out.println("✓ Velocidad promedio calculada correctamente");
        System.out.println("  Distancia: " + ruta.getDistanciaRecorrida() + " km");
        System.out.println("  Tiempo: " + ruta.getTiempoServicio() + " min");
        System.out.println("  Velocidad promedio: " + velocidadPromedio + " km/h");
    }
    
    /**
     * Ejecuta todas las pruebas del módulo GPS.
     */
    public static void ejecutarPruebas() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  PRUEBAS UNITARIAS - MÓDULO GPS       ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        try {
            testCrearUbicacionGPS();
            testCalcularDistancia();
            testCrearRutaHistorial();
            testCalcularVelocidadPromedio();
            
            System.out.println("\n✓ Todas las pruebas del módulo GPS pasaron exitosamente");
        } catch (AssertionError e) {
            System.out.println("\n✗ Error en las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ejecutarPruebas();
    }
}
