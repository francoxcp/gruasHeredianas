
package gruasheredianas.finanzas.model;

import java.util.Date;

/**
 *
 * @author franc
 */
public class movimientoFinanciero {
    
    private int id;
    private Integer camionId; // nullable
    private String tipo; // INGRESO / EGRESO / POLIZA / SEGURO
    private String descripcion;
    private double monto;
    private Date fecha;
    private Integer creadoPor; // user id
    private String referencia;
    private byte[] datosSensibles; // si encriptas info con AES

    public movimientoFinanciero() {}

    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getCamionId() { return camionId; }
    public void setCamionId(Integer camionId) { this.camionId = camionId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Integer getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Integer creadoPor) { this.creadoPor = creadoPor; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public byte[] getDatosSensibles() { return datosSensibles; }
    public void setDatosSensibles(byte[] datosSensibles) { this.datosSensibles = datosSensibles; }
    
}
