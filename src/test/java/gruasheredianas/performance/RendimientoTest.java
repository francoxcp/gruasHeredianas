package gruasheredianas.performance;

import gruasheredianas.inventario.dao.InventarioDAO;
import gruasheredianas.inventario.model.Item;
import gruasheredianas.grua.dao.GruaDAO;
import gruasheredianas.grua.model.Grua;
import gruasheredianas.polizas.dao.PolizaDAO;
import gruasheredianas.polizas.model.Poliza;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

/**
 * Pruebas de rendimiento para evaluar el comportamiento del sistema con carga alta
 * @author franc
 */
public class RendimientoTest {
    
    private InventarioDAO inventarioDAO;
    private GruaDAO gruaDAO;
    private PolizaDAO polizaDAO;
    
    // Constantes para pruebas de rendimiento
    private static final int BULK_SIZE = 500;
    private static final long MAX_ACCEPTABLE_TIME_MS = 5000; // 5 segundos
    
    @BeforeEach
    public void setUp() {
        inventarioDAO = new InventarioDAO();
        gruaDAO = new GruaDAO();
        polizaDAO = new PolizaDAO();
    }
    
    @Test
    @DisplayName("Rendimiento: Inserción masiva de 500+ artículos en inventario")
    public void testInsercionMasivaInventario() {
        // Arrange
        long startTime = System.currentTimeMillis();
        
        // Act: Insertar 500 artículos
        for (int i = 1; i <= BULK_SIZE; i++) {
            Item item = new Item(
                "Artículo-" + i,
                "Tipo-" + (i % 10), // 10 tipos diferentes
                (i % 100) + 1       // Cantidades entre 1 y 100
            );
            inventarioDAO.addItem(item);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertEquals(BULK_SIZE, inventarioDAO.getInventario().size(),
            "Deben haberse insertado " + BULK_SIZE + " artículos");
        
        assertTrue(duration < MAX_ACCEPTABLE_TIME_MS,
            "La inserción de " + BULK_SIZE + " artículos debe completarse en menos de " + 
            MAX_ACCEPTABLE_TIME_MS + "ms. Tiempo real: " + duration + "ms");
        
        System.out.println("Tiempo de inserción de " + BULK_SIZE + " artículos: " + duration + "ms");
    }
    
    @Test
    @DisplayName("Rendimiento: Inserción masiva de 500+ grúas")
    public void testInsercionMasivaGruas() {
        // Arrange
        long startTime = System.currentTimeMillis();
        
        // Act: Insertar 500 grúas
        for (int i = 1; i <= BULK_SIZE; i++) {
            Grua grua = new Grua(
                "Grúa-" + String.format("%04d", i),
                String.format("%.4f, %.4f", 9.9281 + (i * 0.0001), -84.0907 + (i * 0.0001)),
                i % 2 == 0 ? "Disponible" : "En servicio"
            );
            gruaDAO.registrarGrua(grua);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertEquals(BULK_SIZE, gruaDAO.obtenerGruas().size(),
            "Deben haberse registrado " + BULK_SIZE + " grúas");
        
        assertTrue(duration < MAX_ACCEPTABLE_TIME_MS,
            "El registro de " + BULK_SIZE + " grúas debe completarse en menos de " + 
            MAX_ACCEPTABLE_TIME_MS + "ms. Tiempo real: " + duration + "ms");
        
        System.out.println("Tiempo de registro de " + BULK_SIZE + " grúas: " + duration + "ms");
    }
    
    @Test
    @DisplayName("Rendimiento: Inserción masiva de 500+ pólizas")
    public void testInsercionMasivaPolizas() {
        // Arrange
        Date hoy = new Date();
        long startTime = System.currentTimeMillis();
        
        // Act: Insertar 500 pólizas
        for (int i = 1; i <= BULK_SIZE; i++) {
            Poliza poliza = new Poliza(
                "Seguro-" + (i % 10), // 10 tipos de seguros diferentes
                "PLACA-" + String.format("%04d", i),
                hoy,
                agregarDias(hoy, (i % 365) + 30) // Vencimientos variados
            );
            polizaDAO.registrarPoliza(poliza);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertEquals(BULK_SIZE, polizaDAO.obtenerTodasPolizas().size(),
            "Deben haberse registrado " + BULK_SIZE + " pólizas");
        
        assertTrue(duration < MAX_ACCEPTABLE_TIME_MS,
            "El registro de " + BULK_SIZE + " pólizas debe completarse en menos de " + 
            MAX_ACCEPTABLE_TIME_MS + "ms. Tiempo real: " + duration + "ms");
        
        System.out.println("Tiempo de registro de " + BULK_SIZE + " pólizas: " + duration + "ms");
    }
    
    @Test
    @DisplayName("Rendimiento: Búsqueda en inventario con 500+ registros")
    public void testBusquedaEnInventarioGrande() {
        // Arrange: Insertar 500 artículos
        for (int i = 1; i <= BULK_SIZE; i++) {
            Item item = new Item("Artículo-" + i, "Tipo-" + (i % 10), (i % 100) + 1);
            inventarioDAO.addItem(item);
        }
        
        long startTime = System.currentTimeMillis();
        
        // Act: Obtener el inventario completo
        var inventario = inventarioDAO.getInventario();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertEquals(BULK_SIZE, inventario.size());
        assertTrue(duration < 100,
            "La búsqueda en inventario debe ser rápida (< 100ms). Tiempo real: " + duration + "ms");
        
        System.out.println("Tiempo de obtención de inventario con " + BULK_SIZE + " items: " + duration + "ms");
    }
    
    @Test
    @DisplayName("Rendimiento: Actualización masiva de ubicaciones de grúas")
    public void testActualizacionMasivaUbicaciones() {
        // Arrange: Registrar 500 grúas
        for (int i = 1; i <= BULK_SIZE; i++) {
            Grua grua = new Grua("Grúa-" + i, "9.9281, -84.0907", "Disponible");
            gruaDAO.registrarGrua(grua);
        }
        
        long startTime = System.currentTimeMillis();
        
        // Act: Actualizar ubicación de todas las grúas
        var gruas = gruaDAO.obtenerGruas();
        for (int i = 0; i < gruas.size(); i++) {
            Grua grua = gruas.get(i);
            gruaDAO.actualizarUbicacion(
                grua.getId(), 
                String.format("%.4f, %.4f", 9.9350 + (i * 0.0001), -84.0850 + (i * 0.0001))
            );
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertTrue(duration < MAX_ACCEPTABLE_TIME_MS,
            "La actualización masiva de ubicaciones debe completarse en menos de " + 
            MAX_ACCEPTABLE_TIME_MS + "ms. Tiempo real: " + duration + "ms");
        
        System.out.println("Tiempo de actualización de " + BULK_SIZE + " ubicaciones: " + duration + "ms");
    }
    
    @Test
    @DisplayName("Rendimiento: Obtener pólizas próximas a vencer con 500+ registros")
    public void testBusquedaPolizasProximasConMuchosRegistros() {
        // Arrange: Insertar 500 pólizas con diferentes vencimientos
        Date hoy = new Date();
        int polizasProximasEsperadas = 0;
        
        for (int i = 1; i <= BULK_SIZE; i++) {
            int diasParaVencer = (i % 60) + 1; // Vencimientos entre 1 y 60 días
            if (diasParaVencer <= 30) {
                polizasProximasEsperadas++;
            }
            
            Poliza poliza = new Poliza(
                "Seguro-" + i,
                "PLACA-" + i,
                hoy,
                agregarDias(hoy, diasParaVencer)
            );
            polizaDAO.registrarPoliza(poliza);
        }
        
        long startTime = System.currentTimeMillis();
        
        // Act: Buscar pólizas próximas a vencer
        var polizasProximas = polizaDAO.obtenerPolizasProximasAVencer();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertTrue(polizasProximas.size() > 0,
            "Debe haber pólizas próximas a vencer");
        assertTrue(duration < 1000,
            "La búsqueda debe completarse en menos de 1 segundo. Tiempo real: " + duration + "ms");
        
        System.out.println("Tiempo de búsqueda de pólizas próximas con " + BULK_SIZE + 
            " registros: " + duration + "ms. Encontradas: " + polizasProximas.size());
    }
    
    @Test
    @DisplayName("Rendimiento: Uso masivo de artículos del inventario")
    public void testUsoMasivoArticulos() {
        // Arrange: Insertar 500 artículos con stock alto
        for (int i = 1; i <= BULK_SIZE; i++) {
            Item item = new Item("Artículo-" + i, "Tipo-" + (i % 10), 1000);
            inventarioDAO.addItem(item);
        }
        
        long startTime = System.currentTimeMillis();
        
        // Act: Usar una cantidad de cada artículo
        var inventario = inventarioDAO.getInventario();
        for (Item item : inventario) {
            inventarioDAO.useItem(item.getId(), 10);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertTrue(duration < MAX_ACCEPTABLE_TIME_MS,
            "El uso masivo de artículos debe completarse en menos de " + 
            MAX_ACCEPTABLE_TIME_MS + "ms. Tiempo real: " + duration + "ms");
        
        // Verificar que todos los artículos se redujeron correctamente
        for (Item item : inventario) {
            assertEquals(990, item.getQuantity(),
                "Cada artículo debe tener 990 unidades (1000 - 10)");
        }
        
        System.out.println("Tiempo de uso de " + BULK_SIZE + " artículos: " + duration + "ms");
    }
    
    @Test
    @DisplayName("Rendimiento: Agregar rutas masivamente al historial")
    public void testAgregarRutasMasivamente() {
        // Arrange: Registrar 100 grúas
        int numGruas = 100;
        for (int i = 1; i <= numGruas; i++) {
            Grua grua = new Grua("Grúa-" + i, "9.9281, -84.0907", "Disponible");
            gruaDAO.registrarGrua(grua);
        }
        
        long startTime = System.currentTimeMillis();
        
        // Act: Agregar 5 rutas a cada grúa (500 operaciones totales)
        var gruas = gruaDAO.obtenerGruas();
        for (Grua grua : gruas) {
            for (int j = 1; j <= 5; j++) {
                gruaDAO.agregarRuta(grua.getId(), "Ruta-" + j + ": Origen-" + j + " -> Destino-" + j);
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Assert
        assertTrue(duration < MAX_ACCEPTABLE_TIME_MS,
            "Agregar 500 rutas debe completarse en menos de " + 
            MAX_ACCEPTABLE_TIME_MS + "ms. Tiempo real: " + duration + "ms");
        
        // Verificar que las rutas se agregaron
        for (Grua grua : gruas) {
            assertTrue(grua.getHistorialRuta().contains("Ruta-1"),
                "Cada grúa debe tener rutas en su historial");
        }
        
        System.out.println("Tiempo de agregar 500 rutas (5 por grúa x 100 grúas): " + duration + "ms");
    }
    
    /**
     * Método auxiliar para agregar días a una fecha
     */
    private Date agregarDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DAY_OF_MONTH, dias);
        return cal.getTime();
    }
}
