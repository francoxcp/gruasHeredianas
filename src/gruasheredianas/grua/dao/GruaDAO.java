package gruasheredianas.grua.dao;

import gruasheredianas.grua.model.Grua;
import java.util.ArrayList;

/**
 *
 * @author franc
 */
public class GruaDAO {
    private ArrayList<Grua> gruas = new ArrayList<>();

    /**
     * Método para registrar una grúa
     * @param grua Objeto Grua que será registrado
     */
    public void registrarGrua(Grua grua) {
        gruas.add(grua);
        System.out.println("Grúa registrada exitosamente.");
    }

    /**
     * Método para actualizar la ubicación de una grúa
     * @param id Identificador de la grúa
     * @param nuevaUbicacion Nueva ubicación GPS
     */
    public void actualizarUbicacion(int id, String nuevaUbicacion) {
        for (Grua grua : gruas) {
            if (grua.getId() == id) {
                grua.setUbicacionActual(nuevaUbicacion);
                System.out.println("Ubicación actualizada para la grúa: " + grua.getNombre());
                return;
            }
        }
        System.out.println("Grúa no encontrada.");
    }

    /**
     * Método para actualizar el historial de rutas de una grúa
     * @param id Identificador de la grúa
     * @param nuevaRuta Ruta que será añadida al historial
     */
    public void agregarRuta(int id, String nuevaRuta) {
        for (Grua grua : gruas) {
            if (grua.getId() == id) {
                grua.actualizarHistorialRuta(nuevaRuta);
                System.out.println("Ruta añadida al historial de la grúa: " + grua.getNombre());
                return;
            }
        }
        System.out.println("Grúa no encontrada.");
    }
    
    /**
     * Método para obtener la lista completa de grúas
     * @return Lista de objetos Grua
     */
    public ArrayList<Grua> obtenerGruas() {
        return gruas;
    }
    
}
