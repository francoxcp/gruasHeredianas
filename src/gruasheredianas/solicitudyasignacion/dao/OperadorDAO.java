package gruasheredianas.solicitudyasignacion.dao;

import gruasheredianas.conectionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;

public class OperadorDAO {
     public static void cargarOperadoresDisponibles(JComboBox<String> combo) {
        combo.removeAllItems();
        try (Connection con = ConexionDB.getConnection()) {
            String sql = "SELECT id, nombre FROM operadores";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                combo.addItem(rs.getInt("id") + " - " + rs.getString("nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obtener id a partir del combo
    public static int obtenerIdSeleccionado(JComboBox<String> combo) {
        String item = (String) combo.getSelectedItem();
        if (item != null && item.contains(" - ")) {
            return Integer.parseInt(item.split(" - ")[0]);
        }
        return -1;
    }
}
