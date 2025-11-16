package pe.com.acopio.util;

import pe.com.acopio.model.Usuario;

/**
 * Clase singleton para gestionar la sesión del usuario actual
 */
public class SessionManager {

    private static SessionManager instance;
    private Usuario usuarioActual;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean hayUsuarioActivo() {
        return usuarioActual != null;
    }

    public String getNombreUsuarioActual() {
        return (usuarioActual != null) ? usuarioActual.getNombreCompleto() : "Desconocido";
    }

    public Long getIdUsuarioActual() {
        return (usuarioActual != null) ? usuarioActual.getId() : null;
    }
}
