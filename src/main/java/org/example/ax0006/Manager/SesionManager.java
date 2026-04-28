package org.example.ax0006.Manager;

import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Entity.Usuario;

public class SesionManager {

    private Usuario usuarioActual;
    private Concierto conciertoTemporal;
    private Integer idContratoTemporal;
    private String pantallaOrigen;

    public Usuario getUsuarioActual() { return usuarioActual; }
    public Usuario getUsuarioLogueado() { return usuarioActual; }
    public void setUsuarioActual(Usuario usuarioActual) { this.usuarioActual = usuarioActual; }

    public void cerrarSesion() {
        this.usuarioActual = null;
        this.conciertoTemporal = null;
        this.idContratoTemporal = null;
        this.pantallaOrigen = null;
    }

    public void setConciertoTemporal(Concierto conciertoTemporal) { this.conciertoTemporal = conciertoTemporal; }
    public Concierto getConciertoTemporal() { return conciertoTemporal; }

    public void setIdContratoTemporal(Integer idContratoTemporal) { this.idContratoTemporal = idContratoTemporal; }
    public Integer getIdContratoTemporal() { return idContratoTemporal; }

    public void setPantallaOrigen(String pantallaOrigen) { this.pantallaOrigen = pantallaOrigen; }
    public String getPantallaOrigen() { return pantallaOrigen; }
}