package gruasheredianas.inventario.dao;
import gruasheredianas.inventario.model.Item;
import java.util.ArrayList;
/**
 *
 * @author franc
 */
public class InventarioDAO {

    private ArrayList<Item> inventario = new ArrayList<>();
    private static final int LOW_STOCK_THRESHOLD = 5;

    /**
     * Método para agregar un artículo al inventario
     * 
     * @param item Objeto Item que será agregado al inventario
     */
    public void addItem(Item item) {
        inventario.add(item);
        System.out.println("Artículo agregado exitosamente.");
    }

    /**
     * Método para usar un artículo del inventario
     * 
     * @param id     Identificador del artículo
     * @param amount Cantidad a usar
     */
    public void useItem(int id, int amount) {
        for (Item item : inventario) {
            if (item.getId() == id) {
                item.useQuantity(amount);
                checkLowStock(item);
                return;
            }
        }
        System.out.println("Artículo no encontrado.");
    }

    /**
     * Método para reabastecer la cantidad de un artículo en el inventario
     * 
     * @param id     Identificador del artículo
     * @param amount Cantidad a añadir
     */
    public void replenishItem(int id, int amount) {
        for (Item item : inventario) {
            if (item.getId() == id) {
                item.addQuantity(amount);
                System.out.println("Artículo reabastecido exitosamente.");
                return;
            }
        }
        System.out.println("Artículo no encontrado.");
    }

    /**
     * Método para obtener la lista completa del inventario
     * 
     * @return Lista de objetos Item en el inventario
     */
    public ArrayList<Item> getInventario() {
        return inventario;
    }

    /**
     * Método para verificar si un artículo tiene existencias bajas
     * 
     * @param item Objeto Item que debe ser revisado
     */
    private void checkLowStock(Item item) {
        if (item.getQuantity() < LOW_STOCK_THRESHOLD) {
            System.out.println("¡Advertencia: El artículo \"" + item.getName() + "\" tiene existencias bajas!");
        }
    }
}
