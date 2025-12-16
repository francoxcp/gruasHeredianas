package gruasheredianas.gps.service;

import gruasheredianas.gps.dao.UbicacionGPSDAO;
import gruasheredianas.gps.model.UbicacionGPS;
import gruasheredianas.gps.model.RutaHistorial;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Servicio para seguimiento GPS de grúas.
 * Proporciona monitoreo en tiempo real, historial de rutas y notificaciones.
 * 
 * @author franco
 */
public class SeguimientoGPSService {
    
    private final UbicacionGPSDAO ubicacionGPSDAO;
    
    public SeguimientoGPSService() {
        this.ubicacionGPSDAO = new UbicacionGPSDAO();
    }
    
    /**
     * Actualiza la ubicación GPS de una grúa.
     * 
     * @param ubicacion Nueva ubicación
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarUbicacion(UbicacionGPS ubicacion) {
        return ubicacionGPSDAO.registrarUbicacion(ubicacion);
    }
    
    /**
     * Obtiene la ubicación actual de una grúa específica.
     * 
     * @param camionId ID del camión
     * @return Ubicación actual o null si no existe
     */
    public UbicacionGPS obtenerUbicacionActual(int camionId) {
        return ubicacionGPSDAO.obtenerUltimaUbicacion(camionId);
    }
    
    /**
     * Obtiene las ubicaciones actuales de todas las grúas.
     * Útil para panel de control con mapa interactivo.
     * 
     * @return Mapa de ubicaciones por ID de camión
     */
    public Map<Integer, UbicacionGPS> obtenerTodasLasUbicacionesActuales() {
        List<UbicacionGPS> ubicaciones = ubicacionGPSDAO.obtenerUbicacionesActuales();
        Map<Integer, UbicacionGPS> mapaUbicaciones = new HashMap<>();
        for (UbicacionGPS ubicacion : ubicaciones) {
            mapaUbicaciones.put(ubicacion.getCamionId(), ubicacion);
        }
        return mapaUbicaciones;
    }
    
    /**
     * Obtiene el historial de ubicaciones de una grúa en un periodo.
     * 
     * @param camionId ID del camión
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de ubicaciones en el periodo
     */
    public List<UbicacionGPS> obtenerHistorialUbicaciones(int camionId, Date fechaInicio, Date fechaFin) {
        return ubicacionGPSDAO.obtenerUbicacionesPorPeriodo(camionId, fechaInicio, fechaFin);
    }
    
    /**
     * Registra una nueva ruta completada.
     * 
     * @param ruta Ruta a registrar
     * @return true si el registro fue exitoso
     */
    public boolean registrarRuta(RutaHistorial ruta) {
        return ubicacionGPSDAO.registrarRuta(ruta);
    }
    
    /**
     * Obtiene el historial completo de rutas de una grúa.
     * 
     * @param camionId ID del camión
     * @return Lista de rutas
     */
    public List<RutaHistorial> obtenerHistorialRutas(int camionId) {
        return ubicacionGPSDAO.obtenerHistorialRutas(camionId);
    }
    
    /**
     * Obtiene rutas de una grúa en un periodo específico.
     * 
     * @param camionId ID del camión
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de rutas en el periodo
     */
    public List<RutaHistorial> obtenerRutasPorPeriodo(int camionId, Date fechaInicio, Date fechaFin) {
        return ubicacionGPSDAO.obtenerRutasPorPeriodo(camionId, fechaInicio, fechaFin);
    }
    
    /**
     * Calcula el tiempo total de servicio de una grúa en un periodo.
     * 
     * @param camionId ID del camión
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Tiempo total en minutos
     */
    public long calcularTiempoTotalServicio(int camionId, Date fechaInicio, Date fechaFin) {
        List<RutaHistorial> rutas = ubicacionGPSDAO.obtenerRutasPorPeriodo(camionId, fechaInicio, fechaFin);
        return rutas.stream()
                .mapToLong(RutaHistorial::getTiempoServicio)
                .sum();
    }
    
    /**
     * Calcula la distancia total recorrida por una grúa en un periodo.
     * 
     * @param camionId ID del camión
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Distancia total en kilómetros
     */
    public double calcularDistanciaTotalRecorrida(int camionId, Date fechaInicio, Date fechaFin) {
        List<RutaHistorial> rutas = ubicacionGPSDAO.obtenerRutasPorPeriodo(camionId, fechaInicio, fechaFin);
        return rutas.stream()
                .mapToDouble(RutaHistorial::getDistanciaRecorrida)
                .sum();
    }
    
    /**
     * Verifica si una grúa ha llegado a un punto de servicio.
     * Genera notificación si la distancia es menor a 100 metros.
     * 
     * @param camionId ID del camión
     * @param latitudDestino Latitud del punto de servicio
     * @param longitudDestino Longitud del punto de servicio
     * @return true si la grúa está en el punto de servicio
     */
    public boolean verificarLlegadaPuntoServicio(int camionId, double latitudDestino, double longitudDestino) {
        UbicacionGPS ubicacionActual = ubicacionGPSDAO.obtenerUltimaUbicacion(camionId);
        if (ubicacionActual == null) {
            return false;
        }
        
        UbicacionGPS puntoDestino = new UbicacionGPS();
        puntoDestino.setLatitud(latitudDestino);
        puntoDestino.setLongitud(longitudDestino);
        
        double distancia = ubicacionActual.calcularDistancia(puntoDestino);
        
        // Si la distancia es menor a 0.1 km (100 metros), se considera que llegó
        if (distancia < 0.1) {
            notificarLlegada(camionId, ubicacionActual);
            return true;
        }
        
        return false;
    }
    
    /**
     * Genera notificación de llegada a punto de servicio.
     * 
     * @param camionId ID del camión
     * @param ubicacion Ubicación de llegada
     */
    private void notificarLlegada(int camionId, UbicacionGPS ubicacion) {
        System.out.println("=== NOTIFICACIÓN DE LLEGADA ===");
        System.out.println("Grúa ID: " + camionId);
        System.out.println("Ha llegado al punto de servicio");
        System.out.println("Ubicación: " + ubicacion.getDireccion());
        System.out.println("Coordenadas: " + ubicacion.getLatitud() + ", " + ubicacion.getLongitud());
        System.out.println("Hora: " + ubicacion.getFechaHora());
    }
    
    /**
     * Genera notificación de salida de punto de servicio.
     * 
     * @param camionId ID del camión
     * @param ubicacion Ubicación de salida
     */
    public void notificarSalida(int camionId, UbicacionGPS ubicacion) {
        System.out.println("=== NOTIFICACIÓN DE SALIDA ===");
        System.out.println("Grúa ID: " + camionId);
        System.out.println("Ha salido del punto de servicio");
        System.out.println("Ubicación: " + ubicacion.getDireccion());
        System.out.println("Coordenadas: " + ubicacion.getLatitud() + ", " + ubicacion.getLongitud());
        System.out.println("Hora: " + ubicacion.getFechaHora());
    }
    
    /**
     * Genera reporte de actividad de una grúa.
     * 
     * @param camionId ID del camión
     * @param fechaInicio Fecha de inicio del reporte
     * @param fechaFin Fecha de fin del reporte
     * @return String con el reporte
     */
    public String generarReporteActividad(int camionId, Date fechaInicio, Date fechaFin) {
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("=== REPORTE DE ACTIVIDAD GPS ===\n");
        reporte.append("Grúa ID: ").append(camionId).append("\n");
        reporte.append("Periodo: ").append(fechaInicio).append(" - ").append(fechaFin).append("\n\n");
        
        List<RutaHistorial> rutas = ubicacionGPSDAO.obtenerRutasPorPeriodo(camionId, fechaInicio, fechaFin);
        
        reporte.append("Total de servicios: ").append(rutas.size()).append("\n");
        
        double distanciaTotal = rutas.stream()
                .mapToDouble(RutaHistorial::getDistanciaRecorrida)
                .sum();
        reporte.append("Distancia total recorrida: ").append(String.format("%.2f km", distanciaTotal)).append("\n");
        
        long tiempoTotal = rutas.stream()
                .mapToLong(RutaHistorial::getTiempoServicio)
                .sum();
        reporte.append("Tiempo total de servicio: ").append(tiempoTotal).append(" minutos\n");
        
        if (!rutas.isEmpty()) {
            double velocidadPromedio = rutas.stream()
                    .mapToDouble(RutaHistorial::getVelocidadPromedio)
                    .average()
                    .orElse(0.0);
            reporte.append("Velocidad promedio: ").append(String.format("%.2f km/h", velocidadPromedio)).append("\n");
        }
        
        reporte.append("\n--- Detalle de servicios ---\n");
        for (RutaHistorial ruta : rutas) {
            reporte.append("Tipo: ").append(ruta.getTipoServicio())
                   .append(" | Distancia: ").append(String.format("%.2f km", ruta.getDistanciaRecorrida()))
                   .append(" | Tiempo: ").append(ruta.getTiempoServicio()).append(" min")
                   .append(" | Fecha: ").append(ruta.getFechaInicio()).append("\n");
        }
        
        return reporte.toString();
    }
    
    /**
     * Obtiene grúas disponibles cercanas a una ubicación.
     * 
     * @param latitud Latitud de referencia
     * @param longitud Longitud de referencia
     * @param radioKm Radio de búsqueda en kilómetros
     * @return Lista de IDs de grúas disponibles cercanas
     */
    public List<Integer> obtenerGruasCercanas(double latitud, double longitud, double radioKm) {
        Map<Integer, UbicacionGPS> ubicaciones = obtenerTodasLasUbicacionesActuales();
        
        UbicacionGPS puntoReferencia = new UbicacionGPS();
        puntoReferencia.setLatitud(latitud);
        puntoReferencia.setLongitud(longitud);
        
        return ubicaciones.values().stream()
                .filter(u -> "DISPONIBLE".equals(u.getEstado()))
                .filter(u -> u.calcularDistancia(puntoReferencia) <= radioKm)
                .map(UbicacionGPS::getCamionId)
                .collect(Collectors.toList());
    }
}
