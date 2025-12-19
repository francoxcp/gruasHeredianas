package gruasheredianas.integration;

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
 * Pruebas de integración para validar la interacción entre módulos
 * @author franc
 */
public class ModulosIntegracionTest {
    
    private InventarioDAO inventarioDAO;
    private GruaDAO gruaDAO;
    private PolizaDAO polizaDAO;
    
    @BeforeEach
    public void setUp() {
        inventarioDAO = new InventarioDAO();
        gruaDAO = new GruaDAO();
        polizaDAO = new PolizaDAO();
    }
    
    @Test
    @DisplayName("Integración: Inventario → Grúas - Artículos disponibles para uso por grúas")
    public void testInventarioGruasIntegracion() {
        // Arrange: Crear inventario con artículos
        Item llanta = new Item("Llanta de Repuesto", "Repuesto", 10);
        Item aceite = new Item("Aceite de Motor", "Lubricante", 20);
        Item cable = new Item("Cable de Remolque", "Herramienta", 5);
        
        inventarioDAO.addItem(llanta);
        inventarioDAO.addItem(aceite);
        inventarioDAO.addItem(cable);
        
        // Arrange: Registrar grúas
        Grua grua1 = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        Grua grua2 = new Grua("Grúa-002", "9.9330, -84.0800", "En servicio");
        
        gruaDAO.registrarGrua(grua1);
        gruaDAO.registrarGrua(grua2);
        
        // Act: Simular uso de artículos por las grúas
        // Grúa 1 usa 2 llantas y 3 litros de aceite
        inventarioDAO.useItem(llanta.getId(), 2);
        inventarioDAO.useItem(aceite.getId(), 3);
        
        // Grúa 2 usa 1 cable y 2 litros de aceite
        inventarioDAO.useItem(cable.getId(), 1);
        inventarioDAO.useItem(aceite.getId(), 2);
        
        // Assert: Verificar que el inventario se actualizó correctamente
        assertEquals(8, llanta.getQuantity(),
            "Las llantas deben reducirse de 10 a 8");
        assertEquals(15, aceite.getQuantity(),
            "El aceite debe reducirse de 20 a 15");
        assertEquals(4, cable.getQuantity(),
            "Los cables deben reducirse de 5 a 4");
        
        // Assert: Verificar que las grúas siguen registradas
        assertEquals(2, gruaDAO.obtenerGruas().size(),
            "Las grúas deben seguir registradas");
    }
    
    @Test
    @DisplayName("Integración: Grúas → Pólizas - Asociación correcta de pólizas a grúas")
    public void testGruasPolizasIntegracion() {
        // Arrange: Registrar grúas
        Grua grua1 = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        Grua grua2 = new Grua("Grúa-002", "9.9330, -84.0800", "En servicio");
        Grua grua3 = new Grua("Grúa-003", "9.9250, -84.0950", "Disponible");
        
        gruaDAO.registrarGrua(grua1);
        gruaDAO.registrarGrua(grua2);
        gruaDAO.registrarGrua(grua3);
        
        // Arrange: Crear pólizas asociadas a las grúas
        Date hoy = new Date();
        String placaGrua1 = "ABC-123"; // Placa asociada a Grúa-001
        String placaGrua2 = "DEF-456"; // Placa asociada a Grúa-002
        String placaGrua3 = "GHI-789"; // Placa asociada a Grúa-003
        
        Poliza poliza1 = new Poliza("Seguro Todo Riesgo", placaGrua1, hoy, agregarDias(hoy, 365));
        Poliza poliza2 = new Poliza("Seguro Básico", placaGrua2, hoy, agregarDias(hoy, 180));
        Poliza poliza3 = new Poliza("Seguro Premium", placaGrua3, hoy, agregarDias(hoy, 730));
        
        polizaDAO.registrarPoliza(poliza1);
        polizaDAO.registrarPoliza(poliza2);
        polizaDAO.registrarPoliza(poliza3);
        
        // Act: Verificar asociaciones
        var todasPolizas = polizaDAO.obtenerTodasPolizas();
        var todasGruas = gruaDAO.obtenerGruas();
        
        // Assert: Verificar que cada grúa tenga al menos una póliza asociada
        assertEquals(3, todasGruas.size(), "Deben haber 3 grúas registradas");
        assertEquals(3, todasPolizas.size(), "Deben haber 3 pólizas registradas");
        
        // Verificar que las pólizas contienen las placas correctas
        assertTrue(todasPolizas.stream().anyMatch(p -> p.getPlacaRelacionada().equals(placaGrua1)),
            "Debe existir póliza asociada a la placa de Grúa-001");
        assertTrue(todasPolizas.stream().anyMatch(p -> p.getPlacaRelacionada().equals(placaGrua2)),
            "Debe existir póliza asociada a la placa de Grúa-002");
        assertTrue(todasPolizas.stream().anyMatch(p -> p.getPlacaRelacionada().equals(placaGrua3)),
            "Debe existir póliza asociada a la placa de Grúa-003");
    }
    
    @Test
    @DisplayName("Integración: Múltiples pólizas para una misma grúa")
    public void testMultiplesPolizasParaUnaGrua() {
        // Arrange: Registrar una grúa
        Grua grua = new Grua("Grúa-001", "9.9281, -84.0907", "Disponible");
        gruaDAO.registrarGrua(grua);
        
        // Arrange: Crear múltiples pólizas para la misma grúa
        Date hoy = new Date();
        String placa = "ABC-123";
        
        Poliza polizaResponsabilidadCivil = new Poliza("Responsabilidad Civil", placa, 
            hoy, agregarDias(hoy, 365));
        Poliza polizaTodoRiesgo = new Poliza("Todo Riesgo", placa, 
            hoy, agregarDias(hoy, 365));
        Poliza polizaRobo = new Poliza("Seguro contra Robo", placa, 
            hoy, agregarDias(hoy, 180));
        
        polizaDAO.registrarPoliza(polizaResponsabilidadCivil);
        polizaDAO.registrarPoliza(polizaTodoRiesgo);
        polizaDAO.registrarPoliza(polizaRobo);
        
        // Act: Obtener todas las pólizas
        var todasPolizas = polizaDAO.obtenerTodasPolizas();
        
        // Assert: Verificar que se pueden registrar múltiples pólizas para una grúa
        assertEquals(3, todasPolizas.size(),
            "Deben haber 3 pólizas registradas para la misma grúa");
        
        long polizasParaPlaca = todasPolizas.stream()
            .filter(p -> p.getPlacaRelacionada().equals(placa))
            .count();
        
        assertEquals(3, polizasParaPlaca,
            "Las 3 pólizas deben estar asociadas a la misma placa");
    }
    
    @Test
    @DisplayName("Integración: Flujo completo - Registro de grúa, inventario y póliza")
    public void testFlujoCompletoRegistro() {
        // Arrange & Act: Paso 1 - Registrar grúa
        Grua grua = new Grua("Grúa-Premium-001", "9.9281, -84.0907", "Disponible");
        gruaDAO.registrarGrua(grua);
        
        // Paso 2 - Registrar artículos de inventario para la grúa
        Item llantasGrua = new Item("Llantas para Grúa Premium", "Repuesto", 8);
        Item aceitePremium = new Item("Aceite Premium", "Lubricante", 30);
        
        inventarioDAO.addItem(llantasGrua);
        inventarioDAO.addItem(aceitePremium);
        
        // Paso 3 - Registrar póliza para la grúa
        Date hoy = new Date();
        String placa = "PREM-001";
        Poliza polizaGrua = new Poliza("Seguro Completo Premium", placa, 
            hoy, agregarDias(hoy, 365));
        
        polizaDAO.registrarPoliza(polizaGrua);
        
        // Paso 4 - Actualizar ubicación de la grúa
        gruaDAO.actualizarUbicacion(grua.getId(), "9.9350, -84.0850");
        gruaDAO.agregarRuta(grua.getId(), "Heredia -> San José");
        
        // Paso 5 - Usar artículos del inventario
        inventarioDAO.useItem(llantasGrua.getId(), 2);
        inventarioDAO.useItem(aceitePremium.getId(), 5);
        
        // Assert: Verificar el estado final del sistema
        assertEquals(1, gruaDAO.obtenerGruas().size(), "Debe haber 1 grúa registrada");
        assertEquals(2, inventarioDAO.getInventario().size(), "Debe haber 2 artículos en inventario");
        assertEquals(1, polizaDAO.obtenerTodasPolizas().size(), "Debe haber 1 póliza registrada");
        
        assertEquals("9.9350, -84.0850", grua.getUbicacionActual(),
            "La ubicación de la grúa debe estar actualizada");
        assertTrue(grua.getHistorialRuta().contains("Heredia -> San José"),
            "El historial debe contener la ruta");
        
        assertEquals(6, llantasGrua.getQuantity(), "Las llantas deben reducirse a 6");
        assertEquals(25, aceitePremium.getQuantity(), "El aceite debe reducirse a 25");
        
        assertEquals(placa, polizaGrua.getPlacaRelacionada(),
            "La póliza debe estar asociada a la placa correcta");
    }
    
    @Test
    @DisplayName("Integración: Alerta de inventario bajo durante operación de grúa")
    public void testAlertaInventarioBajoDuranteOperacion() {
        // Arrange: Crear inventario con stock bajo
        Item itemBajoStock = new Item("Filtro de Aceite", "Repuesto", 7);
        inventarioDAO.addItem(itemBajoStock);
        
        // Arrange: Registrar grúa
        Grua grua = new Grua("Grúa-001", "9.9281, -84.0907", "En servicio");
        gruaDAO.registrarGrua(grua);
        
        // Act: Usar artículo hasta que genere alerta (threshold es 5)
        inventarioDAO.useItem(itemBajoStock.getId(), 3); // Quedará en 4
        
        // Assert: Verificar que el item tiene stock bajo
        assertTrue(itemBajoStock.getQuantity() < 5,
            "El artículo debe tener menos de 5 unidades");
        assertEquals(4, itemBajoStock.getQuantity(),
            "El artículo debe tener exactamente 4 unidades");
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
