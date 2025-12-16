package gruasheredianas.inventario.service;

import gruasheredianas.inventario.dao.InventarioDAO;
import gruasheredianas.inventario.model.ArticuloInventario;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión del inventario.
 * Proporciona lógica de negocio, alertas y reportes.
 * 
 * @author franco
 */
public class InventarioService {
    
    private final InventarioDAO inventarioDAO;
    
    public InventarioService() {
        this.inventarioDAO = new InventarioDAO();
    }
    
    /**
     * Registra un nuevo artículo en el inventario.
     * 
     * @param articulo Artículo a registrar
     * @return true si el registro fue exitoso
     */
    public boolean registrarArticulo(ArticuloInventario articulo) {
        return inventarioDAO.registrar(articulo);
    }
    
    /**
     * Usa un artículo del inventario y verifica alertas de stock bajo.
     * 
     * @param id ID del artículo
     * @param cantidad Cantidad a usar
     * @return true si la operación fue exitosa
     */
    public boolean usarArticulo(int id, int cantidad) {
        boolean exito = inventarioDAO.usarArticulo(id, cantidad);
        if (exito) {
            verificarYNotificarStockBajo(id);
        }
        return exito;
    }
    
    /**
     * Repone un artículo en el inventario.
     * 
     * @param id ID del artículo
     * @param cantidad Cantidad a reponer
     * @return true si la operación fue exitosa
     */
    public boolean reponerArticulo(int id, int cantidad) {
        return inventarioDAO.reponerArticulo(id, cantidad);
    }
    
    /**
     * Actualiza la cantidad de un artículo.
     * 
     * @param id ID del artículo
     * @param nuevaCantidad Nueva cantidad
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarCantidad(int id, int nuevaCantidad) {
        boolean exito = inventarioDAO.actualizarCantidad(id, nuevaCantidad);
        if (exito) {
            verificarYNotificarStockBajo(id);
        }
        return exito;
    }
    
    /**
     * Obtiene todos los artículos del inventario.
     * 
     * @return Lista de artículos
     */
    public List<ArticuloInventario> obtenerTodosLosArticulos() {
        return inventarioDAO.listar();
    }
    
    /**
     * Obtiene artículos por tipo.
     * 
     * @param tipo Tipo de artículo
     * @return Lista de artículos del tipo especificado
     */
    public List<ArticuloInventario> obtenerArticulosPorTipo(String tipo) {
        return inventarioDAO.listarPorTipo(tipo);
    }
    
    /**
     * Obtiene un artículo por ID.
     * 
     * @param id ID del artículo
     * @return Artículo o null si no existe
     */
    public ArticuloInventario obtenerArticuloPorId(int id) {
        return inventarioDAO.obtenerPorId(id);
    }
    
    /**
     * Obtiene artículos con stock bajo.
     * 
     * @return Lista de artículos con stock bajo
     */
    public List<ArticuloInventario> obtenerArticulosConStockBajo() {
        return inventarioDAO.obtenerArticulosStockBajo();
    }
    
    /**
     * Verifica si un artículo tiene stock bajo y genera notificación.
     * 
     * @param id ID del artículo a verificar
     */
    private void verificarYNotificarStockBajo(int id) {
        ArticuloInventario articulo = inventarioDAO.obtenerPorId(id);
        if (articulo != null && articulo.tieneStockBajo()) {
            generarAlertaStockBajo(articulo);
        }
    }
    
    /**
     * Genera una alerta de stock bajo.
     * 
     * @param articulo Artículo con stock bajo
     */
    private void generarAlertaStockBajo(ArticuloInventario articulo) {
        System.out.println("¡ALERTA DE STOCK BAJO!");
        System.out.println("Artículo: " + articulo.getNombre());
        System.out.println("Tipo: " + articulo.getTipo());
        System.out.println("Cantidad actual: " + articulo.getCantidad());
        System.out.println("Stock mínimo: " + articulo.getStockMinimo());
        System.out.println("Ubicación: " + articulo.getUbicacion());
        System.out.println("Se requiere reposición inmediata.");
    }
    
    /**
     * Genera un reporte de inventario en tiempo real.
     * 
     * @return String con el reporte completo
     */
    public String generarReporteInventario() {
        List<ArticuloInventario> articulos = inventarioDAO.listar();
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("=== REPORTE DE INVENTARIO ===\n");
        reporte.append("Total de artículos: ").append(articulos.size()).append("\n\n");
        
        // Resumen por tipo
        long repuestos = articulos.stream().filter(a -> "REPUESTO".equals(a.getTipo())).count();
        long herramientas = articulos.stream().filter(a -> "HERRAMIENTA".equals(a.getTipo())).count();
        long equipos = articulos.stream().filter(a -> "EQUIPO".equals(a.getTipo())).count();
        
        reporte.append("Repuestos: ").append(repuestos).append("\n");
        reporte.append("Herramientas: ").append(herramientas).append("\n");
        reporte.append("Equipos: ").append(equipos).append("\n\n");
        
        // Artículos con stock bajo
        List<ArticuloInventario> stockBajo = articulos.stream()
                .filter(ArticuloInventario::tieneStockBajo)
                .collect(Collectors.toList());
        
        reporte.append("Artículos con stock bajo: ").append(stockBajo.size()).append("\n");
        if (!stockBajo.isEmpty()) {
            reporte.append("--- Detalle ---\n");
            for (ArticuloInventario articulo : stockBajo) {
                reporte.append("- ").append(articulo.getNombre())
                       .append(" (").append(articulo.getTipo()).append("): ")
                       .append(articulo.getCantidad()).append(" unidades\n");
            }
        }
        
        reporte.append("\n");
        
        // Valor total del inventario
        double valorTotal = articulos.stream()
                .mapToDouble(ArticuloInventario::getValorTotal)
                .sum();
        
        reporte.append("Valor total del inventario: $").append(String.format("%.2f", valorTotal)).append("\n");
        
        return reporte.toString();
    }
    
    /**
     * Verifica y retorna todas las alertas activas de stock bajo.
     * 
     * @return Lista de mensajes de alerta
     */
    public List<String> obtenerAlertasStockBajo() {
        List<ArticuloInventario> stockBajo = inventarioDAO.obtenerArticulosStockBajo();
        return stockBajo.stream()
                .map(articulo -> String.format("ALERTA: %s (%s) - Stock: %d/%d unidades en %s",
                        articulo.getNombre(),
                        articulo.getTipo(),
                        articulo.getCantidad(),
                        articulo.getStockMinimo(),
                        articulo.getUbicacion()))
                .collect(Collectors.toList());
    }
}
