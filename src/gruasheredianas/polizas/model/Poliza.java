package gruasheredianas.polizas.model;

import java.util.Date;

/**
 *
 * @author franc
 */
public class Poliza {
     private int id;
    private final String nombreSeguro;
    private String placaRelacionada; // Nombre de la grúa asociada
    private Date fechaInicio;
    private Date fechaVencimiento;
    private static int nextId = 1;

    public Poliza(String nombreSeguro, String placaRelacionada, Date fechaInicio, Date fechaVencimiento) {
        this.id = nextId++;
        this.nombreSeguro = nombreSeguro;
        this.placaRelacionada = placaRelacionada;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getId() {
        return id;
    }

    public String getNombreSeguro() {
        return nombreSeguro;
    }

    public String getPlacaRelacionada() {
        return placaRelacionada;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return "Poliza ID: " + id + ", Seguro: " + nombreSeguro + ", Placa de grúa: " + placaRelacionada +
                ", Fecha Inicio: " + fechaInicio + ", Fecha Vencimiento: " + fechaVencimiento;
    }
    
}
