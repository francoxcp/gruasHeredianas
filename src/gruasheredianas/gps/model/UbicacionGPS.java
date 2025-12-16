package gruasheredianas.gps.model;

import java.util.Date;

/**
 * Representa la ubicación GPS de una grúa en tiempo real
 * @author franco
 */
public class UbicacionGPS {
    
    private int id;
    private int camionId;
    private double latitud;
    private double longitud;
    private Date fechaHora;
    private double velocidad;
    private String estado; // EN_SERVICIO, EN_RUTA, DISPONIBLE, FUERA_SERVICIO
    private String puntoServicio; // Dirección o nombre del punto de servicio
    
    public UbicacionGPS() {}

    public UbicacionGPS(int id, int camionId, double latitud, double longitud, Date fechaHora, 
                        double velocidad, String estado, String puntoServicio) {
        this.id = id;
        this.camionId = camionId;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.velocidad = velocidad;
        this.estado = estado;
        this.puntoServicio = puntoServicio;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCamionId() { return camionId; }
    public void setCamionId(int camionId) { this.camionId = camionId; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }

    public double getVelocidad() { return velocidad; }
    public void setVelocidad(double velocidad) { this.velocidad = velocidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getPuntoServicio() { return puntoServicio; }
    public void setPuntoServicio(String puntoServicio) { this.puntoServicio = puntoServicio; }
}
