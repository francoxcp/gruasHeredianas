package gruasheredianas.polizas.model;

import java.util.Date;

/**
 * Representa una renovación de póliza
 * @author franco
 */
public class RenovacionPoliza {
    
    private int id;
    private int polizaId;
    private Date fechaRenovacion;
    private Date nuevaFechaVencimiento;
    private double nuevaPrima;
    private String observaciones;
    private Integer usuarioId;
    
    public RenovacionPoliza() {}

    public RenovacionPoliza(int id, int polizaId, Date fechaRenovacion, 
                           Date nuevaFechaVencimiento, double nuevaPrima, 
                           String observaciones, Integer usuarioId) {
        this.id = id;
        this.polizaId = polizaId;
        this.fechaRenovacion = fechaRenovacion;
        this.nuevaFechaVencimiento = nuevaFechaVencimiento;
        this.nuevaPrima = nuevaPrima;
        this.observaciones = observaciones;
        this.usuarioId = usuarioId;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPolizaId() { return polizaId; }
    public void setPolizaId(int polizaId) { this.polizaId = polizaId; }

    public Date getFechaRenovacion() { return fechaRenovacion; }
    public void setFechaRenovacion(Date fechaRenovacion) { this.fechaRenovacion = fechaRenovacion; }

    public Date getNuevaFechaVencimiento() { return nuevaFechaVencimiento; }
    public void setNuevaFechaVencimiento(Date nuevaFechaVencimiento) { this.nuevaFechaVencimiento = nuevaFechaVencimiento; }

    public double getNuevaPrima() { return nuevaPrima; }
    public void setNuevaPrima(double nuevaPrima) { this.nuevaPrima = nuevaPrima; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
}
