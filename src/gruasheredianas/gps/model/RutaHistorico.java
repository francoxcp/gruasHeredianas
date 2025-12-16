package gruasheredianas.gps.model;

import java.util.Date;

/**
 * Representa el historial de rutas y tiempos de servicio de una grúa
 * @author franco
 */
public class RutaHistorico {
    
    private int id;
    private int camionId;
    private Date fechaInicio;
    private Date fechaFin;
    private double latitudInicio;
    private double longitudInicio;
    private double latitudFin;
    private double longitudFin;
    private double distanciaKm;
    private int tiempoMinutos;
    private String tipoServicio;
    private Integer solicitudId; // Referencia a la solicitud de servicio
    
    public RutaHistorico() {}

    public RutaHistorico(int id, int camionId, Date fechaInicio, Date fechaFin, 
                        double latitudInicio, double longitudInicio, 
                        double latitudFin, double longitudFin,
                        double distanciaKm, int tiempoMinutos, 
                        String tipoServicio, Integer solicitudId) {
        this.id = id;
        this.camionId = camionId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.latitudInicio = latitudInicio;
        this.longitudInicio = longitudInicio;
        this.latitudFin = latitudFin;
        this.longitudFin = longitudFin;
        this.distanciaKm = distanciaKm;
        this.tiempoMinutos = tiempoMinutos;
        this.tipoServicio = tipoServicio;
        this.solicitudId = solicitudId;
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

    public double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(double distanciaKm) { this.distanciaKm = distanciaKm; }

    public int getTiempoMinutos() { return tiempoMinutos; }
    public void setTiempoMinutos(int tiempoMinutos) { this.tiempoMinutos = tiempoMinutos; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public Integer getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Integer solicitudId) { this.solicitudId = solicitudId; }
}
