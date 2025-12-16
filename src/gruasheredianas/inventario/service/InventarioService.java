package gruasheredianas.inventario.service;

import gruasheredianas.inventario.dao.InventarioDAO;
import gruasheredianas.inventario.dao.MovimientoInventarioDAO;
import gruasheredianas.inventario.model.Articulo;
import gruasheredianas.inventario.model.MovimientoInventario;
import java.util.Date;
import java.util.List;

/**
 * Servicio para gestión de inventario con actualización automática de existencias
 * @author franco
 */
public class InventarioService {
    
    private InventarioDAO inventarioDAO = new InventarioDAO();
    private MovimientoInventarioDAO movimientoDAO = new MovimientoInventarioDAO();

    /**
     * Registra una entrada de artículos al inventario
     * @param articuloId ID del artículo
     * @param cantidad Cantidad a ingresar
     * @param motivo Razón del ingreso
     * @param usuarioId ID del usuario que realiza el movimiento
     * @param referencia Referencia del documento (factura, orden, etc)
     * @return true si se realizó correctamente
     */
    public boolean registrarEntrada(int articuloId, int cantidad, String motivo, Integer usuarioId, String referencia) {
        // Obtener artículo actual
        Articulo articulo = inventarioDAO.buscarPorId(articuloId);
        if (articulo == null) {
            System.err.println("Artículo no encontrado");
            return false;
        }

        // Registrar movimiento
        MovimientoInventario mov = new MovimientoInventario();
        mov.setArticuloId(articuloId);
        mov.setTipoMovimiento("ENTRADA");
        mov.setCantidad(cantidad);
        mov.setMotivo(motivo);
        mov.setFecha(new Date());
        mov.setUsuarioId(usuarioId);
        mov.setReferencia(referencia);

        if (!movimientoDAO.registrar(mov)) {
            System.err.println("Error al registrar movimiento");
            return false;
        }

        // Actualizar cantidad en inventario
        int nuevaCantidad = articulo.getCantidadActual() + cantidad;
        return inventarioDAO.actualizarCantidad(articuloId, nuevaCantidad);
    }

    /**
     * Registra una salida de artículos del inventario
     * @param articuloId ID del artículo
     * @param cantidad Cantidad a sacar
     * @param motivo Razón de la salida
     * @param usuarioId ID del usuario que realiza el movimiento
     * @param referencia Referencia del documento
     * @return true si se realizó correctamente
     */
    public boolean registrarSalida(int articuloId, int cantidad, String motivo, Integer usuarioId, String referencia) {
        // Obtener artículo actual
        Articulo articulo = inventarioDAO.buscarPorId(articuloId);
        if (articulo == null) {
            System.err.println("Artículo no encontrado");
            return false;
        }

        // Verificar que hay suficiente stock
        if (articulo.getCantidadActual() < cantidad) {
            System.err.println("Stock insuficiente. Disponible: " + articulo.getCantidadActual() + ", Solicitado: " + cantidad);
            return false;
        }

        // Registrar movimiento
        MovimientoInventario mov = new MovimientoInventario();
        mov.setArticuloId(articuloId);
        mov.setTipoMovimiento("SALIDA");
        mov.setCantidad(cantidad);
        mov.setMotivo(motivo);
        mov.setFecha(new Date());
        mov.setUsuarioId(usuarioId);
        mov.setReferencia(referencia);

        if (!movimientoDAO.registrar(mov)) {
            System.err.println("Error al registrar movimiento");
            return false;
        }

        // Actualizar cantidad en inventario
        int nuevaCantidad = articulo.getCantidadActual() - cantidad;
        return inventarioDAO.actualizarCantidad(articuloId, nuevaCantidad);
    }

    /**
     * Obtiene lista de artículos con stock bajo que requieren reposición
     * @return Lista de artículos con alerta de stock
     */
    public List<Articulo> obtenerAlertasStockBajo() {
        return inventarioDAO.listarStockBajo();
    }

    /**
     * Genera reporte de inventario en tiempo real
     * @return Lista completa de artículos con sus cantidades actuales
     */
    public List<Articulo> generarReporteInventario() {
        return inventarioDAO.listar();
    }

    /**
     * Obtiene el historial de movimientos de un artículo
     * @param articuloId ID del artículo
     * @return Lista de movimientos
     */
    public List<MovimientoInventario> obtenerHistorialArticulo(int articuloId) {
        return movimientoDAO.listarPorArticulo(articuloId);
    }

    /**
     * Obtiene movimientos de inventario en un periodo
     * @param desde Fecha inicial
     * @param hasta Fecha final
     * @return Lista de movimientos
     */
    public List<MovimientoInventario> obtenerMovimientosPeriodo(Date desde, Date hasta) {
        return movimientoDAO.listarPorPeriodo(desde, hasta);
    }

    /**
     * Verifica si un artículo requiere reposición
     * @param articuloId ID del artículo
     * @return true si está en stock bajo
     */
    public boolean verificarStockBajo(int articuloId) {
        Articulo articulo = inventarioDAO.buscarPorId(articuloId);
        return articulo != null && articulo.requiereReposicion();
    }
}
