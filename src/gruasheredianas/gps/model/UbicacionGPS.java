package gruasheredianas.gps.model;

import java.util.Date;

/**
 * Modelo que representa una ubicación GPS de una grúa.
 * Almacena coordenadas, velocidad y datos de localización en tiempo real.
 * 
 * @author franco
 */
public class UbicacionGPS {
    
    private int id;
    private int camionId;
    private double latitud;
    private double longitud;
    private Date fechaHora;
    private double velocidad; // km/h
    private String estado; // "EN_SERVICIO", "DISPONIBLE", "MANTENIMIENTO", "INACTIVO"
    private String direccion; // Dirección aproximada (opcional)
    
    public UbicacionGPS() {}

    public UbicacionGPS(int id, int camionId, double latitud, double longitud, 
                       Date fechaHora, double velocidad, String estado, String direccion) {
        this.id = id;
        this.camionId = camionId;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHora = fechaHora;
        this.velocidad = velocidad;
        this.estado = estado;
        this.direccion = direccion;
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

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    /**
     * Calcula la distancia en kilómetros entre esta ubicación y otra.
     * Utiliza la fórmula de Haversine.
     * 
     * @param otra Otra ubicación GPS
     * @return Distancia en kilómetros
     */
    public double calcularDistancia(UbicacionGPS otra) {
        final int RADIO_TIERRA_KM = 6371;
        
        double lat1Rad = Math.toRadians(this.latitud);
        double lat2Rad = Math.toRadians(otra.latitud);
        double deltaLatRad = Math.toRadians(otra.latitud - this.latitud);
        double deltaLonRad = Math.toRadians(otra.longitud - this.longitud);
        
        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return RADIO_TIERRA_KM * c;
    }
}
