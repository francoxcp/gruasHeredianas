package gruasheredianas.polizas.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.polizas.model.Poliza;
import gruasheredianas.polizas.model.RenovacionPoliza;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO para gestión de pólizas de seguro
 * @author franco
 */
public class PolizaDAO {
    
    /**
     * Registra una nueva póliza
     */
    public boolean registrar(Poliza poliza) {
        String sql = "INSERT INTO polizas (camion_id, numero_poliza, aseguradora, tipo_cobertura, monto_asegurado, prima, fecha_inicio, fecha_vencimiento, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, poliza.getCamionId());
            ps.setString(2, poliza.getNumeroPoliza());
            ps.setString(3, poliza.getAseguradora());
            ps.setString(4, poliza.getTipoCobertura());
            ps.setDouble(5, poliza.getMontoAsegurado());
            ps.setDouble(6, poliza.getPrima());
            ps.setDate(7, new java.sql.Date(poliza.getFechaInicio().getTime()));
            ps.setDate(8, new java.sql.Date(poliza.getFechaVencimiento().getTime()));
            ps.setString(9, poliza.getEstado());
            ps.setString(10, poliza.getObservaciones());
            
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            
            try (ResultSet generated = ps.getGeneratedKeys()) {
                if (generated.next()) {
                    poliza.setId(generated.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar póliza: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza una póliza existente
     */
    public boolean actualizar(Poliza poliza) {
        String sql = "UPDATE polizas SET camion_id=?, numero_poliza=?, aseguradora=?, tipo_cobertura=?, monto_asegurado=?, prima=?, fecha_inicio=?, fecha_vencimiento=?, estado=?, observaciones=? WHERE id=?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, poliza.getCamionId());
            ps.setString(2, poliza.getNumeroPoliza());
            ps.setString(3, poliza.getAseguradora());
            ps.setString(4, poliza.getTipoCobertura());
            ps.setDouble(5, poliza.getMontoAsegurado());
            ps.setDouble(6, poliza.getPrima());
            ps.setDate(7, new java.sql.Date(poliza.getFechaInicio().getTime()));
            ps.setDate(8, new java.sql.Date(poliza.getFechaVencimiento().getTime()));
            ps.setString(9, poliza.getEstado());
            ps.setString(10, poliza.getObservaciones());
            ps.setInt(11, poliza.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar póliza: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todas las pólizas
     */
    public List<Poliza> listar() {
        List<Poliza> lista = new ArrayList<>();
        String sql = "SELECT * FROM polizas ORDER BY fecha_vencimiento";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pólizas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca una póliza por ID
     */
    public Poliza buscarPorId(int id) {
        String sql = "SELECT * FROM polizas WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar póliza: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista pólizas de una grúa específica
     */
    public List<Poliza> listarPorCamion(int camionId) {
        List<Poliza> lista = new ArrayList<>();
        String sql = "SELECT * FROM polizas WHERE camion_id = ? ORDER BY fecha_vencimiento DESC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, camionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pólizas por camión: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista pólizas vigentes
     */
    public List<Poliza> listarVigentes() {
        List<Poliza> lista = new ArrayList<>();
        String sql = "SELECT * FROM polizas WHERE estado = 'VIGENTE' ORDER BY fecha_vencimiento";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pólizas vigentes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista pólizas próximas a vencer (30 días)
     */
    public List<Poliza> listarProximasAVencer() {
        List<Poliza> lista = new ArrayList<>();
        String sql = "SELECT * FROM polizas WHERE estado = 'VIGENTE' AND fecha_vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) ORDER BY fecha_vencimiento";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pólizas próximas a vencer: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Registra una renovación de póliza
     */
    public boolean registrarRenovacion(RenovacionPoliza renovacion) {
        String sql = "INSERT INTO renovaciones_poliza (poliza_id, fecha_renovacion, nueva_fecha_vencimiento, nueva_prima, observaciones, usuario_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, renovacion.getPolizaId());
            
            if (renovacion.getFechaRenovacion() == null) {
                ps.setDate(2, new java.sql.Date(new Date().getTime()));
            } else {
                ps.setDate(2, new java.sql.Date(renovacion.getFechaRenovacion().getTime()));
            }
            
            ps.setDate(3, new java.sql.Date(renovacion.getNuevaFechaVencimiento().getTime()));
            ps.setDouble(4, renovacion.getNuevaPrima());
            ps.setString(5, renovacion.getObservaciones());
            
            if (renovacion.getUsuarioId() == null) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, renovacion.getUsuarioId());
            }
            
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            
            try (ResultSet generated = ps.getGeneratedKeys()) {
                if (generated.next()) {
                    renovacion.setId(generated.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar renovación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista renovaciones de una póliza
     */
    public List<RenovacionPoliza> listarRenovaciones(int polizaId) {
        List<RenovacionPoliza> lista = new ArrayList<>();
        String sql = "SELECT * FROM renovaciones_poliza WHERE poliza_id = ? ORDER BY fecha_renovacion DESC";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, polizaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRenovacionRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar renovaciones: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Mapea un ResultSet a un objeto Poliza
     */
    private Poliza mapRow(ResultSet rs) throws SQLException {
        Poliza p = new Poliza();
        p.setId(rs.getInt("id"));
        p.setCamionId(rs.getInt("camion_id"));
        p.setNumeroPoliza(rs.getString("numero_poliza"));
        p.setAseguradora(rs.getString("aseguradora"));
        p.setTipoCobertura(rs.getString("tipo_cobertura"));
        p.setMontoAsegurado(rs.getDouble("monto_asegurado"));
        p.setPrima(rs.getDouble("prima"));
        
        java.sql.Date fi = rs.getDate("fecha_inicio");
        if (fi != null) {
            p.setFechaInicio(new Date(fi.getTime()));
        }
        
        java.sql.Date fv = rs.getDate("fecha_vencimiento");
        if (fv != null) {
            p.setFechaVencimiento(new Date(fv.getTime()));
        }
        
        p.setEstado(rs.getString("estado"));
        p.setObservaciones(rs.getString("observaciones"));
        return p;
    }

    /**
     * Mapea un ResultSet a un objeto RenovacionPoliza
     */
    private RenovacionPoliza mapRenovacionRow(ResultSet rs) throws SQLException {
        RenovacionPoliza r = new RenovacionPoliza();
        r.setId(rs.getInt("id"));
        r.setPolizaId(rs.getInt("poliza_id"));
        
        java.sql.Date fr = rs.getDate("fecha_renovacion");
        if (fr != null) {
            r.setFechaRenovacion(new Date(fr.getTime()));
        }
        
        java.sql.Date nfv = rs.getDate("nueva_fecha_vencimiento");
        if (nfv != null) {
            r.setNuevaFechaVencimiento(new Date(nfv.getTime()));
        }
        
        r.setNuevaPrima(rs.getDouble("nueva_prima"));
        r.setObservaciones(rs.getString("observaciones"));
        
        int uid = rs.getInt("usuario_id");
        if (rs.wasNull()) {
            r.setUsuarioId(null);
        } else {
            r.setUsuarioId(uid);
        }
        
        return r;
    }
}
