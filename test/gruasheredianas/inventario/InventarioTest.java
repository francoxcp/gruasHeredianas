package gruasheredianas.inventario;

import gruasheredianas.inventario.model.ArticuloInventario;
import java.util.Date;

/**
 * Pruebas unitarias para el módulo de inventario.
 * 
 * @author franco
 */
public class InventarioTest {
    
    /**
     * Prueba la creación de un artículo de inventario.
     */
    public static void testCrearArticulo() {
        System.out.println("\n=== Test: Crear Artículo ===");
        
        ArticuloInventario articulo = new ArticuloInventario();
        articulo.setNombre("Filtro de aceite");
        articulo.setTipo("REPUESTO");
        articulo.setDescripcion("Filtro de aceite para motor diesel");
        articulo.setCantidad(15);
        articulo.setStockMinimo(5);
        articulo.setUbicacion("Almacén A-12");
        articulo.setPrecioUnitario(25.50);
        articulo.setFechaRegistro(new Date());
        articulo.setFechaUltimaActualizacion(new Date());
        
        assert articulo.getNombre().equals("Filtro de aceite");
        assert articulo.getTipo().equals("REPUESTO");
        assert articulo.getCantidad() == 15;
        assert articulo.getStockMinimo() == 5;
        
        System.out.println("✓ Artículo creado correctamente");
        System.out.println("  Nombre: " + articulo.getNombre());
        System.out.println("  Tipo: " + articulo.getTipo());
        System.out.println("  Cantidad: " + articulo.getCantidad());
    }
    
    /**
     * Prueba la verificación de stock bajo.
     */
    public static void testVerificarStockBajo() {
        System.out.println("\n=== Test: Verificar Stock Bajo ===");
        
        ArticuloInventario articulo = new ArticuloInventario();
        articulo.setCantidad(3);
        articulo.setStockMinimo(5);
        
        boolean stockBajo = articulo.tieneStockBajo();
        assert stockBajo == true;
        
        System.out.println("✓ Stock bajo detectado correctamente");
        System.out.println("  Cantidad: " + articulo.getCantidad());
        System.out.println("  Stock mínimo: " + articulo.getStockMinimo());
        System.out.println("  Tiene stock bajo: " + stockBajo);
    }
    
    /**
     * Prueba el cálculo del valor total.
     */
    public static void testCalcularValorTotal() {
        System.out.println("\n=== Test: Calcular Valor Total ===");
        
        ArticuloInventario articulo = new ArticuloInventario();
        articulo.setCantidad(10);
        articulo.setPrecioUnitario(25.50);
        
        double valorTotal = articulo.getValorTotal();
        assert valorTotal == 255.0;
        
        System.out.println("✓ Valor total calculado correctamente");
        System.out.println("  Cantidad: " + articulo.getCantidad());
        System.out.println("  Precio unitario: $" + articulo.getPrecioUnitario());
        System.out.println("  Valor total: $" + valorTotal);
    }
    
    /**
     * Ejecuta todas las pruebas del módulo de inventario.
     */
    public static void ejecutarPruebas() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  PRUEBAS UNITARIAS - MÓDULO INVENTARIO ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        try {
            testCrearArticulo();
            testVerificarStockBajo();
            testCalcularValorTotal();
            
            System.out.println("\n✓ Todas las pruebas del módulo de inventario pasaron exitosamente");
        } catch (AssertionError e) {
            System.out.println("\n✗ Error en las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ejecutarPruebas();
    }
}
