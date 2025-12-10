package gruasheredianas.usuarios.dao;

import gruasheredianas.usuarios.model.Usuario;
import gruasheredianas.conectionDB.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;


public class UsuarioDAO {
    
    //registrar un nuevo usuario (contraseña encriptada)
    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, usuario, password_hash, rol, activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsuario());
            ps.setString(3, BCrypt.hashpw(u.getContrasena(), BCrypt.gensalt())); //encriptacion
            ps.setString(4, u.getRol());
            ps.setBoolean(5, u.isActivo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    //validar login
    public Usuario login(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE usuario=? AND activo=1";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hash = rs.getString("contrasena");
                if (BCrypt.checkpw(contrasena, hash)) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setRol(rs.getString("rol"));
                    u.setActivo(rs.getBoolean("activo"));
                    registrarAuditoria(u.getId(), "LOGIN EXITOSO");
                    return u;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en login: " + e.getMessage());
        }
        return null;
    }

    // Registrar auditoría
    private void registrarAuditoria(int usuarioId, String accion) {
        String sql = "INSERT INTO auditoria (usuario_id, accion) VALUES (?, ?)";
        try (Connection con = ConexionDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setString(2, accion);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en auditoría: " + e.getMessage());
        }
    }
    
    
 public List<Usuario> listarUsuarios() {
    List<Usuario> lista = new ArrayList<>();
    String sql = "SELECT id, nombre, usuario, rol, activo FROM usuarios";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNombre(rs.getString("nombre"));
            u.setUsuario(rs.getString("usuario"));
            u.setRol(rs.getString("rol"));
            u.setActivo(rs.getBoolean("activo"));
            lista.add(u);
        }

    } catch (SQLException e) {
        System.out.println("Error en listarUsuarios(): " + e.getMessage());
    }

    return lista;
}
public List<Usuario> buscarUsuarios(String texto) {
    List<Usuario> lista = new ArrayList<>();
    String sql = "SELECT id, nombre, usuario, rol, activo FROM usuarios "
               + "WHERE nombre LIKE ? OR usuario LIKE ?";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, "%" + texto + "%");
        ps.setString(2, "%" + texto + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNombre(rs.getString("nombre"));
            u.setUsuario(rs.getString("usuario"));
            u.setRol(rs.getString("rol"));
            u.setActivo(rs.getBoolean("activo"));
            lista.add(u);
        }

    } catch (SQLException e) {
        System.out.println("Error en buscarUsuarios(): " + e.getMessage());
    }

    return lista;
}
public boolean desactivarUsuario(int id) {
    String sql = "UPDATE usuarios SET activo = 0 WHERE id = ?";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        return filas > 0;

    } catch (SQLException e) {
        System.out.println("Error al desactivar usuario: " + e.getMessage());
        return false;
    }
}
public boolean activarUsuario(int id) {
    String sql = "UPDATE usuarios SET activo = 1 WHERE id = ?";

    try (Connection con = ConexionDB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);
        int filas = ps.executeUpdate();
        return filas > 0;

    } catch (SQLException e) {
        System.out.println("Error al activar usuario: " + e.getMessage());
        return false;
    }
}

}
