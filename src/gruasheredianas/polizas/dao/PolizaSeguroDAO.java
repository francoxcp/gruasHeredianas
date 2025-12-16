package gruasheredianas.polizas.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.polizas.model.PolizaSeguro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de pólizas de seguro en la base de datos.
 * 
 * @author franco
 */
public class PolizaSeguroDAO {
    
    /**
     * Registra una nueva póliza de seguro.
     * 
     * @param poliza Póliza a registrar
     * @return true si el registro fue exitoso
     */
    public boolean registrar(PolizaSeguro poliza) {
        String sql = "INSERT INTO poliza_seguro (camion_id, numero_poliza, aseguradora, " +
                     "fecha_inicio, fecha_vencimiento, tipo_cobertura, monto_cobertura, " +
                     "prima_mensual, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, poliza.getCamionId());
            ps.setString(2, poliza.getNumeroPoliza());
            ps.setString(3, poliza.getAseguradora());
            ps.setDate(4, new java.sql.Date(poliza.getFechaInicio().getTime()));
            ps.setDate(5, new java.sql.Date(poliza.getFechaVencimiento().getTime()));
            ps.setString(6, poliza.getTipoCobertura());
            ps.setDouble(7, poliza.getMontoCobertura());
            ps.setDouble(8, poliza.getPrimaMensual());
            ps.setString(9, poliza.getEstado());
            ps.setString(10, poliza.getObservaciones());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar póliza: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza el estado de una póliza.
     * 
     * @param id ID de la póliza
     * @param nuevoEstado Nuevo estado
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE poliza_seguro SET estado = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de póliza: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza la fecha de vencimiento de una póliza (renovación).
     * 
     * @param id ID de la póliza
     * @param nuevaFechaVencimiento Nueva fecha de vencimiento
     * @return true si la actualización fue exitosa
     */
    public boolean renovarPoliza(int id, java.util.Date nuevaFechaVencimiento) {
        String sql = "UPDATE poliza_seguro SET fecha_vencimiento = ?, estado = 'ACTIVA' WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(nuevaFechaVencimiento.getTime()));
            ps.setInt(2, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al renovar póliza: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lista todas las pólizas de seguro.
     * 
     * @return Lista de pólizas
     */
    public List<PolizaSeguro> listar() {
        List<PolizaSeguro> lista = new ArrayList<>();
        String sql = "SELECT * FROM poliza_seguro";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerPolizaDeResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pólizas: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Obtiene una póliza por su ID.
     * 
     * @param id ID de la póliza
     * @return Póliza encontrada o null si no existe
     */
    public PolizaSeguro obtenerPorId(int id) {
        String sql = "SELECT * FROM poliza_seguro WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraerPolizaDeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener póliza por ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene pólizas de una grúa específica.
     * 
     * @param camionId ID del camión
     * @return Lista de pólizas del camión
     */
    public List<PolizaSeguro> obtenerPorCamion(int camionId) {
        List<PolizaSeguro> lista = new ArrayList<>();
        String sql = "SELECT * FROM poliza_seguro WHERE camion_id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerPolizaDeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pólizas por camión: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Obtiene la póliza activa de una grúa.
     * 
     * @param camionId ID del camión
     * @return Póliza activa o null si no existe
     */
    public PolizaSeguro obtenerPolizaActiva(int camionId) {
        String sql = "SELECT * FROM poliza_seguro WHERE camion_id = ? AND estado = 'ACTIVA' " +
                     "ORDER BY fecha_vencimiento DESC LIMIT 1";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraerPolizaDeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener póliza activa: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene pólizas que vencen dentro de los próximos días especificados.
     * 
     * @param dias Número de días de anticipación
     * @return Lista de pólizas próximas a vencer
     */
    public List<PolizaSeguro> obtenerPolizasPorVencer(int dias) {
        List<PolizaSeguro> lista = new ArrayList<>();
        String sql = "SELECT * FROM poliza_seguro WHERE estado = 'ACTIVA' " +
                     "AND fecha_vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dias);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerPolizaDeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pólizas por vencer: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Obtiene pólizas vencidas.
     * 
     * @return Lista de pólizas vencidas
     */
    public List<PolizaSeguro> obtenerPolizasVencidas() {
        List<PolizaSeguro> lista = new ArrayList<>();
        String sql = "SELECT * FROM poliza_seguro WHERE estado = 'ACTIVA' " +
                     "AND fecha_vencimiento < CURDATE()";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerPolizaDeResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pólizas vencidas: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Obtiene pólizas por aseguradora.
     * 
     * @param aseguradora Nombre de la aseguradora
     * @return Lista de pólizas
     */
    public List<PolizaSeguro> obtenerPorAseguradora(String aseguradora) {
        List<PolizaSeguro> lista = new ArrayList<>();
        String sql = "SELECT * FROM poliza_seguro WHERE aseguradora = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, aseguradora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerPolizaDeResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pólizas por aseguradora: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Extrae una póliza del ResultSet.
     */
    private PolizaSeguro extraerPolizaDeResultSet(ResultSet rs) throws SQLException {
        PolizaSeguro poliza = new PolizaSeguro();
        poliza.setId(rs.getInt("id"));
        poliza.setCamionId(rs.getInt("camion_id"));
        poliza.setNumeroPoliza(rs.getString("numero_poliza"));
        poliza.setAseguradora(rs.getString("aseguradora"));
        poliza.setFechaInicio(rs.getDate("fecha_inicio"));
        poliza.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
        poliza.setTipoCobertura(rs.getString("tipo_cobertura"));
        poliza.setMontoCobertura(rs.getDouble("monto_cobertura"));
        poliza.setPrimaMensual(rs.getDouble("prima_mensual"));
        poliza.setEstado(rs.getString("estado"));
        poliza.setObservaciones(rs.getString("observaciones"));
        return poliza;
    }
}
