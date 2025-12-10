package gruasheredianas.camiones.model;

/**
 *
 * @author franco
 */
public class Camion {
    
    private int id;
    private String placa;
    private String modelo;
    private int anio;
    private double kilometraje;
    private String estado;
    private double consumoCombustible;
    
    public Camion() {}

    public Camion(int id, String placa, String modelo, int anio, double kilometraje, String estado, double consumoCombustible) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometraje = kilometraje;
        this.estado = estado;
        this.consumoCombustible = consumoCombustible;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public double getKilometraje() { return kilometraje; }
    public void setKilometraje(double kilometraje) { this.kilometraje = kilometraje; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getConsumoCombustible() { return consumoCombustible; }
    public void setConsumoCombustible(double consumoCombustible) { this.consumoCombustible = consumoCombustible; }

    public double getConsumoPromedio() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
