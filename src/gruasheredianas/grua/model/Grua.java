package gruasheredianas.grua.model;

/**
 *
 * @author franc
 */
public class Grua {
    private int id;
    private String nombre;
    private String ubicacionActual; // Ejemplo: "Latitud, Longitud"
    private String estado; // Ejemplo: "Disponible", "En servicio"
    private String historialRuta; // Ejemplo: JSON o Cadena para rutas
    private static int nextId = 1; 

    public Grua(String nombre, String ubicacionActual, String estado) {
        this.id = nextId++;
        this.nombre = nombre;
        this.ubicacionActual = ubicacionActual;
        this.estado = estado;
        this.historialRuta = "";
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHistorialRuta() {
        return historialRuta;
    }

    public void actualizarHistorialRuta(String nuevaRuta) {
        this.historialRuta += nuevaRuta + "; "; // Acumula rutas separadas por punto y coma
    }

    @Override
    public String toString() {
        return "Grua ID: " + id + ", Nombre: " + nombre + ", Ubicación Actual: " + ubicacionActual + 
               ", Estado: " + estado + ", Historial de Rutas: " + historialRuta;
    }
    
}
