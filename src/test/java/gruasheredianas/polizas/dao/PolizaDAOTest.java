package gruasheredianas.polizas.dao;

import gruasheredianas.polizas.model.Poliza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Pruebas unitarias para la clase PolizaDAO
 * @author franc
 */
public class PolizaDAOTest {
    
    private PolizaDAO polizaDAO;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    
    @BeforeEach
    public void setUp() {
        polizaDAO = new PolizaDAO();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    @Test
    @DisplayName("Test registrar póliza")
    public void testRegistrarPoliza() {
        // Arrange
        Date fechaInicio = new Date();
        Date fechaVencimiento = agregarDias(fechaInicio, 365);
        Poliza poliza = new Poliza("Seguro Todo Riesgo", "ABC-123", fechaInicio, fechaVencimiento);
        
        // Act
        polizaDAO.registrarPoliza(poliza);
        
        // Assert
        assertEquals(1, polizaDAO.obtenerTodasPolizas().size(),
            "Debe haber 1 póliza registrada");
        assertEquals(poliza, polizaDAO.obtenerTodasPolizas().get(0),
            "La póliza registrada debe estar en la lista");
        assertTrue(outputStreamCaptor.toString().contains("Póliza registrada exitosamente"),
            "Debe mostrar mensaje de registro exitoso");
    }
    
    @Test
    @DisplayName("Test registrar múltiples pólizas")
    public void testRegistrarMultiplesPolizas() {
        // Arrange
        Date hoy = new Date();
        Poliza poliza1 = new Poliza("Seguro Todo Riesgo", "ABC-123", hoy, agregarDias(hoy, 365));
        Poliza poliza2 = new Poliza("Seguro Básico", "DEF-456", hoy, agregarDias(hoy, 180));
        Poliza poliza3 = new Poliza("Seguro Premium", "GHI-789", hoy, agregarDias(hoy, 730));
        
        // Act
        polizaDAO.registrarPoliza(poliza1);
        polizaDAO.registrarPoliza(poliza2);
        polizaDAO.registrarPoliza(poliza3);
        
        // Assert
        assertEquals(3, polizaDAO.obtenerTodasPolizas().size(),
            "Deben haber 3 pólizas registradas");
    }
    
    @Test
    @DisplayName("Test obtener pólizas próximas a vencer (dentro de 30 días)")
    public void testObtenerPolizasProximasAVencer() {
        // Arrange
        Date hoy = new Date();
        // Póliza que vence en 20 días (debe estar en la lista)
        Poliza poliza1 = new Poliza("Seguro 1", "ABC-123", hoy, agregarDias(hoy, 20));
        // Póliza que vence en 45 días (NO debe estar en la lista)
        Poliza poliza2 = new Poliza("Seguro 2", "DEF-456", hoy, agregarDias(hoy, 45));
        // Póliza que vence en 5 días (debe estar en la lista)
        Poliza poliza3 = new Poliza("Seguro 3", "GHI-789", hoy, agregarDias(hoy, 5));
        
        polizaDAO.registrarPoliza(poliza1);
        polizaDAO.registrarPoliza(poliza2);
        polizaDAO.registrarPoliza(poliza3);
        
        // Act
        ArrayList<Poliza> proximasAVencer = polizaDAO.obtenerPolizasProximasAVencer();
        
        // Assert
        assertEquals(2, proximasAVencer.size(),
            "Deben haber 2 pólizas próximas a vencer");
        assertTrue(proximasAVencer.contains(poliza1),
            "Debe incluir la póliza que vence en 20 días");
        assertTrue(proximasAVencer.contains(poliza3),
            "Debe incluir la póliza que vence en 5 días");
        assertFalse(proximasAVencer.contains(poliza2),
            "No debe incluir la póliza que vence en 45 días");
    }
    
    @Test
    @DisplayName("Test obtener pólizas próximas a vencer (lista vacía)")
    public void testObtenerPolizasProximasAVencerVacio() {
        // Arrange
        Date hoy = new Date();
        // Todas las pólizas vencen después de 30 días
        Poliza poliza1 = new Poliza("Seguro 1", "ABC-123", hoy, agregarDias(hoy, 60));
        Poliza poliza2 = new Poliza("Seguro 2", "DEF-456", hoy, agregarDias(hoy, 90));
        
        polizaDAO.registrarPoliza(poliza1);
        polizaDAO.registrarPoliza(poliza2);
        
        // Act
        ArrayList<Poliza> proximasAVencer = polizaDAO.obtenerPolizasProximasAVencer();
        
        // Assert
        assertEquals(0, proximasAVencer.size(),
            "No debe haber pólizas próximas a vencer");
    }
    
    @Test
    @DisplayName("Test póliza vencida no se incluye en próximas a vencer")
    public void testPolizaVencidaNoIncluidaEnProximas() {
        // Arrange
        Date hoy = new Date();
        // Póliza que venció hace 5 días
        Poliza polizaVencida = new Poliza("Seguro Vencido", "ABC-123", 
            agregarDias(hoy, -365), agregarDias(hoy, -5));
        // Póliza que vence en 10 días
        Poliza polizaProxima = new Poliza("Seguro Próximo", "DEF-456", 
            hoy, agregarDias(hoy, 10));
        
        polizaDAO.registrarPoliza(polizaVencida);
        polizaDAO.registrarPoliza(polizaProxima);
        
        // Act
        ArrayList<Poliza> proximasAVencer = polizaDAO.obtenerPolizasProximasAVencer();
        
        // Assert
        assertEquals(1, proximasAVencer.size(),
            "Solo debe haber 1 póliza próxima a vencer");
        assertFalse(proximasAVencer.contains(polizaVencida),
            "No debe incluir pólizas vencidas");
        assertTrue(proximasAVencer.contains(polizaProxima),
            "Debe incluir póliza próxima a vencer");
    }
    
    @Test
    @DisplayName("Test obtener todas las pólizas (lista vacía)")
    public void testObtenerTodasPolizasVacio() {
        // Act
        ArrayList<Poliza> polizas = polizaDAO.obtenerTodasPolizas();
        
        // Assert
        assertNotNull(polizas, "La lista no debe ser null");
        assertEquals(0, polizas.size(), "La lista debe estar vacía");
    }
    
    @Test
    @DisplayName("Test obtener todas las pólizas")
    public void testObtenerTodasPolizas() {
        // Arrange
        Date hoy = new Date();
        Poliza poliza1 = new Poliza("Seguro 1", "ABC-123", hoy, agregarDias(hoy, 20));
        Poliza poliza2 = new Poliza("Seguro 2", "DEF-456", hoy, agregarDias(hoy, 45));
        Poliza poliza3 = new Poliza("Seguro 3", "GHI-789", hoy, agregarDias(hoy, 365));
        
        polizaDAO.registrarPoliza(poliza1);
        polizaDAO.registrarPoliza(poliza2);
        polizaDAO.registrarPoliza(poliza3);
        
        // Act
        ArrayList<Poliza> polizas = polizaDAO.obtenerTodasPolizas();
        
        // Assert
        assertNotNull(polizas);
        assertEquals(3, polizas.size(), "Deben haber 3 pólizas en total");
        assertTrue(polizas.contains(poliza1));
        assertTrue(polizas.contains(poliza2));
        assertTrue(polizas.contains(poliza3));
    }
    
    @Test
    @DisplayName("Test póliza exactamente en el límite de 30 días")
    public void testPolizaEnLimite30Dias() {
        // Arrange
        Date hoy = new Date();
        Poliza poliza30Dias = new Poliza("Seguro Límite", "ABC-123", 
            hoy, agregarDias(hoy, 30));
        polizaDAO.registrarPoliza(poliza30Dias);
        
        // Act
        ArrayList<Poliza> proximasAVencer = polizaDAO.obtenerPolizasProximasAVencer();
        
        // Assert
        assertEquals(1, proximasAVencer.size(),
            "La póliza que vence en exactamente 30 días debe incluirse");
        assertTrue(proximasAVencer.contains(poliza30Dias));
    }
    
    @Test
    @DisplayName("Test póliza con múltiples grúas asociadas")
    public void testPolizasConDiferentesGruas() {
        // Arrange
        Date hoy = new Date();
        Poliza poliza1 = new Poliza("Seguro 1", "ABC-123", hoy, agregarDias(hoy, 15));
        Poliza poliza2 = new Poliza("Seguro 2", "DEF-456", hoy, agregarDias(hoy, 15));
        Poliza poliza3 = new Poliza("Seguro 3", "ABC-123", hoy, agregarDias(hoy, 15));
        
        polizaDAO.registrarPoliza(poliza1);
        polizaDAO.registrarPoliza(poliza2);
        polizaDAO.registrarPoliza(poliza3);
        
        // Act
        ArrayList<Poliza> todasPolizas = polizaDAO.obtenerTodasPolizas();
        
        // Assert
        assertEquals(3, todasPolizas.size(),
            "Pueden existir múltiples pólizas para la misma grúa");
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
    
    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
