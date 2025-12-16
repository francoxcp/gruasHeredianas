package gruasheredianas.gps.service;

import gruasheredianas.gps.dao.UbicacionGPSDAO;
import gruasheredianas.gps.dao.RutaHistoricoDAO;
import gruasheredianas.gps.model.UbicacionGPS;
import gruasheredianas.gps.model.RutaHistorico;
import java.util.Date;
import java.util.List;

/**
 * Servicio para gestión de seguimiento GPS y rutas
 * @author franco
 */
public class GPSService {
    
    private UbicacionGPSDAO ubicacionDAO = new UbicacionGPSDAO();
    private RutaHistoricoDAO rutaDAO = new RutaHistoricoDAO();

    /**
     * Actualiza la ubicación actual de una grúa
     * @param camionId ID de la grúa
     * @param latitud Latitud GPS
     * @param longitud Longitud GPS
     * @param velocidad Velocidad actual
     * @param estado Estado de la grúa
     * @param puntoServicio Punto de servicio actual (opcional)
     * @return true si se actualizó correctamente
     */
    public boolean actualizarUbicacion(int camionId, double latitud, double longitud, 
                                      double velocidad, String estado, String puntoServicio) {
        UbicacionGPS ubicacion = new UbicacionGPS();
        ubicacion.setCamionId(camionId);
        ubicacion.setLatitud(latitud);
        ubicacion.setLongitud(longitud);
        ubicacion.setFechaHora(new Date());
        ubicacion.setVelocidad(velocidad);
        ubicacion.setEstado(estado);
        ubicacion.setPuntoServicio(puntoServicio);
        
        return ubicacionDAO.registrar(ubicacion);
    }

    /**
     * Obtiene la ubicación actual de una grúa
     * @param camionId ID de la grúa
     * @return Última ubicación registrada
     */
    public UbicacionGPS obtenerUbicacionActual(int camionId) {
        return ubicacionDAO.obtenerUltimaUbicacion(camionId);
    }

    /**
     * Obtiene las ubicaciones actuales de toda la flota
     * @return Lista de últimas ubicaciones de todas las grúas
     */
    public List<UbicacionGPS> obtenerUbicacionesFlota() {
        return ubicacionDAO.obtenerUltimasUbicaciones();
    }

    /**
     * Obtiene el historial de ubicaciones de una grúa
     * @param camionId ID de la grúa
     * @param desde Fecha inicial
     * @param hasta Fecha final
     * @return Lista de ubicaciones en el periodo
     */
    public List<UbicacionGPS> obtenerHistorialUbicaciones(int camionId, Date desde, Date hasta) {
        return ubicacionDAO.obtenerHistorial(camionId, desde, hasta);
    }

    /**
     * Registra el inicio de una ruta de servicio
     * @param camionId ID de la grúa
     * @param latitudInicio Latitud inicial
     * @param longitudInicio Longitud inicial
     * @param tipoServicio Tipo de servicio
     * @param solicitudId ID de la solicitud relacionada
     * @return ID de la ruta creada o -1 si hay error
     */
    public int iniciarRuta(int camionId, double latitudInicio, double longitudInicio, 
                          String tipoServicio, Integer solicitudId) {
        RutaHistorico ruta = new RutaHistorico();
        ruta.setCamionId(camionId);
        ruta.setFechaInicio(new Date());
        ruta.setLatitudInicio(latitudInicio);
        ruta.setLongitudInicio(longitudInicio);
        ruta.setLatitudFin(0.0);
        ruta.setLongitudFin(0.0);
        ruta.setDistanciaKm(0.0);
        ruta.setTiempoMinutos(0);
        ruta.setTipoServicio(tipoServicio);
        ruta.setSolicitudId(solicitudId);
        
        if (rutaDAO.registrar(ruta)) {
            return ruta.getId();
        }
        return -1;
    }

    /**
     * Obtiene el historial de rutas de una grúa
     * @param camionId ID de la grúa
     * @return Lista de rutas históricas
     */
    public List<RutaHistorico> obtenerHistorialRutas(int camionId) {
        return rutaDAO.listarPorCamion(camionId);
    }

    /**
     * Obtiene rutas en un periodo determinado
     * @param desde Fecha inicial
     * @param hasta Fecha final
     * @return Lista de rutas en el periodo
     */
    public List<RutaHistorico> obtenerRutasPeriodo(Date desde, Date hasta) {
        return rutaDAO.listarPorPeriodo(desde, hasta);
    }

    /**
     * Calcula la distancia total recorrida por una grúa en un periodo
     * @param camionId ID de la grúa
     * @param desde Fecha inicial
     * @param hasta Fecha final
     * @return Kilómetros recorridos
     */
    public double calcularDistanciaRecorrida(int camionId, Date desde, Date hasta) {
        return rutaDAO.calcularKmsPeriodo(camionId, desde, hasta);
    }

    /**
     * Notifica llegada a un punto de servicio
     * @param camionId ID de la grúa
     * @param puntoServicio Nombre o dirección del punto
     * @return true si se registró correctamente
     */
    public boolean notificarLlegada(int camionId, String puntoServicio) {
        UbicacionGPS ubicacionActual = ubicacionDAO.obtenerUltimaUbicacion(camionId);
        if (ubicacionActual == null) {
            return false;
        }
        
        // Registrar llegada con estado EN_SERVICIO
        return actualizarUbicacion(camionId, ubicacionActual.getLatitud(), 
                                  ubicacionActual.getLongitud(), 0.0, 
                                  "EN_SERVICIO", puntoServicio);
    }

    /**
     * Notifica salida de un punto de servicio
     * @param camionId ID de la grúa
     * @return true si se registró correctamente
     */
    public boolean notificarSalida(int camionId) {
        UbicacionGPS ubicacionActual = ubicacionDAO.obtenerUltimaUbicacion(camionId);
        if (ubicacionActual == null) {
            return false;
        }
        
        // Registrar salida con estado DISPONIBLE
        return actualizarUbicacion(camionId, ubicacionActual.getLatitud(), 
                                  ubicacionActual.getLongitud(), 0.0, 
                                  "DISPONIBLE", null);
    }
}
