package gruasheredianas.polizas.dao;

import gruasheredianas.polizas.model.Poliza;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author franc
 */
public class PolizaDAO {
    
    private ArrayList<Poliza> polizas = new ArrayList<>();

    /**
     * Método para registrar una póliza
     * @param poliza Objeto Poliza que será registrado
     */
    public void registrarPoliza(Poliza poliza) {
        polizas.add(poliza);
        System.out.println("Póliza registrada exitosamente.");
    }

    /**
     * Método para verificar pólizas próximas a vencerse
     * @return Lista de pólizas próximas a vencer
     */
    public ArrayList<Poliza> obtenerPolizasProximasAVencer() {
        ArrayList<Poliza> proximasAVencer = new ArrayList<>();
        Date hoy = new Date();

        for (Poliza poliza : polizas) {
            long diasRestantes = (poliza.getFechaVencimiento().getTime() - hoy.getTime()) / (1000 * 60 * 60 * 24);
            if (diasRestantes > 0 && diasRestantes <= 30) {
                proximasAVencer.add(poliza);
            }
        }
        return proximasAVencer;
    }

    /**
     * Método para obtener todas las pólizas registradas
     * @return Lista de todas las pólizas
     */
    public ArrayList<Poliza> obtenerTodasPolizas() {
        return polizas;
    }
}