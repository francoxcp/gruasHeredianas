package gruasheredianas.grua.dao;

import gruasheredianas.grua.model.Grua;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Pruebas unitarias para la clase GruaDAO
 * @author franc
 */
public class GruaDAOTest {
    
    private GruaDAO gruaDAO;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    
    @BeforeEach
    public void setUp() {
        gruaDAO = new GruaDAO();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    @Test
    @DisplayName("Test registrar grúa")
    public void testRegistrarGrua() {
        // Arrange
        Grua grua = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        
        // Act
        gruaDAO.registrarGrua(grua);
        
        // Assert
        assertEquals(1, gruaDAO.obtenerGruas().size(),
            "Debe haber 1 grúa registrada");
        assertEquals(grua, gruaDAO.obtenerGruas().get(0),
            "La grúa registrada debe estar en la lista");
        assertTrue(outputStreamCaptor.toString().contains("Grúa registrada exitosamente"),
            "Debe mostrar mensaje de registro exitoso");
    }
    
    @Test
    @DisplayName("Test registrar múltiples grúas")
    public void testRegistrarMultiplesGruas() {
        // Arrange
        Grua grua1 = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        Grua grua2 = new Grua("Grúa-002", "9.9330, -84.0800", "En servicio");
        Grua grua3 = new Grua("Grúa-003", "9.9250, -84.0950", "Disponible");
        
        // Act
        gruaDAO.registrarGrua(grua1);
        gruaDAO.registrarGrua(grua2);
        gruaDAO.registrarGrua(grua3);
        
        // Assert
        assertEquals(3, gruaDAO.obtenerGruas().size(),
            "Deben haber 3 grúas registradas");
    }
    
    @Test
    @DisplayName("Test actualizar ubicación de grúa")
    public void testActualizarUbicacion() {
        // Arrange
        Grua grua = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        gruaDAO.registrarGrua(grua);
        int gruaId = grua.getId();
        String nuevaUbicacion = "9.9350, -84.0850";
        
        // Act
        outputStreamCaptor.reset();
        gruaDAO.actualizarUbicacion(gruaId, nuevaUbicacion);
        
        // Assert
        assertEquals(nuevaUbicacion, grua.getUbicacionActual(),
            "La ubicación debe actualizarse correctamente");
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Ubicación actualizada"),
            "Debe mostrar mensaje de actualización");
        assertTrue(output.contains("Grúa-001"),
            "El mensaje debe incluir el nombre de la grúa");
    }
    
    @Test
    @DisplayName("Test actualizar ubicación de grúa no encontrada")
    public void testActualizarUbicacionGruaNoEncontrada() {
        // Arrange
        outputStreamCaptor.reset();
        
        // Act
        gruaDAO.actualizarUbicacion(999, "9.9350, -84.0850");
        
        // Assert
        assertTrue(outputStreamCaptor.toString().contains("Grúa no encontrada"),
            "Debe mostrar mensaje de grúa no encontrada");
    }
    
    @Test
    @DisplayName("Test agregar ruta al historial")
    public void testAgregarRuta() {
        // Arrange
        Grua grua = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        gruaDAO.registrarGrua(grua);
        int gruaId = grua.getId();
        String ruta1 = "Heredia -> San José";
        
        // Act
        outputStreamCaptor.reset();
        gruaDAO.agregarRuta(gruaId, ruta1);
        
        // Assert
        assertTrue(grua.getHistorialRuta().contains(ruta1),
            "El historial debe contener la ruta agregada");
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Ruta añadida al historial"),
            "Debe mostrar mensaje de confirmación");
        assertTrue(output.contains("Grúa-001"),
            "El mensaje debe incluir el nombre de la grúa");
    }
    
    @Test
    @DisplayName("Test agregar múltiples rutas al historial")
    public void testAgregarMultiplesRutas() {
        // Arrange
        Grua grua = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        gruaDAO.registrarGrua(grua);
        int gruaId = grua.getId();
        String ruta1 = "Heredia -> San José";
        String ruta2 = "San José -> Alajuela";
        String ruta3 = "Alajuela -> Cartago";
        
        // Act
        gruaDAO.agregarRuta(gruaId, ruta1);
        gruaDAO.agregarRuta(gruaId, ruta2);
        gruaDAO.agregarRuta(gruaId, ruta3);
        
        // Assert
        String historial = grua.getHistorialRuta();
        assertTrue(historial.contains(ruta1), "El historial debe contener ruta1");
        assertTrue(historial.contains(ruta2), "El historial debe contener ruta2");
        assertTrue(historial.contains(ruta3), "El historial debe contener ruta3");
    }
    
    @Test
    @DisplayName("Test agregar ruta a grúa no encontrada")
    public void testAgregarRutaGruaNoEncontrada() {
        // Arrange
        outputStreamCaptor.reset();
        
        // Act
        gruaDAO.agregarRuta(999, "Ruta de prueba");
        
        // Assert
        assertTrue(outputStreamCaptor.toString().contains("Grúa no encontrada"),
            "Debe mostrar mensaje de grúa no encontrada");
    }
    
    @Test
    @DisplayName("Test obtener lista vacía de grúas")
    public void testObtenerGruasVacio() {
        // Act
        var gruas = gruaDAO.obtenerGruas();
        
        // Assert
        assertNotNull(gruas, "La lista no debe ser null");
        assertEquals(0, gruas.size(), "La lista debe estar vacía");
    }
    
    @Test
    @DisplayName("Test obtener lista de grúas")
    public void testObtenerGruas() {
        // Arrange
        Grua grua1 = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        Grua grua2 = new Grua("Grúa-002", "9.9330, -84.0800", "En servicio");
        gruaDAO.registrarGrua(grua1);
        gruaDAO.registrarGrua(grua2);
        
        // Act
        var gruas = gruaDAO.obtenerGruas();
        
        // Assert
        assertNotNull(gruas);
        assertEquals(2, gruas.size());
        assertTrue(gruas.contains(grua1));
        assertTrue(gruas.contains(grua2));
    }
    
    @Test
    @DisplayName("Test actualización secuencial de ubicación")
    public void testActualizacionSecuencialUbicacion() {
        // Arrange
        Grua grua = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        gruaDAO.registrarGrua(grua);
        int gruaId = grua.getId();
        
        // Act
        gruaDAO.actualizarUbicacion(gruaId, "9.9300, -84.0850");
        gruaDAO.actualizarUbicacion(gruaId, "9.9320, -84.0820");
        gruaDAO.actualizarUbicacion(gruaId, "9.9340, -84.0800");
        
        // Assert
        assertEquals("9.9340, -84.0800", grua.getUbicacionActual(),
            "La ubicación final debe ser la última actualizada");
    }
    
    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
