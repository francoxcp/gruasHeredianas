
package gruasheredianas.camiones.model;

import java.util.Date;


public class Factura {
  private int id;
    private String nombreCliente;
    private String cedula;
    private Date fecha;
    private String telefono;
    private double kilometrosRecorridos;
    private double tarifaPorKm;
    private double otrosCargos;
    private String observaciones;
    private double subtotal;
    private double iva;
    private double total;

    public Factura() {}

    public Factura(int id, String nombreCliente, String cedula, Date fecha, String telefono,
                   double kilometrosRecorridos, double tarifaPorKm, double otrosCargos,
                   String observaciones, double subtotal, double iva, double total) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.cedula = cedula;
        this.fecha = fecha;
        this.telefono = telefono;
        this.kilometrosRecorridos = kilometrosRecorridos;
        this.tarifaPorKm = tarifaPorKm;
        this.otrosCargos = otrosCargos;
        this.observaciones = observaciones;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public double getKilometrosRecorridos() { return kilometrosRecorridos; }
    public void setKilometrosRecorridos(double kilometrosRecorridos) { this.kilometrosRecorridos = kilometrosRecorridos; }

    public double getTarifaPorKm() { return tarifaPorKm; }
    public void setTarifaPorKm(double tarifaPorKm) { this.tarifaPorKm = tarifaPorKm; }

    public double getOtrosCargos() { return otrosCargos; }
    public void setOtrosCargos(double otrosCargos) { this.otrosCargos = otrosCargos; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    
 public void calcularMontos() {
    this.subtotal = (kilometrosRecorridos * tarifaPorKm) + otrosCargos;
    this.iva = subtotal * 0.13; // 13% de IVA
    this.total = subtotal + iva;
}
}
