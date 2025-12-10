package gruasheredianas.solicitudyasignacion.dao;

import gruasheredianas.conectionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class AsignacionDAO {
  public static void registrarAsignacion(int idSolicitud, int idCamion, int idOperador) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
           
            con = ConexionDB.getConnection();

            // Insertar en la tabla asignaciones
            String sql = "INSERT INTO asignaciones (id_solicitud, id_camion, id_operador) VALUES (?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idSolicitud);
            ps.setInt(2, idCamion);
            ps.setInt(3, idOperador);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Asignación registrada correctamente");

                // Opcional: actualizar estado de la solicitud a "Asignada"
                String sqlActualizar = "UPDATE solicitudes SET estado = 'Asignada' WHERE id = ?";
                PreparedStatement ps2 = con.prepareStatement(sqlActualizar);
                ps2.setInt(1, idSolicitud);
                ps2.executeUpdate();
                ps2.close();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo registrar la asignación");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar asignación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
  
}
