package gruasheredianas.polizas.model;

import java.util.Date;

/**
 * Modelo que representa una póliza de seguro asociada a una grúa.
 * Gestiona información de seguros, coberturas y vencimientos.
 * 
 * @author franco
 */
public class PolizaSeguro {
    
    private int id;
    private int camionId;
    private String numeroPoliza;
    private String aseguradora;
    private Date fechaInicio;
    private Date fechaVencimiento;
    private String tipoCobertura; // "TOTAL", "PARCIAL", "RESPONSABILIDAD_CIVIL", etc.
    private double montoCobertura;
    private double primaMensual;
    private String estado; // "ACTIVA", "VENCIDA", "CANCELADA"
    private String observaciones;
    
    public PolizaSeguro() {}

    public PolizaSeguro(int id, int camionId, String numeroPoliza, String aseguradora, 
                       Date fechaInicio, Date fechaVencimiento, String tipoCobertura, 
                       double montoCobertura, double primaMensual, String estado, String observaciones) {
        this.id = id;
        this.camionId = camionId;
        this.numeroPoliza = numeroPoliza;
        this.aseguradora = aseguradora;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.tipoCobertura = tipoCobertura;
        this.montoCobertura = montoCobertura;
        this.primaMensual = primaMensual;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCamionId() { return camionId; }
    public void setCamionId(int camionId) { this.camionId = camionId; }

    public String getNumeroPoliza() { return numeroPoliza; }
    public void setNumeroPoliza(String numeroPoliza) { this.numeroPoliza = numeroPoliza; }

    public String getAseguradora() { return aseguradora; }
    public void setAseguradora(String aseguradora) { this.aseguradora = aseguradora; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getTipoCobertura() { return tipoCobertura; }
    public void setTipoCobertura(String tipoCobertura) { this.tipoCobertura = tipoCobertura; }

    public double getMontoCobertura() { return montoCobertura; }
    public void setMontoCobertura(double montoCobertura) { this.montoCobertura = montoCobertura; }

    public double getPrimaMensual() { return primaMensual; }
    public void setPrimaMensual(double primaMensual) { this.primaMensual = primaMensual; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    /**
     * Calcula los días restantes hasta el vencimiento.
     * 
     * @return Días restantes, negativo si ya venció
     */
    public long getDiasHastaVencimiento() {
        Date hoy = new Date();
        long diferencia = fechaVencimiento.getTime() - hoy.getTime();
        return diferencia / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Verifica si la póliza está por vencer (dentro de 30 días).
     * 
     * @return true si está por vencer
     */
    public boolean estaPorVencer() {
        long diasRestantes = getDiasHastaVencimiento();
        return diasRestantes >= 0 && diasRestantes <= 30;
    }
    
    /**
     * Verifica si la póliza está vencida.
     * 
     * @return true si está vencida
     */
    public boolean estaVencida() {
        return getDiasHastaVencimiento() < 0;
    }
    
    /**
     * Calcula el costo anual de la póliza.
     * 
     * @return Costo anual
     */
    public double getCostoAnual() {
        return primaMensual * 12;
    }
}
