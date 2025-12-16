package gruasheredianas.inventario.model;

/**
 *
 * @author franc
 */
public class Item {
    
    private int id;
    private String name;
    private String type;
    private int quantity;
    private static int nextId = 1; // Generador de identificadores únicos

    public Item(String name, String type, int quantity) {
        this.id = nextId++;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public void useQuantity(int amount) {
        if (amount > this.quantity) {
            System.out.println("No hay suficientes existencias para este artículo.");
        } else {
            this.quantity -= amount;
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nombre: " + name + ", Tipo: " + type + ", Cantidad: " + quantity;
    }
}