package gruasheredianas.camiones.model;


public class EstadoServicio {
   private int idSolicitud;
    private String camion;
    private String operador;
    private String estado;
    private String horaSalida;
    private String horaLlegada;
    private String horaFinalizacion;

    public EstadoServicio() {}

    public EstadoServicio(int idSolicitud, String camion, String operador, String estado,
                          String horaSalida, String horaLlegada, String horaFinalizacion) {
        this.idSolicitud = idSolicitud;
        this.camion = camion;
        this.operador = operador;
        this.estado = estado;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.horaFinalizacion = horaFinalizacion;
    }

    // Getters y setters
    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public String getCamion() { return camion; }
    public void setCamion(String camion) { this.camion = camion; }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getHoraSalida() { return horaSalida; }
    public void setHoraSalida(String horaSalida) { this.horaSalida = horaSalida; }

    public String getHoraLlegada() { return horaLlegada; }
    public void setHoraLlegada(String horaLlegada) { this.horaLlegada = horaLlegada; }

    public String getHoraFinalizacion() { return horaFinalizacion; }
    public void setHoraFinalizacion(String horaFinalizacion) { this.horaFinalizacion = horaFinalizacion; }
}