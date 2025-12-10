package gruasheredianas.finanzas.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.finanzas.model.movimientoFinanciero;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author franco
 */
public class FinanzasDAO {
     public boolean registrarMovimiento(movimientoFinanciero m) {
        String sql = "INSERT INTO finanzas (camion_id, tipo, descripcion, monto, fecha, creado_por, referencia, datos_sensibles) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (m.getCamionId() == null) ps.setNull(1, Types.INTEGER);
            else ps.setInt(1, m.getCamionId());
            ps.setString(2, m.getTipo());
            ps.setString(3, m.getDescripcion());
            ps.setDouble(4, m.getMonto());
            if (m.getFecha() == null) ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            else ps.setTimestamp(5, new Timestamp(m.getFecha().getTime()));
            if (m.getCreadoPor() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, m.getCreadoPor());
            ps.setString(7, m.getReferencia());
            if (m.getDatosSensibles() == null) ps.setNull(8, Types.VARBINARY);
            else ps.setBytes(8, m.getDatosSensibles());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            try (ResultSet generated = ps.getGeneratedKeys()) {
                if (generated.next()) m.setId(generated.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            System.err.println("Error registrarMovimiento: " + ex.getMessage());
            return false;
        }
    }

    public List<movimientoFinanciero> listarPorPeriodo(Date desde, Date hasta) {
        List<movimientoFinanciero> lista = new ArrayList<>();
        String sql = "SELECT * FROM finanzas WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(desde.getTime()));
            ps.setTimestamp(2, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                movimientoFinanciero m = mapRow(rs);
                lista.add(m);
            }
        } catch (SQLException ex) {
            System.err.println("Error listarPorPeriodo: " + ex.getMessage());
        }
        return lista;
    }

    public List<movimientoFinanciero> listarPorCamion(int camionId, Date desde, Date hasta) {
        List<movimientoFinanciero> lista = new ArrayList<>();
        String sql = "SELECT * FROM finanzas WHERE camion_id = ? AND fecha BETWEEN ? AND ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));
        } catch (SQLException ex) {
            System.err.println("Error listarPorCamion: " + ex.getMessage());
        }
        return lista;
    }

    public double totalPorCamion(int camionId, Date desde, Date hasta) {
        String sql = "SELECT SUM(CASE WHEN tipo='INGRESO' THEN monto ELSE -monto END) AS total FROM finanzas WHERE camion_id = ? AND fecha BETWEEN ? AND ?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ps.setTimestamp(2, new Timestamp(desde.getTime()));
            ps.setTimestamp(3, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException ex) {
            System.err.println("Error totalPorCamion: " + ex.getMessage());
        }
        return 0.0;
    }

    private movimientoFinanciero mapRow(ResultSet rs) throws SQLException {
        movimientoFinanciero m = new movimientoFinanciero();
        m.setId(rs.getInt("id"));
        int cid = rs.getInt("camion_id"); if (rs.wasNull()) m.setCamionId(null); else m.setCamionId(cid);
        m.setTipo(rs.getString("tipo"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setMonto(rs.getDouble("monto"));
        Timestamp t = rs.getTimestamp("fecha"); if (t != null) m.setFecha(new Date(t.getTime()));
        int cp = rs.getInt("creado_por"); if (rs.wasNull()) m.setCreadoPor(null); else m.setCreadoPor(cp);
        m.setReferencia(rs.getString("referencia"));
        byte[] ds = rs.getBytes("datos_sensibles"); m.setDatosSensibles(ds);
        return m;
    }
}
