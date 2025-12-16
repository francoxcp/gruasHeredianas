package gruasheredianas.gps.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.gps.model.RutaHistorico;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO para gestión del historial de rutas
 * @author franco
 */
public class RutaHistoricoDAO {
    
    /**
     * Registra un historial de ruta
     */
    public boolean registrar(RutaHistorico ruta) {
        String sql = "INSERT INTO rutas_historico (camion_id, fecha_inicio, fecha_fin, latitud_inicio, longitud_inicio, latitud_fin, longitud_fin, distancia_km, tiempo_minutos, tipo_servicio, solicitud_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ruta.getCamionId());
            ps.setTimestamp(2, new Timestamp(ruta.getFechaInicio().getTime()));
            
            if (ruta.getFechaFin() == null) {
                ps.setNull(3, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(3, new Timestamp(ruta.getFechaFin().getTime()));
            }
            
            ps.setDouble(4, ruta.getLatitudInicio());
            ps.setDouble(5, ruta.getLongitudInicio());
            ps.setDouble(6, ruta.getLatitudFin());
            ps.setDouble(7, ruta.getLongitudFin());
            ps.setDouble(8, ruta.getDistanciaKm());
            ps.setInt(9, ruta.getTiempoMinutos());
            ps.setString(10, ruta.getTipoServicio());
            
            if (ruta.getSolicitudId() == null) {
                ps.setNull(11, Types.INTEGER);
            } else {
                ps.setInt(11, ruta.getSolicitudId());
            }
            
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            
            try (ResultSet generated = ps.getGeneratedKeys()) {
                if (generated.next()) {
                    ruta.setId(generated.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar ruta histórica: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el historial de rutas de una grúa
     */
    public List<RutaHistorico> listarPorCamion(int camionId) {
        List<RutaHistorico> lista = new ArrayList<>();
        String sql = "SELECT * FROM rutas_historico WHERE camion_id = ? ORDER BY fecha_inicio DESC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar rutas por camión: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene el historial de rutas en un periodo
     */
    public List<RutaHistorico> listarPorPeriodo(Date desde, Date hasta) {
        List<RutaHistorico> lista = new ArrayList<>();
        String sql = "SELECT * FROM rutas_historico WHERE fecha_inicio BETWEEN ? AND ? ORDER BY fecha_inicio DESC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(desde.getTime()));
            ps.setTimestamp(2, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar rutas por periodo: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Calcula el total de kilómetros recorridos por una grúa en un periodo
     */
    public double calcularKmsPeriodo(int camionId, Date desde, Date hasta) {
        String sql = "SELECT SUM(distancia_km) as total_km FROM rutas_historico WHERE camion_id = ? AND fecha_inicio BETWEEN ? AND ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_km");
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular kms: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Mapea un ResultSet a un objeto RutaHistorico
     */
    private RutaHistorico mapRow(ResultSet rs) throws SQLException {
        RutaHistorico r = new RutaHistorico();
        r.setId(rs.getInt("id"));
        r.setCamionId(rs.getInt("camion_id"));
        
        Timestamp ti = rs.getTimestamp("fecha_inicio");
        if (ti != null) {
            r.setFechaInicio(new Date(ti.getTime()));
        }
        
        Timestamp tf = rs.getTimestamp("fecha_fin");
        if (tf != null) {
            r.setFechaFin(new Date(tf.getTime()));
        }
        
        r.setLatitudInicio(rs.getDouble("latitud_inicio"));
        r.setLongitudInicio(rs.getDouble("longitud_inicio"));
        r.setLatitudFin(rs.getDouble("latitud_fin"));
        r.setLongitudFin(rs.getDouble("longitud_fin"));
        r.setDistanciaKm(rs.getDouble("distancia_km"));
        r.setTiempoMinutos(rs.getInt("tiempo_minutos"));
        r.setTipoServicio(rs.getString("tipo_servicio"));
        
        int sid = rs.getInt("solicitud_id");
        if (rs.wasNull()) {
            r.setSolicitudId(null);
        } else {
            r.setSolicitudId(sid);
        }
        
        return r;
    }
}
