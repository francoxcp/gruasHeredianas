package gruasheredianas.gps.model;

import java.util.Date;

/**
 * Modelo que representa el historial de rutas de una grúa.
 * Registra puntos de inicio/fin y tiempos de servicio.
 * 
 * @author franco
 */
public class RutaHistorial {
    
    private int id;
    private int camionId;
    private Date fechaInicio;
    private Date fechaFin;
    private double latitudInicio;
    private double longitudInicio;
    private double latitudFin;
    private double longitudFin;
    private double distanciaRecorrida; // en kilómetros
    private long tiempoServicio; // en minutos
    private String tipoServicio; // "TRASLADO", "AUXILIO", "MANTENIMIENTO", etc.
    
    public RutaHistorial() {}

    public RutaHistorial(int id, int camionId, Date fechaInicio, Date fechaFin, 
                        double latitudInicio, double longitudInicio, 
                        double latitudFin, double longitudFin, 
                        double distanciaRecorrida, long tiempoServicio, String tipoServicio) {
        this.id = id;
        this.camionId = camionId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.latitudInicio = latitudInicio;
        this.longitudInicio = longitudInicio;
        this.latitudFin = latitudFin;
        this.longitudFin = longitudFin;
        this.distanciaRecorrida = distanciaRecorrida;
        this.tiempoServicio = tiempoServicio;
        this.tipoServicio = tipoServicio;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCamionId() { return camionId; }
    public void setCamionId(int camionId) { this.camionId = camionId; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public double getLatitudInicio() { return latitudInicio; }
    public void setLatitudInicio(double latitudInicio) { this.latitudInicio = latitudInicio; }

    public double getLongitudInicio() { return longitudInicio; }
    public void setLongitudInicio(double longitudInicio) { this.longitudInicio = longitudInicio; }

    public double getLatitudFin() { return latitudFin; }
    public void setLatitudFin(double latitudFin) { this.latitudFin = latitudFin; }

    public double getLongitudFin() { return longitudFin; }
    public void setLongitudFin(double longitudFin) { this.longitudFin = longitudFin; }

    public double getDistanciaRecorrida() { return distanciaRecorrida; }
    public void setDistanciaRecorrida(double distanciaRecorrida) { 
        this.distanciaRecorrida = distanciaRecorrida; 
    }

    public long getTiempoServicio() { return tiempoServicio; }
    public void setTiempoServicio(long tiempoServicio) { this.tiempoServicio = tiempoServicio; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    
    /**
     * Calcula la velocidad promedio del servicio.
     * 
     * @return Velocidad promedio en km/h
     */
    public double getVelocidadPromedio() {
        if (tiempoServicio == 0) return 0;
        return (distanciaRecorrida / tiempoServicio) * 60; // convertir minutos a horas
    }
}
