package gruasheredianas.inventario.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.inventario.model.MovimientoInventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO para gestión de movimientos de inventario
 * @author franco
 */
public class MovimientoInventarioDAO {
    
    /**
     * Registra un movimiento de inventario
     */
    public boolean registrar(MovimientoInventario movimiento) {
        String sql = "INSERT INTO movimientos_inventario (articulo_id, tipo_movimiento, cantidad, motivo, fecha, usuario_id, referencia) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, movimiento.getArticuloId());
            ps.setString(2, movimiento.getTipoMovimiento());
            ps.setInt(3, movimiento.getCantidad());
            ps.setString(4, movimiento.getMotivo());
            
            if (movimiento.getFecha() == null) {
                ps.setTimestamp(5, new Timestamp(new Date().getTime()));
            } else {
                ps.setTimestamp(5, new Timestamp(movimiento.getFecha().getTime()));
            }
            
            if (movimiento.getUsuarioId() == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, movimiento.getUsuarioId());
            }
            
            ps.setString(7, movimiento.getReferencia());
            
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            
            try (ResultSet generated = ps.getGeneratedKeys()) {
                if (generated.next()) {
                    movimiento.setId(generated.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar movimiento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista movimientos por artículo
     */
    public List<MovimientoInventario> listarPorArticulo(int articuloId) {
        List<MovimientoInventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos_inventario WHERE articulo_id = ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, articuloId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista movimientos por periodo
     */
    public List<MovimientoInventario> listarPorPeriodo(Date desde, Date hasta) {
        List<MovimientoInventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos_inventario WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(desde.getTime()));
            ps.setTimestamp(2, new Timestamp(hasta.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar movimientos por periodo: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Mapea un ResultSet a un objeto MovimientoInventario
     */
    private MovimientoInventario mapRow(ResultSet rs) throws SQLException {
        MovimientoInventario m = new MovimientoInventario();
        m.setId(rs.getInt("id"));
        m.setArticuloId(rs.getInt("articulo_id"));
        m.setTipoMovimiento(rs.getString("tipo_movimiento"));
        m.setCantidad(rs.getInt("cantidad"));
        m.setMotivo(rs.getString("motivo"));
        
        Timestamp t = rs.getTimestamp("fecha");
        if (t != null) {
            m.setFecha(new Date(t.getTime()));
        }
        
        int uid = rs.getInt("usuario_id");
        if (rs.wasNull()) {
            m.setUsuarioId(null);
        } else {
            m.setUsuarioId(uid);
        }
        
        m.setReferencia(rs.getString("referencia"));
        return m;
    }
}
