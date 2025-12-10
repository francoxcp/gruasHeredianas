package gruasheredianas.camiones.dao;

import gruasheredianas.camiones.model.Camion;
import gruasheredianas.conectionDB.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author franco
 */
public class CamionDAO {
    
    public boolean registrar(Camion camion) {
        String sql = "INSERT INTO camiones (placa, modelo, anio, kilometraje, estado, consumo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, camion.getPlaca());
            ps.setString(2, camion.getModelo());
            ps.setInt(3, camion.getAnio());
            ps.setDouble(4, camion.getKilometraje());
            ps.setString(5, camion.getEstado());
            ps.setDouble(6, camion.getConsumoCombustible());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar camión: " + e.getMessage());
            return false;
        }
    }

    public List<Camion> listar() {
        List<Camion> lista = new ArrayList<>();
        String sql = "SELECT * FROM camiones";
        try (Connection con = ConexionDB.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Camion c = new Camion();
                c.setId(rs.getInt("id"));
                c.setPlaca(rs.getString("placa"));
                c.setModelo(rs.getString("modelo"));
                c.setAnio(rs.getInt("anio"));
                c.setKilometraje(rs.getDouble("kilometraje"));
                c.setEstado(rs.getString("estado"));
                c.setConsumoCombustible(rs.getDouble("consumo"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar camiones: " + e.getMessage());
        }
        return lista;
    }

    public Camion findById(int camionId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public double kmsRecorridos(int camionId, java.util.Date desde, java.util.Date hasta) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
