
package gruasheredianas.camiones.model;


public class Solicitud {
   private int id;
    private String nombreCliente;
    private String telefono;
    private String direccionIncidente;
    private String descripcion;
    private String tipoVehiculo;
    private String tipoServicio;
    private String prioridad;
    private String estado;
    private String fecha;

    // Constructor vacío (NECESARIO para cargar desde BD)
    public Solicitud() {
    }

    // Constructor para registrar nuevas solicitudes
    public Solicitud(String nombreCliente, String telefono, String direccionIncidente,
                     String descripcion, String tipoVehiculo, String tipoServicio,
                     String prioridad) {

        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.direccionIncidente = direccionIncidente;
        this.descripcion = descripcion;
        this.tipoVehiculo = tipoVehiculo;
        this.tipoServicio = tipoServicio;
        this.prioridad = prioridad;
    }

    // ===== GETTERS =====
    public int getId() { return id; }
    public String getNombreCliente() { return nombreCliente; }
    public String getTelefono() { return telefono; }
    public String getDireccionIncidente() { return direccionIncidente; }
    public String getDescripcion() { return descripcion; }
    public String getTipoVehiculo() { return tipoVehiculo; }
    public String getTipoServicio() { return tipoServicio; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public String getFecha() { return fecha; }

    // ===== SETTERS =====
    public void setId(int id) { this.id = id; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccionIncidente(String direccion) { this.direccionIncidente = direccion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}              



