package gruasheredianas.polizas.service;

import gruasheredianas.polizas.dao.PolizaDAO;
import gruasheredianas.polizas.model.Poliza;
import gruasheredianas.polizas.model.RenovacionPoliza;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Servicio para gestión de pólizas y seguros con alertas automáticas
 * @author franco
 */
public class PolizaService {
    
    private PolizaDAO polizaDAO = new PolizaDAO();

    /**
     * Registra una nueva póliza de seguro
     * @param poliza Póliza a registrar
     * @return true si se registró correctamente
     */
    public boolean registrarPoliza(Poliza poliza) {
        // Verificar que la fecha de vencimiento sea posterior a la fecha de inicio
        if (poliza.getFechaVencimiento().before(poliza.getFechaInicio())) {
            System.err.println("Error: La fecha de vencimiento debe ser posterior a la fecha de inicio");
            return false;
        }
        
        return polizaDAO.registrar(poliza);
    }

    /**
     * Actualiza una póliza existente
     * @param poliza Póliza con datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarPoliza(Poliza poliza) {
        return polizaDAO.actualizar(poliza);
    }

    /**
     * Obtiene todas las pólizas
     * @return Lista de pólizas
     */
    public List<Poliza> obtenerTodasPolizas() {
        return polizaDAO.listar();
    }

    /**
     * Obtiene pólizas de una grúa específica
     * @param camionId ID de la grúa
     * @return Lista de pólizas de la grúa
     */
    public List<Poliza> obtenerPolizasPorCamion(int camionId) {
        return polizaDAO.listarPorCamion(camionId);
    }

    /**
     * Obtiene pólizas vigentes
     * @return Lista de pólizas vigentes
     */
    public List<Poliza> obtenerPolizasVigentes() {
        return polizaDAO.listarVigentes();
    }

    /**
     * Obtiene alertas de pólizas próximas a vencer (30 días)
     * @return Lista de pólizas que vencen pronto
     */
    public List<Poliza> obtenerAlertasVencimiento() {
        return polizaDAO.listarProximasAVencer();
    }

    /**
     * Renueva una póliza existente
     * @param polizaId ID de la póliza a renovar
     * @param nuevaFechaVencimiento Nueva fecha de vencimiento
     * @param nuevaPrima Nueva prima
     * @param observaciones Observaciones de la renovación
     * @param usuarioId ID del usuario que realiza la renovación
     * @return true si se renovó correctamente
     */
    public boolean renovarPoliza(int polizaId, Date nuevaFechaVencimiento, 
                                double nuevaPrima, String observaciones, Integer usuarioId) {
        Poliza poliza = polizaDAO.buscarPorId(polizaId);
        if (poliza == null) {
            System.err.println("Póliza no encontrada");
            return false;
        }

        // Registrar renovación
        RenovacionPoliza renovacion = new RenovacionPoliza();
        renovacion.setPolizaId(polizaId);
        renovacion.setFechaRenovacion(new Date());
        renovacion.setNuevaFechaVencimiento(nuevaFechaVencimiento);
        renovacion.setNuevaPrima(nuevaPrima);
        renovacion.setObservaciones(observaciones);
        renovacion.setUsuarioId(usuarioId);

        if (!polizaDAO.registrarRenovacion(renovacion)) {
            System.err.println("Error al registrar renovación");
            return false;
        }

        // Actualizar póliza
        poliza.setFechaVencimiento(nuevaFechaVencimiento);
        poliza.setPrima(nuevaPrima);
        poliza.setEstado("VIGENTE");
        
        return polizaDAO.actualizar(poliza);
    }

    /**
     * Verifica y actualiza el estado de las pólizas vencidas
     * @return Número de pólizas actualizadas
     */
    public int actualizarEstadosVencidos() {
        List<Poliza> vigentes = polizaDAO.listarVigentes();
        int actualizadas = 0;
        
        for (Poliza poliza : vigentes) {
            if (poliza.estaVencida()) {
                poliza.setEstado("VENCIDA");
                if (polizaDAO.actualizar(poliza)) {
                    actualizadas++;
                }
            }
        }
        
        return actualizadas;
    }

    /**
     * Genera reporte de cumplimiento de seguros
     * @return Lista con información de cobertura
     */
    public List<String> generarReporteCumplimiento() {
        List<String> reporte = new ArrayList<>();
        List<Poliza> todasPolizas = polizaDAO.listar();
        
        int vigentes = 0;
        int vencidas = 0;
        int proximasVencer = 0;
        
        for (Poliza poliza : todasPolizas) {
            if ("VIGENTE".equals(poliza.getEstado())) {
                vigentes++;
                if (poliza.proximaAVencer()) {
                    proximasVencer++;
                }
            } else if ("VENCIDA".equals(poliza.getEstado())) {
                vencidas++;
            }
        }
        
        reporte.add("Total de pólizas: " + todasPolizas.size());
        reporte.add("Pólizas vigentes: " + vigentes);
        reporte.add("Pólizas vencidas: " + vencidas);
        reporte.add("Pólizas próximas a vencer (30 días): " + proximasVencer);
        
        return reporte;
    }

    /**
     * Cancela una póliza
     * @param polizaId ID de la póliza
     * @return true si se canceló correctamente
     */
    public boolean cancelarPoliza(int polizaId) {
        Poliza poliza = polizaDAO.buscarPorId(polizaId);
        if (poliza == null) {
            System.err.println("Póliza no encontrada");
            return false;
        }
        
        poliza.setEstado("CANCELADA");
        return polizaDAO.actualizar(poliza);
    }

    /**
     * Obtiene el historial de renovaciones de una póliza
     * @param polizaId ID de la póliza
     * @return Lista de renovaciones
     */
    public List<RenovacionPoliza> obtenerHistorialRenovaciones(int polizaId) {
        return polizaDAO.listarRenovaciones(polizaId);
    }

    /**
     * Verifica si una grúa tiene cobertura vigente
     * @param camionId ID de la grúa
     * @return true si tiene al menos una póliza vigente
     */
    public boolean tieneCoberturaVigente(int camionId) {
        List<Poliza> polizas = polizaDAO.listarPorCamion(camionId);
        for (Poliza poliza : polizas) {
            if ("VIGENTE".equals(poliza.getEstado()) && !poliza.estaVencida()) {
                return true;
            }
        }
        return false;
    }
}
