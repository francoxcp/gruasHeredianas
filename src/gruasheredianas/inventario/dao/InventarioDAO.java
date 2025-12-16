package gruasheredianas.inventario.dao;

import gruasheredianas.conectionDB.ConexionDB;
import gruasheredianas.inventario.model.ArticuloInventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de artículos de inventario en la base de datos.
 * Implementa operaciones CRUD y consultas especializadas.
 * 
 * @author franco
 */
public class InventarioDAO {
    
    /**
     * Registra un nuevo artículo en el inventario.
     * 
     * @param articulo Artículo a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public boolean registrar(ArticuloInventario articulo) {
        String sql = "INSERT INTO inventario (nombre, tipo, descripcion, cantidad, stock_minimo, " +
                     "ubicacion, precio_unitario, fecha_registro, fecha_ultima_actualizacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, articulo.getNombre());
            ps.setString(2, articulo.getTipo());
            ps.setString(3, articulo.getDescripcion());
            ps.setInt(4, articulo.getCantidad());
            ps.setInt(5, articulo.getStockMinimo());
            ps.setString(6, articulo.getUbicacion());
            ps.setDouble(7, articulo.getPrecioUnitario());
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar artículo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza la cantidad de un artículo en el inventario.
     * 
     * @param id ID del artículo
     * @param nuevaCantidad Nueva cantidad
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarCantidad(int id, int nuevaCantidad) {
        String sql = "UPDATE inventario SET cantidad = ?, fecha_ultima_actualizacion = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevaCantidad);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cantidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Reduce la cantidad de un artículo (uso de inventario).
     * 
     * @param id ID del artículo
     * @param cantidadUsada Cantidad a reducir
     * @return true si la operación fue exitosa
     */
    public boolean usarArticulo(int id, int cantidadUsada) {
        String sql = "UPDATE inventario SET cantidad = cantidad - ?, " +
                     "fecha_ultima_actualizacion = ? WHERE id = ? AND cantidad >= ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidadUsada);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, id);
            ps.setInt(4, cantidadUsada);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al usar artículo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Incrementa la cantidad de un artículo (reposición de inventario).
     * 
     * @param id ID del artículo
     * @param cantidadRepuesta Cantidad a agregar
     * @return true si la operación fue exitosa
     */
    public boolean reponerArticulo(int id, int cantidadRepuesta) {
        String sql = "UPDATE inventario SET cantidad = cantidad + ?, " +
                     "fecha_ultima_actualizacion = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cantidadRepuesta);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setInt(3, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al reponer artículo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lista todos los artículos del inventario.
     * 
     * @return Lista de artículos
     */
    public List<ArticuloInventario> listar() {
        List<ArticuloInventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ArticuloInventario articulo = new ArticuloInventario();
                articulo.setId(rs.getInt("id"));
                articulo.setNombre(rs.getString("nombre"));
                articulo.setTipo(rs.getString("tipo"));
                articulo.setDescripcion(rs.getString("descripcion"));
                articulo.setCantidad(rs.getInt("cantidad"));
                articulo.setStockMinimo(rs.getInt("stock_minimo"));
                articulo.setUbicacion(rs.getString("ubicacion"));
                articulo.setPrecioUnitario(rs.getDouble("precio_unitario"));
                articulo.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                articulo.setFechaUltimaActualizacion(rs.getTimestamp("fecha_ultima_actualizacion"));
                lista.add(articulo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar inventario: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Obtiene artículos con stock bajo (cantidad <= stock mínimo).
     * 
     * @return Lista de artículos con stock bajo
     */
    public List<ArticuloInventario> obtenerArticulosStockBajo() {
        List<ArticuloInventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE cantidad <= stock_minimo";
        try (Connection con = ConexionDB.getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ArticuloInventario articulo = new ArticuloInventario();
                articulo.setId(rs.getInt("id"));
                articulo.setNombre(rs.getString("nombre"));
                articulo.setTipo(rs.getString("tipo"));
                articulo.setDescripcion(rs.getString("descripcion"));
                articulo.setCantidad(rs.getInt("cantidad"));
                articulo.setStockMinimo(rs.getInt("stock_minimo"));
                articulo.setUbicacion(rs.getString("ubicacion"));
                articulo.setPrecioUnitario(rs.getDouble("precio_unitario"));
                articulo.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                articulo.setFechaUltimaActualizacion(rs.getTimestamp("fecha_ultima_actualizacion"));
                lista.add(articulo);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener artículos con stock bajo: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Obtiene un artículo por su ID.
     * 
     * @param id ID del artículo
     * @return Artículo encontrado o null si no existe
     */
    public ArticuloInventario obtenerPorId(int id) {
        String sql = "SELECT * FROM inventario WHERE id = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ArticuloInventario articulo = new ArticuloInventario();
                    articulo.setId(rs.getInt("id"));
                    articulo.setNombre(rs.getString("nombre"));
                    articulo.setTipo(rs.getString("tipo"));
                    articulo.setDescripcion(rs.getString("descripcion"));
                    articulo.setCantidad(rs.getInt("cantidad"));
                    articulo.setStockMinimo(rs.getInt("stock_minimo"));
                    articulo.setUbicacion(rs.getString("ubicacion"));
                    articulo.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    articulo.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    articulo.setFechaUltimaActualizacion(rs.getTimestamp("fecha_ultima_actualizacion"));
                    return articulo;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener artículo por ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Lista artículos por tipo.
     * 
     * @param tipo Tipo de artículo (REPUESTO, HERRAMIENTA, EQUIPO)
     * @return Lista de artículos del tipo especificado
     */
    public List<ArticuloInventario> listarPorTipo(String tipo) {
        List<ArticuloInventario> lista = new ArrayList<>();
        String sql = "SELECT * FROM inventario WHERE tipo = ?";
        try (Connection con = ConexionDB.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ArticuloInventario articulo = new ArticuloInventario();
                    articulo.setId(rs.getInt("id"));
                    articulo.setNombre(rs.getString("nombre"));
                    articulo.setTipo(rs.getString("tipo"));
                    articulo.setDescripcion(rs.getString("descripcion"));
                    articulo.setCantidad(rs.getInt("cantidad"));
                    articulo.setStockMinimo(rs.getInt("stock_minimo"));
                    articulo.setUbicacion(rs.getString("ubicacion"));
                    articulo.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    articulo.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    articulo.setFechaUltimaActualizacion(rs.getTimestamp("fecha_ultima_actualizacion"));
                    lista.add(articulo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar por tipo: " + e.getMessage());
        }
        return lista;
    }
}
