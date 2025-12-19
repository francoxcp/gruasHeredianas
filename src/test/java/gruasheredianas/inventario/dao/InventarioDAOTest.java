package gruasheredianas.inventario.dao;

import gruasheredianas.inventario.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Pruebas unitarias para la clase InventarioDAO
 * @author franc
 */
public class InventarioDAOTest {
    
    private InventarioDAO inventarioDAO;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    
    @BeforeEach
    public void setUp() {
        inventarioDAO = new InventarioDAO();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    @Test
    @DisplayName("Test agregar artículo al inventario")
    public void testAddItem() {
        // Arrange
        Item item = new Item("Llanta", "Repuesto", 10);
        
        // Act
        inventarioDAO.addItem(item);
        
        // Assert
        assertEquals(1, inventarioDAO.getInventario().size(), 
            "El inventario debe contener 1 artículo");
        assertEquals(item, inventarioDAO.getInventario().get(0), 
            "El artículo agregado debe estar en el inventario");
        assertTrue(outputStreamCaptor.toString().contains("Artículo agregado exitosamente"),
            "Debe mostrar mensaje de éxito");
    }
    
    @Test
    @DisplayName("Test agregar múltiples artículos")
    public void testAddMultipleItems() {
        // Arrange
        Item item1 = new Item("Llanta", "Repuesto", 10);
        Item item2 = new Item("Aceite", "Lubricante", 20);
        Item item3 = new Item("Filtro", "Repuesto", 15);
        
        // Act
        inventarioDAO.addItem(item1);
        inventarioDAO.addItem(item2);
        inventarioDAO.addItem(item3);
        
        // Assert
        assertEquals(3, inventarioDAO.getInventario().size(),
            "El inventario debe contener 3 artículos");
    }
    
    @Test
    @DisplayName("Test usar artículo del inventario")
    public void testUseItem() {
        // Arrange
        Item item = new Item("Llanta", "Repuesto", 10);
        inventarioDAO.addItem(item);
        int itemId = item.getId();
        
        // Act
        outputStreamCaptor.reset(); // Limpiar buffer
        inventarioDAO.useItem(itemId, 3);
        
        // Assert
        assertEquals(7, item.getQuantity(), 
            "La cantidad debe reducirse de 10 a 7");
    }
    
    @Test
    @DisplayName("Test usar artículo que genera alerta de stock bajo")
    public void testUseItemLowStockAlert() {
        // Arrange
        Item item = new Item("Llanta", "Repuesto", 10);
        inventarioDAO.addItem(item);
        int itemId = item.getId();
        
        // Act
        outputStreamCaptor.reset();
        inventarioDAO.useItem(itemId, 7); // Quedará con 3, menos que el threshold de 5
        
        // Assert
        assertEquals(3, item.getQuantity());
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Advertencia") || output.contains("advertencia"),
            "Debe mostrar advertencia de stock bajo");
        assertTrue(output.contains("Llanta"),
            "La advertencia debe mencionar el nombre del artículo");
    }
    
    @Test
    @DisplayName("Test usar artículo no encontrado")
    public void testUseItemNotFound() {
        // Arrange - no agregar ningún artículo
        outputStreamCaptor.reset();
        
        // Act
        inventarioDAO.useItem(999, 5);
        
        // Assert
        assertTrue(outputStreamCaptor.toString().contains("Artículo no encontrado"),
            "Debe mostrar mensaje de artículo no encontrado");
    }
    
    @Test
    @DisplayName("Test reabastecer artículo")
    public void testReplenishItem() {
        // Arrange
        Item item = new Item("Aceite", "Lubricante", 5);
        inventarioDAO.addItem(item);
        int itemId = item.getId();
        
        // Act
        outputStreamCaptor.reset();
        inventarioDAO.replenishItem(itemId, 10);
        
        // Assert
        assertEquals(15, item.getQuantity(),
            "La cantidad debe aumentar de 5 a 15");
        assertTrue(outputStreamCaptor.toString().contains("Artículo reabastecido exitosamente"),
            "Debe mostrar mensaje de reabastecimiento exitoso");
    }
    
    @Test
    @DisplayName("Test reabastecer artículo no encontrado")
    public void testReplenishItemNotFound() {
        // Arrange
        outputStreamCaptor.reset();
        
        // Act
        inventarioDAO.replenishItem(999, 10);
        
        // Assert
        assertTrue(outputStreamCaptor.toString().contains("Artículo no encontrado"),
            "Debe mostrar mensaje de artículo no encontrado");
    }
    
    @Test
    @DisplayName("Test obtener inventario vacío")
    public void testGetInventarioEmpty() {
        // Act
        var inventario = inventarioDAO.getInventario();
        
        // Assert
        assertNotNull(inventario, "El inventario no debe ser null");
        assertEquals(0, inventario.size(), "El inventario debe estar vacío");
    }
    
    @Test
    @DisplayName("Test obtener inventario con artículos")
    public void testGetInventario() {
        // Arrange
        Item item1 = new Item("Llanta", "Repuesto", 10);
        Item item2 = new Item("Aceite", "Lubricante", 20);
        inventarioDAO.addItem(item1);
        inventarioDAO.addItem(item2);
        
        // Act
        var inventario = inventarioDAO.getInventario();
        
        // Assert
        assertNotNull(inventario);
        assertEquals(2, inventario.size());
        assertTrue(inventario.contains(item1));
        assertTrue(inventario.contains(item2));
    }
    
    @Test
    @DisplayName("Test usar cantidad mayor a existencias")
    public void testUseItemInsufficientStock() {
        // Arrange
        Item item = new Item("Filtro", "Repuesto", 5);
        inventarioDAO.addItem(item);
        int itemId = item.getId();
        
        // Act
        outputStreamCaptor.reset();
        inventarioDAO.useItem(itemId, 10); // Intentar usar más de lo disponible
        
        // Assert
        assertEquals(5, item.getQuantity(),
            "La cantidad no debe cambiar cuando no hay suficientes existencias");
    }
    
    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }
}
