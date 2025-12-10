package gruasheredianas.solicitudyasignacion.dao;

import java.sql.Statement;
import gruasheredianas.camiones.model.Solicitud;
import gruasheredianas.conectionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitudDAO {
      public boolean registrar(Solicitud s) {
        String sqlInsertCliente = "INSERT INTO clientes (nombre, telefono, direccion) VALUES (?, ?, ?)";
        String sqlInsertSolicitud = "INSERT INTO solicitudes "
                                  + "(id_cliente, direccion, tipo_servicio, observaciones, estado, fecha_solicitud) "
                                  + "VALUES (?, ?, ?, ?, 'Pendiente', CURRENT_TIMESTAMP)";

        try (Connection con = ConexionDB.getConnection()) {
            con.setAutoCommit(false); // Inicia transacción

            // 1️⃣ Insertar cliente
            int idCliente = 0;
            try (PreparedStatement psCliente = con.prepareStatement(sqlInsertCliente, Statement.RETURN_GENERATED_KEYS)) {
                psCliente.setString(1, s.getNombreCliente());
                psCliente.setString(2, s.getTelefono());
                psCliente.setString(3, s.getDireccionIncidente()); // Dirección del cliente (puede ser diferente de la del incidente)
                psCliente.executeUpdate();

                try (ResultSet rs = psCliente.getGeneratedKeys()) {
                    if (rs.next()) {
                        idCliente = rs.getInt(1); // ID generado
                    } else {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 2️⃣ Insertar solicitud usando el id_cliente
            try (PreparedStatement psSolicitud = con.prepareStatement(sqlInsertSolicitud, Statement.RETURN_GENERATED_KEYS)) {
                psSolicitud.setInt(1, idCliente);
                psSolicitud.setString(2, s.getDireccionIncidente()); // Dirección del incidente
                psSolicitud.setString(3, s.getTipoServicio());
                psSolicitud.setString(4, s.getDescripcion());
                psSolicitud.executeUpdate();

                // Obtener número de solicitud generado
                try (ResultSet rs = psSolicitud.getGeneratedKeys()) {
                    if (rs.next()) {
                        int numeroSolicitud = rs.getInt(1);
                        System.out.println("Solicitud registrada con Nº: " + numeroSolicitud);
                    }
                }
            }

            con.commit(); // Confirmar transacción
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
      
public static List<Solicitud> listarSolicitudes(String filtroTexto, String filtroEstado) {
  
    List<Solicitud> lista = new ArrayList<>();

    String sql = "SELECT s.id, c.nombre AS nombre_cliente, s.direccion, s.tipo_servicio, "
               + "s.observaciones, s.estado, s.fecha_solicitud "
               + "FROM solicitudes s "
               + "JOIN clientes c ON s.id_cliente = c.id "
               + "WHERE 1=1 ";

    if (filtroTexto != null && !filtroTexto.isEmpty()) {
        sql += " AND (c.nombre LIKE ? OR s.id = ?) ";
    }

    if (!filtroEstado.equalsIgnoreCase("Todas")) {
        sql += " AND s.estado = ? ";
    }

    sql += " ORDER BY s.fecha_solicitud DESC";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        int index = 1;

        if (filtroTexto != null && !filtroTexto.isEmpty()) {
            ps.setString(index++, "%" + filtroTexto + "%"); // filtro por nombre
            try {
                int idBusqueda = Integer.parseInt(filtroTexto);
                ps.setInt(index++, idBusqueda); // filtro por ID
            } catch (NumberFormatException e) {
                ps.setInt(index++, -1); // si no es número, ningún ID coincidirá
            }
        }

        if (!filtroEstado.equalsIgnoreCase("Todas")) {
            ps.setString(index++, filtroEstado);
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Solicitud s = new Solicitud();
            s.setId(rs.getInt("id"));
            s.setNombreCliente(rs.getString("nombre_cliente"));
            s.setDireccionIncidente(rs.getString("direccion"));
            s.setDescripcion(rs.getString("observaciones"));
            s.setTipoServicio(rs.getString("tipo_servicio"));
            s.setEstado(rs.getString("estado"));
            s.setFecha(rs.getString("fecha_solicitud"));

            lista.add(s);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return lista;
}
}