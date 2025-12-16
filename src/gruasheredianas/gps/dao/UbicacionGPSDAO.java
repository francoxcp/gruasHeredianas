package gruasheredianas.gps.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.gps.model.UbicacionGPS;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO para gestión de ubicaciones GPS
 * @author franco
 */
public class UbicacionGPSDAO {
    
    /**
     * Registra una nueva ubicación GPS
     */
    public boolean registrar(UbicacionGPS ubicacion) {
        String sql = "INSERT INTO ubicaciones_gps (camion_id, latitud, longitud, fecha_hora, velocidad, estado, punto_servicio) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ubicacion.getCamionId());
            ps.setDouble(2, ubicacion.getLatitud());
            ps.setDouble(3, ubicacion.getLongitud());
            
            if (ubicacion.getFechaHora() == null) {
                ps.setTimestamp(4, new Timestamp(new Date().getTime()));
            } else {
                ps.setTimestamp(4, new Timestamp(ubicacion.getFechaHora().getTime()));
            }
            
            ps.setDouble(5, ubicacion.getVelocidad());
            ps.setString(6, ubicacion.getEstado());
            ps.setString(7, ubicacion.getPuntoServicio());
            
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            
            try (ResultSet generated = ps.getGeneratedKeys()) {
                if (generated.next()) {
                    ubicacion.setId(generated.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar ubicación GPS: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la última ubicación de una grúa
     */
    public UbicacionGPS obtenerUltimaUbicacion(int camionId) {
        String sql = "SELECT * FROM ubicaciones_gps WHERE camion_id = ? ORDER BY fecha_hora DESC LIMIT 1";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener última ubicación: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene las últimas ubicaciones de todas las grúas
     */
    public List<UbicacionGPS> obtenerUltimasUbicaciones() {
        List<UbicacionGPS> lista = new ArrayList<>();
        String sql = "SELECT u1.* FROM ubicaciones_gps u1 " +
                    "INNER JOIN (SELECT camion_id, MAX(fecha_hora) as max_fecha FROM ubicaciones_gps GROUP BY camion_id) u2 " +
                    "ON u1.camion_id = u2.camion_id AND u1.fecha_hora = u2.max_fecha";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener últimas ubicaciones: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene el historial de ubicaciones de una grúa en un periodo
     */
    public List<UbicacionGPS> obtenerHistorial(int camionId, Date desde, Date hasta) {
        List<UbicacionGPS> lista = new ArrayList<>();
        String sql = "SELECT * FROM ubicaciones_gps WHERE camion_id = ? AND fecha_hora BETWEEN ? AND ? ORDER BY fecha_hora";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Mapea un ResultSet a un objeto UbicacionGPS
     */
    private UbicacionGPS mapRow(ResultSet rs) throws SQLException {
        UbicacionGPS u = new UbicacionGPS();
        u.setId(rs.getInt("id"));
        u.setCamionId(rs.getInt("camion_id"));
        u.setLatitud(rs.getDouble("latitud"));
        u.setLongitud(rs.getDouble("longitud"));
        
        Timestamp t = rs.getTimestamp("fecha_hora");
        if (t != null) {
            u.setFechaHora(new Date(t.getTime()));
        }
        
        u.setVelocidad(rs.getDouble("velocidad"));
        u.setEstado(rs.getString("estado"));
        u.setPuntoServicio(rs.getString("punto_servicio"));
        return u;
    }
}
