package gruasheredianas.solicitudyasignacion.dao;

import gruasheredianas.camiones.model.EstadoServicio;
import gruasheredianas.conectionDB.ConexionDB;
import static gruasheredianas.conectionDB.ConexionDB.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EstadoServicioDAO {
    
     public static boolean crearEstadoServicio(int idSolicitud) {
        try (Connection con = getConnection()) {
            String sql = "INSERT INTO estado_servicio (id_solicitud) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSolicitud);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     
     public static EstadoServicio buscarPorSolicitud(int idSolicitud) {
        EstadoServicio es = null;
        String sql = "SELECT s.estado, c.placa AS camion, o.nombre AS operador, " +
                     "e.hora_salida, e.hora_llegada, e.hora_finalizacion " +
                     "FROM solicitudes s " +
                     "LEFT JOIN asignaciones a ON s.id = a.id_solicitud " +
                     "LEFT JOIN camiones c ON a.id_camion = c.id " +
                     "LEFT JOIN operadores o ON a.id_operador = o.id " +
                     "LEFT JOIN estado_servicio e ON s.id = e.id_solicitud " +
                     "WHERE s.id = ?";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                es = new EstadoServicio();
                es.setIdSolicitud(idSolicitud);
                es.setCamion(rs.getString("camion"));
                es.setOperador(rs.getString("operador"));
                es.setEstado(rs.getString("estado"));
                es.setHoraSalida(rs.getString("hora_salida"));
                es.setHoraLlegada(rs.getString("hora_llegada"));
                es.setHoraFinalizacion(rs.getString("hora_finalizacion"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return es;
    }

    // Actualizar estado de la solicitud
   public static boolean actualizarEstado(int idSolicitud, String nuevoEstado, 
                                      String horaSalida, String horaLlegada) {
    String sqlCheck = "SELECT COUNT(*) FROM estado_servicio WHERE id_solicitud = ?";
    String sqlInsert = "INSERT INTO estado_servicio (id_solicitud) VALUES (?)";
    String sqlUpdateSolicitudes = "UPDATE solicitudes SET estado = ? WHERE id = ?";
    String sqlUpdateEstadoServicio = "UPDATE estado_servicio SET hora_salida = ?, hora_llegada = ? WHERE id_solicitud = ?";

    try (Connection con = ConexionDB.getConnection()) {
        // 1. Verificar existencia en estado_servicio
        try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, idSolicitud);
            ResultSet rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                    psInsert.setInt(1, idSolicitud);
                    psInsert.executeUpdate();
                }
            }
        }

        // 2. Actualizar estado en solicitudes
        try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdateSolicitudes)) {
            psUpdate.setString(1, nuevoEstado);
            psUpdate.setInt(2, idSolicitud);
            psUpdate.executeUpdate();
        }

        // 3. Actualizar horas en estado_servicio si se proporcionan
        try (PreparedStatement psUpdateES = con.prepareStatement(sqlUpdateEstadoServicio)) {
            psUpdateES.setString(1, horaSalida);   // puede ser null
            psUpdateES.setString(2, horaLlegada);  // puede ser null
            psUpdateES.setInt(3, idSolicitud);
            psUpdateES.executeUpdate();
        }

        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
   
   
    public static boolean finalizarServicio(int idSolicitud) {
        String sql = "UPDATE solicitudes s " +
                     "JOIN estado_servicio e ON s.id = e.id_solicitud " +
                     "SET s.estado='Finalizada', e.hora_finalizacion=NOW() " +
                     "WHERE s.id=?";
        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSolicitud);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}