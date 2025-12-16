package gruasheredianas.gps.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.gps.model.UbicacionGPS;
import gruasheredianas.gps.model.RutaHistorial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de ubicaciones GPS y rutas en la base de datos.
 * 
 * @author franco
 */
public class UbicacionGPSDAO {
    
    /**
     * Registra una nueva ubicación GPS.
     * 
     * @param ubicacion Ubicación a registrar
     * @return true si el registro fue exitoso
     */
    public boolean registrarUbicacion(UbicacionGPS ubicacion) {
        String sql = "INSERT INTO ubicacion_gps (camion_id, latitud, longitud, fecha_hora, " +
                     "velocidad, estado, direccion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ubicacion.getCamionId());
            ps.setDouble(2, ubicacion.getLatitud());
            ps.setDouble(3, ubicacion.getLongitud());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setDouble(5, ubicacion.getVelocidad());
            ps.setString(6, ubicacion.getEstado());
            ps.setString(7, ubicacion.getDireccion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar ubicación GPS: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene la última ubicación registrada de una grúa.
     * 
     * @param camionId ID del camión
     * @return Última ubicación o null si no existe
     */
    public UbicacionGPS obtenerUltimaUbicacion(int camionId) {
        String sql = "SELECT * FROM ubicacion_gps WHERE camion_id = ? " +
                     "ORDER BY fecha_hora DESC LIMIT 1";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraerUbicacionDeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener última ubicación: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todas las ubicaciones de una grúa en un rango de fechas.
     * 
     * @param camionId ID del camión
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de ubicaciones
     */
    public List<UbicacionGPS> obtenerUbicacionesPorPeriodo(int camionId, java.util.Date fechaInicio, java.util.Date fechaFin) {
        List<UbicacionGPS> ubicaciones = new ArrayList<>();
        String sql = "SELECT * FROM ubicacion_gps WHERE camion_id = ? " +
                     "AND fecha_hora BETWEEN ? AND ? ORDER BY fecha_hora ASC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ps.setTimestamp(2, new Timestamp(fechaInicio.getTime()));
            ps.setTimestamp(3, new Timestamp(fechaFin.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ubicaciones.add(extraerUbicacionDeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ubicaciones por periodo: " + e.getMessage());
        }
        return ubicaciones;
    }
    
    /**
     * Obtiene ubicaciones actuales de todas las grúas.
     * 
     * @return Lista de últimas ubicaciones por grúa
     */
    public List<UbicacionGPS> obtenerUbicacionesActuales() {
        List<UbicacionGPS> ubicaciones = new ArrayList<>();
        String sql = "SELECT u1.* FROM ubicacion_gps u1 " +
                     "INNER JOIN (SELECT camion_id, MAX(fecha_hora) as max_fecha " +
                     "FROM ubicacion_gps GROUP BY camion_id) u2 " +
                     "ON u1.camion_id = u2.camion_id AND u1.fecha_hora = u2.max_fecha";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ubicaciones.add(extraerUbicacionDeResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ubicaciones actuales: " + e.getMessage());
        }
        return ubicaciones;
    }
    
    /**
     * Registra una ruta en el historial.
     * 
     * @param ruta Ruta a registrar
     * @return true si el registro fue exitoso
     */
    public boolean registrarRuta(RutaHistorial ruta) {
        String sql = "INSERT INTO ruta_historial (camion_id, fecha_inicio, fecha_fin, " +
                     "latitud_inicio, longitud_inicio, latitud_fin, longitud_fin, " +
                     "distancia_recorrida, tiempo_servicio, tipo_servicio) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ruta.getCamionId());
            ps.setTimestamp(2, new Timestamp(ruta.getFechaInicio().getTime()));
            ps.setTimestamp(3, new Timestamp(ruta.getFechaFin().getTime()));
            ps.setDouble(4, ruta.getLatitudInicio());
            ps.setDouble(5, ruta.getLongitudInicio());
            ps.setDouble(6, ruta.getLatitudFin());
            ps.setDouble(7, ruta.getLongitudFin());
            ps.setDouble(8, ruta.getDistanciaRecorrida());
            ps.setLong(9, ruta.getTiempoServicio());
            ps.setString(10, ruta.getTipoServicio());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar ruta: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el historial de rutas de una grúa.
     * 
     * @param camionId ID del camión
     * @return Lista de rutas
     */
    public List<RutaHistorial> obtenerHistorialRutas(int camionId) {
        List<RutaHistorial> rutas = new ArrayList<>();
        String sql = "SELECT * FROM ruta_historial WHERE camion_id = ? ORDER BY fecha_inicio DESC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rutas.add(extraerRutaDeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener historial de rutas: " + e.getMessage());
        }
        return rutas;
    }
    
    /**
     * Obtiene rutas por periodo de tiempo.
     * 
     * @param camionId ID del camión
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de rutas en el periodo
     */
    public List<RutaHistorial> obtenerRutasPorPeriodo(int camionId, java.util.Date fechaInicio, java.util.Date fechaFin) {
        List<RutaHistorial> rutas = new ArrayList<>();
        String sql = "SELECT * FROM ruta_historial WHERE camion_id = ? " +
                     "AND fecha_inicio BETWEEN ? AND ? ORDER BY fecha_inicio ASC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ps.setTimestamp(2, new Timestamp(fechaInicio.getTime()));
            ps.setTimestamp(3, new Timestamp(fechaFin.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rutas.add(extraerRutaDeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener rutas por periodo: " + e.getMessage());
        }
        return rutas;
    }
    
    /**
     * Extrae una ubicación GPS del ResultSet.
     */
    private UbicacionGPS extraerUbicacionDeResultSet(ResultSet rs) throws SQLException {
        UbicacionGPS ubicacion = new UbicacionGPS();
        ubicacion.setId(rs.getInt("id"));
        ubicacion.setCamionId(rs.getInt("camion_id"));
        ubicacion.setLatitud(rs.getDouble("latitud"));
        ubicacion.setLongitud(rs.getDouble("longitud"));
        ubicacion.setFechaHora(rs.getTimestamp("fecha_hora"));
        ubicacion.setVelocidad(rs.getDouble("velocidad"));
        ubicacion.setEstado(rs.getString("estado"));
        ubicacion.setDireccion(rs.getString("direccion"));
        return ubicacion;
    }
    
    /**
     * Extrae una ruta del ResultSet.
     */
    private RutaHistorial extraerRutaDeResultSet(ResultSet rs) throws SQLException {
        RutaHistorial ruta = new RutaHistorial();
        ruta.setId(rs.getInt("id"));
        ruta.setCamionId(rs.getInt("camion_id"));
        ruta.setFechaInicio(rs.getTimestamp("fecha_inicio"));
        ruta.setFechaFin(rs.getTimestamp("fecha_fin"));
        ruta.setLatitudInicio(rs.getDouble("latitud_inicio"));
        ruta.setLongitudInicio(rs.getDouble("longitud_inicio"));
        ruta.setLatitudFin(rs.getDouble("latitud_fin"));
        ruta.setLongitudFin(rs.getDouble("longitud_fin"));
        ruta.setDistanciaRecorrida(rs.getDouble("distancia_recorrida"));
        ruta.setTiempoServicio(rs.getLong("tiempo_servicio"));
        ruta.setTipoServicio(rs.getString("tipo_servicio"));
        return ruta;
    }
}
