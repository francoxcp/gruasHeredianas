package gruasheredianas.inventario.model;

import java.util.Date;

/**
 * Modelo que representa un artículo en el inventario.
 * Puede ser repuesto, herramienta o equipo.
 * 
 * @author franco
 */
public class ArticuloInventario {
    
    private int id;
    private String nombre;
    private String tipo; // "REPUESTO", "HERRAMIENTA", "EQUIPO"
    private String descripcion;
    private int cantidad;
    private int stockMinimo;
    private String ubicacion;
    private double precioUnitario;
    private Date fechaRegistro;
    private Date fechaUltimaActualizacion;
    
    public ArticuloInventario() {}

    public ArticuloInventario(int id, String nombre, String tipo, String descripcion, 
                             int cantidad, int stockMinimo, String ubicacion, 
                             double precioUnitario, Date fechaRegistro, Date fechaUltimaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.ubicacion = ubicacion;
        this.precioUnitario = precioUnitario;
        this.fechaRegistro = fechaRegistro;
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Date getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }
    public void setFechaUltimaActualizacion(Date fechaUltimaActualizacion) { 
        this.fechaUltimaActualizacion = fechaUltimaActualizacion; 
    }
    
    /**
     * Verifica si el artículo tiene stock bajo.
     * 
     * @return true si la cantidad es menor o igual al stock mínimo
     */
    public boolean tieneStockBajo() {
        return cantidad <= stockMinimo;
    }
    
    /**
     * Calcula el valor total del inventario de este artículo.
     * 
     * @return valor total (cantidad * precio unitario)
     */
    public double getValorTotal() {
        return cantidad * precioUnitario;
    }
}
