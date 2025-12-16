package gruasheredianas.inventario.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.inventario.model.Articulo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de artículos de inventario
 * @author franco
 */
public class InventarioDAO {
    
    /**
     * Registra un nuevo artículo en el inventario
     */
    public boolean registrar(Articulo articulo) {
        String sql = "INSERT INTO inventario (codigo, nombre, categoria, descripcion, cantidad_actual, stock_minimo, precio_unitario, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getNombre());
            ps.setString(3, articulo.getCategoria());
            ps.setString(4, articulo.getDescripcion());
            ps.setInt(5, articulo.getCantidadActual());
            ps.setInt(6, articulo.getStockMinimo());
            ps.setDouble(7, articulo.getPrecioUnitario());
            ps.setString(8, articulo.getUbicacion());
            
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            
            try (ResultSet generated = ps.getGeneratedKeys()) {
                if (generated.next()) {
                    articulo.setId(generated.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar artículo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza un artículo existente
     */
    public boolean actualizar(Articulo articulo) {
        String sql = "UPDATE inventario SET codigo=?, nombre=?, categoria=?, descripcion=?, cantidad_actual=?, stock_minimo=?, precio_unitario=?, ubicacion=? WHERE id=?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getNombre());
            ps.setString(3, articulo.getCategoria());
            ps.setString(4, articulo.getDescripcion());
            ps.setInt(5, articulo.getCantidadActual());
            ps.setInt(6, articulo.getStockMinimo());
            ps.setDouble(7, articulo.getPrecioUnitario());
            ps.setString(8, articulo.getUbicacion());
            ps.setInt(9, articulo.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar artículo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos los artículos del inventario
     */
    public List<Articulo> listar() {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario ORDER BY nombre";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar artículos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un artículo por ID
     */
    public Articulo buscarPorId(int id) {
        String sql = "SELECT * FROM inventario WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar artículo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista artículos con stock bajo (cantidad <= stock mínimo)
     */
    public List<Articulo> listarStockBajo() {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE cantidad_actual <= stock_minimo ORDER BY cantidad_actual";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar stock bajo: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza la cantidad de un artículo
     */
    public boolean actualizarCantidad(int articuloId, int nuevaCantidad) {
        String sql = "UPDATE inventario SET cantidad_actual = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, articuloId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cantidad: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Articulo
     */
    private Articulo mapRow(ResultSet rs) throws SQLException {
        Articulo a = new Articulo();
        a.setId(rs.getInt("id"));
        a.setCodigo(rs.getString("codigo"));
        a.setNombre(rs.getString("nombre"));
        a.setCategoria(rs.getString("categoria"));
        a.setDescripcion(rs.getString("descripcion"));
        a.setCantidadActual(rs.getInt("cantidad_actual"));
        a.setStockMinimo(rs.getInt("stock_minimo"));
        a.setPrecioUnitario(rs.getDouble("precio_unitario"));
        a.setUbicacion(rs.getString("ubicacion"));
        return a;
    }
}
