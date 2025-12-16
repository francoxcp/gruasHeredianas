package gruasheredianas.polizas.model;

import java.util.Date;

/**
 * Representa una póliza de seguro asociada a una grúa
 * @author franco
 */
public class Poliza {
    
    private int id;
    private int camionId;
    private String numeroPoliza;
    private String aseguradora;
    private String tipoCobertura;
    private double montoAsegurado;
    private double prima;
    private Date fechaInicio;
    private Date fechaVencimiento;
    private String estado; // VIGENTE, VENCIDA, CANCELADA
    private String observaciones;
    
    public Poliza() {}

    public Poliza(int id, int camionId, String numeroPoliza, String aseguradora, 
                 String tipoCobertura, double montoAsegurado, double prima, 
                 Date fechaInicio, Date fechaVencimiento, String estado, String observaciones) {
        this.id = id;
        this.camionId = camionId;
        this.numeroPoliza = numeroPoliza;
        this.aseguradora = aseguradora;
        this.tipoCobertura = tipoCobertura;
        this.montoAsegurado = montoAsegurado;
        this.prima = prima;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
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

    public String getTipoCobertura() { return tipoCobertura; }
    public void setTipoCobertura(String tipoCobertura) { this.tipoCobertura = tipoCobertura; }

    public double getMontoAsegurado() { return montoAsegurado; }
    public void setMontoAsegurado(double montoAsegurado) { this.montoAsegurado = montoAsegurado; }

    public double getPrima() { return prima; }
    public void setPrima(double prima) { this.prima = prima; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    /**
     * Calcula los días hasta el vencimiento
     * @return días restantes (negativo si ya venció)
     */
    public long diasHastaVencimiento() {
        long diff = fechaVencimiento.getTime() - new Date().getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Verifica si la póliza está próxima a vencer (30 días)
     * @return true si vence en 30 días o menos
     */
    public boolean proximaAVencer() {
        long dias = diasHastaVencimiento();
        return dias >= 0 && dias <= 30;
    }
    
    /**
     * Verifica si la póliza está vencida
     * @return true si ya venció
     */
    public boolean estaVencida() {
        return diasHastaVencimiento() < 0;
    }
}
