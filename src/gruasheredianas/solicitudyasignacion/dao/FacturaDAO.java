
package gruasheredianas.solicitudyasignacion.dao;

import gruasheredianas.camiones.model.Factura;
import gruasheredianas.conectionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FacturaDAO {
    private Connection conn;

    public FacturaDAO(Connection conn) {
        this.conn = conn;
    }

  
    public boolean insertarFactura(Factura f) {
        String sql = "INSERT INTO factura (nombre_cliente, cedula, fecha, telefono, kilometros_recorridos, tarifa_km, otros_cargos, observaciones, subtotal, iva, total) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNombreCliente());
            ps.setString(2, f.getCedula());
            ps.setDate(3, new java.sql.Date(f.getFecha().getTime()));
            ps.setString(4, f.getTelefono());
            ps.setDouble(5, f.getKilometrosRecorridos());
            ps.setDouble(6, f.getTarifaPorKm());
            ps.setDouble(7, f.getOtrosCargos());
            ps.setString(8, f.getObservaciones());
            ps.setDouble(9, f.getSubtotal());
            ps.setDouble(10, f.getIva());
            ps.setDouble(11, f.getTotal());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

   
    public List<Factura> listarFacturas() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM factura";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Factura f = new Factura(
                    rs.getInt("id"),
                    rs.getString("nombre_cliente"),
                    rs.getString("cedula"),
                    rs.getDate("fecha"),
                    rs.getString("telefono"),
                    rs.getDouble("kilometros_recorridos"),
                    rs.getDouble("tarifa_km"),
                    rs.getDouble("otros_cargos"),
                    rs.getString("observaciones"),
                    rs.getDouble("subtotal"),
                    rs.getDouble("iva"),
                    rs.getDouble("total")
                );
                lista.add(f);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
}
