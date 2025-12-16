package gruasheredianas.inventario.model;

/**
 * Representa un artículo del inventario (repuestos, herramientas, equipos)
 * @author franco
 */
public class Articulo {
    
    private int id;
    private String codigo;
    private String nombre;
    private String categoria; // REPUESTO, HERRAMIENTA, EQUIPO
    private String descripcion;
    private int cantidadActual;
    private int stockMinimo;
    private double precioUnitario;
    private String ubicacion;
    
    public Articulo() {}

    public Articulo(int id, String codigo, String nombre, String categoria, String descripcion, 
                    int cantidadActual, int stockMinimo, double precioUnitario, String ubicacion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.stockMinimo = stockMinimo;
        this.precioUnitario = precioUnitario;
        this.ubicacion = ubicacion;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = cantidadActual; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    /**
     * Verifica si el artículo está por debajo del stock mínimo
     * @return true si requiere reposición
     */
    public boolean requiereReposicion() {
        return cantidadActual <= stockMinimo;
    }
}
