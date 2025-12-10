package gruasheredianas.finanzas.service;

import gruasheredianas.finanzas.dao.FinanzasDAO;
import gruasheredianas.finanzas.model.movimientoFinanciero;
import gruasheredianas.camiones.dao.CamionDAO;
import gruasheredianas.camiones.model.Camion;
import java.util.Date;
import java.util.List;

/**
 *
 * @author franco
 */
public class finanzasService {
    
    private FinanzasDAO dao = new FinanzasDAO();
    private CamionDAO camionDAO = new CamionDAO();

    /**
     * Calcula utilidad neta por camión en un periodo, restando ingresos - egresos y estimando consumo.
     * @param camionId id del camión
     * @param desde inicio periodo
     * @param hasta fin periodo
     * @param precioCombustible precio por litro (parámetro)
     * @return utilidad estimada
     */
    public double calcularUtilidadPorCamion(int camionId, Date desde, Date hasta, double precioCombustible) {
        // 1) obtener movimientos financieros netos
        double neto = dao.totalPorCamion(camionId, desde, hasta);

        // 2) estimar costo por combustible:
        Camion c = camionDAO.findById(camionId);
        double costoCombustible = 0.0;
        if (c != null) {
            // supuesto: consumoPromedio = litros por 100km, obtener km recorridos en periodo — SI no hay km por periodo, aproximamos 0
            // Necesitas registrar kilometraje histórico para cálculo real; aquí hacemos una aproximación con consumoPromedio * kmsPeriodo/100
            // Supongamos un método en CamionDAO para obtener kms recorridos: camionDAO.kmsRecorridos(camionId, desde, hasta)
            double kms = camionDAO.kmsRecorridos(camionId, desde, hasta); // implementa en CamionDAO
            double litros = (c.getConsumoPromedio() * kms) / 100.0;
            costoCombustible = litros * precioCombustible;
        }

        // 3) obtener costos de mantenimiento registrados en finanzas durante el periodo
        double mantenimiento = daoTotalPorTipo(camionId, "EGRESO", desde, hasta); // simplificado

        // utilidad neta = neto - costoCombustible - mantenimiento
        return neto - costoCombustible - mantenimiento;
    }

    // Método auxiliar para sumar montos por tipo (puedes complementar en DAO)
    private double daoTotalPorTipo(int camionId, String tipo, Date desde, Date hasta) {
        // Implementar consulta SUM en DAO si lo deseas; aquí lo hacemos con lista:
        List<movimientoFinanciero> lista = dao.listarPorCamion(camionId, desde, hasta);
        double sum = 0;
        for (movimientoFinanciero m : lista) {
            if (tipo.equals("INGRESO") && "INGRESO".equals(m.getTipo())) sum += m.getMonto();
            else if (tipo.equals("EGRESO") && "EGRESO".equals(m.getTipo())) sum += m.getMonto();
        }
        return sum;
    }
    
}
