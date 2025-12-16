package gruasheredianas.inventario.model;

import java.util.Date;

/**
 * Representa un movimiento de inventario (entrada/salida)
 * @author franco
 */
public class MovimientoInventario {
    
    private int id;
    private int articuloId;
    private String tipoMovimiento; // ENTRADA, SALIDA
    private int cantidad;
    private String motivo;
    private Date fecha;
    private Integer usuarioId;
    private String referencia;
    
    public MovimientoInventario() {}

    public MovimientoInventario(int id, int articuloId, String tipoMovimiento, int cantidad, 
                                String motivo, Date fecha, Integer usuarioId, String referencia) {
        this.id = id;
        this.articuloId = articuloId;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
        this.referencia = referencia;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getArticuloId() { return articuloId; }
    public void setArticuloId(int articuloId) { this.articuloId = articuloId; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
