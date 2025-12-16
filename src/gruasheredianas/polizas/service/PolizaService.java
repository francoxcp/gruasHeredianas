package gruasheredianas.polizas.service;

import gruasheredianas.polizas.dao.PolizaSeguroDAO;
import gruasheredianas.polizas.model.PolizaSeguro;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de pólizas de seguro.
 * Proporciona lógica de negocio, alertas y reportes de seguros.
 * 
 * @author franco
 */
public class PolizaService {
    
    private final PolizaSeguroDAO polizaDAO;
    
    public PolizaService() {
        this.polizaDAO = new PolizaSeguroDAO();
    }
    
    /**
     * Registra una nueva póliza de seguro.
     * 
     * @param poliza Póliza a registrar
     * @return true si el registro fue exitoso
     */
    public boolean registrarPoliza(PolizaSeguro poliza) {
        return polizaDAO.registrar(poliza);
    }
    
    /**
     * Renueva una póliza existente.
     * 
     * @param id ID de la póliza
     * @param nuevaFechaVencimiento Nueva fecha de vencimiento
     * @return true si la renovación fue exitosa
     */
    public boolean renovarPoliza(int id, Date nuevaFechaVencimiento) {
        return polizaDAO.renovarPoliza(id, nuevaFechaVencimiento);
    }
    
    /**
     * Cancela una póliza.
     * 
     * @param id ID de la póliza
     * @return true si la cancelación fue exitosa
     */
    public boolean cancelarPoliza(int id) {
        return polizaDAO.actualizarEstado(id, "CANCELADA");
    }
    
    /**
     * Obtiene todas las pólizas.
     * 
     * @return Lista de pólizas
     */
    public List<PolizaSeguro> obtenerTodasLasPolizas() {
        return polizaDAO.listar();
    }
    
    /**
     * Obtiene una póliza por ID.
     * 
     * @param id ID de la póliza
     * @return Póliza o null si no existe
     */
    public PolizaSeguro obtenerPolizaPorId(int id) {
        return polizaDAO.obtenerPorId(id);
    }
    
    /**
     * Obtiene pólizas de una grúa específica.
     * 
     * @param camionId ID del camión
     * @return Lista de pólizas
     */
    public List<PolizaSeguro> obtenerPolizasPorCamion(int camionId) {
        return polizaDAO.obtenerPorCamion(camionId);
    }
    
    /**
     * Obtiene la póliza activa de una grúa.
     * 
     * @param camionId ID del camión
     * @return Póliza activa o null si no existe
     */
    public PolizaSeguro obtenerPolizaActiva(int camionId) {
        return polizaDAO.obtenerPolizaActiva(camionId);
    }
    
    /**
     * Verifica si una grúa tiene cobertura vigente.
     * 
     * @param camionId ID del camión
     * @return true si tiene póliza activa no vencida
     */
    public boolean tieneCobertura(int camionId) {
        PolizaSeguro poliza = polizaDAO.obtenerPolizaActiva(camionId);
        return poliza != null && !poliza.estaVencida();
    }
    
    /**
     * Obtiene pólizas próximas a vencer (dentro de 30 días).
     * 
     * @return Lista de pólizas por vencer
     */
    public List<PolizaSeguro> obtenerPolizasPorVencer() {
        return polizaDAO.obtenerPolizasPorVencer(30);
    }
    
    /**
     * Obtiene pólizas vencidas.
     * 
     * @return Lista de pólizas vencidas
     */
    public List<PolizaSeguro> obtenerPolizasVencidas() {
        return polizaDAO.obtenerPolizasVencidas();
    }
    
    /**
     * Genera alertas de vencimiento próximo.
     * 
     * @return Lista de mensajes de alerta
     */
    public List<String> generarAlertasVencimiento() {
        List<PolizaSeguro> polizasPorVencer = polizaDAO.obtenerPolizasPorVencer(30);
        
        return polizasPorVencer.stream()
                .map(poliza -> String.format(
                    "¡ALERTA! Póliza %s de la grúa ID %d vence en %d días (%s) - Aseguradora: %s",
                    poliza.getNumeroPoliza(),
                    poliza.getCamionId(),
                    poliza.getDiasHastaVencimiento(),
                    poliza.getFechaVencimiento(),
                    poliza.getAseguradora()
                ))
                .collect(Collectors.toList());
    }
    
    /**
     * Actualiza automáticamente el estado de pólizas vencidas.
     * 
     * @return Número de pólizas actualizadas
     */
    public int actualizarEstadoPolizasVencidas() {
        List<PolizaSeguro> vencidas = polizaDAO.obtenerPolizasVencidas();
        int actualizadas = 0;
        
        for (PolizaSeguro poliza : vencidas) {
            if (polizaDAO.actualizarEstado(poliza.getId(), "VENCIDA")) {
                actualizadas++;
                System.out.println("Póliza " + poliza.getNumeroPoliza() + " marcada como VENCIDA");
            }
        }
        
        return actualizadas;
    }
    
    /**
     * Genera un reporte de cumplimiento de seguros.
     * 
     * @return String con el reporte
     */
    public String generarReporteCumplimiento() {
        List<PolizaSeguro> todasPolizas = polizaDAO.listar();
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("=== REPORTE DE CUMPLIMIENTO DE SEGUROS ===\n\n");
        
        long activas = todasPolizas.stream()
                .filter(p -> "ACTIVA".equals(p.getEstado()))
                .count();
        long vencidas = todasPolizas.stream()
                .filter(p -> "VENCIDA".equals(p.getEstado()))
                .count();
        long canceladas = todasPolizas.stream()
                .filter(p -> "CANCELADA".equals(p.getEstado()))
                .count();
        
        reporte.append("Total de pólizas: ").append(todasPolizas.size()).append("\n");
        reporte.append("Pólizas activas: ").append(activas).append("\n");
        reporte.append("Pólizas vencidas: ").append(vencidas).append("\n");
        reporte.append("Pólizas canceladas: ").append(canceladas).append("\n\n");
        
        List<PolizaSeguro> porVencer = polizaDAO.obtenerPolizasPorVencer(30);
        reporte.append("Pólizas próximas a vencer (30 días): ").append(porVencer.size()).append("\n");
        
        if (!porVencer.isEmpty()) {
            reporte.append("\n--- Detalle de vencimientos próximos ---\n");
            for (PolizaSeguro poliza : porVencer) {
                reporte.append("- Grúa ID: ").append(poliza.getCamionId())
                       .append(" | Póliza: ").append(poliza.getNumeroPoliza())
                       .append(" | Vence: ").append(poliza.getFechaVencimiento())
                       .append(" (").append(poliza.getDiasHastaVencimiento()).append(" días)")
                       .append(" | Aseguradora: ").append(poliza.getAseguradora()).append("\n");
            }
        }
        
        return reporte.toString();
    }
    
    /**
     * Genera un reporte de cobertura por aseguradora.
     * 
     * @return String con el reporte
     */
    public String generarReporteCobertura() {
        List<PolizaSeguro> polizas = polizaDAO.listar();
        StringBuilder reporte = new StringBuilder();
        
        reporte.append("=== REPORTE DE COBERTURA ===\n\n");
        
        // Agrupar por aseguradora
        var porAseguradora = polizas.stream()
                .collect(Collectors.groupingBy(PolizaSeguro::getAseguradora));
        
        for (var entry : porAseguradora.entrySet()) {
            String aseguradora = entry.getKey();
            List<PolizaSeguro> polizasAseguradora = entry.getValue();
            
            reporte.append("Aseguradora: ").append(aseguradora).append("\n");
            reporte.append("Total de pólizas: ").append(polizasAseguradora.size()).append("\n");
            
            double coberturaTotal = polizasAseguradora.stream()
                    .filter(p -> "ACTIVA".equals(p.getEstado()))
                    .mapToDouble(PolizaSeguro::getMontoCobertura)
                    .sum();
            
            double primaTotal = polizasAseguradora.stream()
                    .filter(p -> "ACTIVA".equals(p.getEstado()))
                    .mapToDouble(PolizaSeguro::getPrimaMensual)
                    .sum();
            
            reporte.append("Cobertura total: $").append(String.format("%.2f", coberturaTotal)).append("\n");
            reporte.append("Prima mensual total: $").append(String.format("%.2f", primaTotal)).append("\n");
            reporte.append("Prima anual total: $").append(String.format("%.2f", primaTotal * 12)).append("\n\n");
        }
        
        // Resumen total
        double coberturaGlobal = polizas.stream()
                .filter(p -> "ACTIVA".equals(p.getEstado()))
                .mapToDouble(PolizaSeguro::getMontoCobertura)
                .sum();
        
        double primaGlobal = polizas.stream()
                .filter(p -> "ACTIVA".equals(p.getEstado()))
                .mapToDouble(PolizaSeguro::getPrimaMensual)
                .sum();
        
        reporte.append("--- RESUMEN GLOBAL ---\n");
        reporte.append("Cobertura total: $").append(String.format("%.2f", coberturaGlobal)).append("\n");
        reporte.append("Prima mensual total: $").append(String.format("%.2f", primaGlobal)).append("\n");
        reporte.append("Prima anual total: $").append(String.format("%.2f", primaGlobal * 12)).append("\n");
        
        return reporte.toString();
    }
    
    /**
     * Verifica y muestra alertas para una grúa específica.
     * 
     * @param camionId ID del camión
     */
    public void verificarAlertasGrua(int camionId) {
        PolizaSeguro poliza = polizaDAO.obtenerPolizaActiva(camionId);
        
        if (poliza == null) {
            System.out.println("¡ALERTA CRÍTICA! La grúa ID " + camionId + " no tiene póliza activa.");
            return;
        }
        
        if (poliza.estaVencida()) {
            System.out.println("¡ALERTA CRÍTICA! La póliza " + poliza.getNumeroPoliza() + 
                             " de la grúa ID " + camionId + " está VENCIDA.");
        } else if (poliza.estaPorVencer()) {
            System.out.println("¡ALERTA! La póliza " + poliza.getNumeroPoliza() + 
                             " de la grúa ID " + camionId + " vence en " + 
                             poliza.getDiasHastaVencimiento() + " días.");
        }
    }
}
