package pe.com.acopio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.acopio.model.HistorialMovimiento;
import pe.com.acopio.model.Usuario;
import pe.com.acopio.repository.HistorialMovimientoRepository;
import pe.com.acopio.util.SessionManager;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class HistorialService {

    private static final Logger logger = LoggerFactory.getLogger(HistorialService.class);

    @Autowired
    private HistorialMovimientoRepository historialRepository;

    /**
     * Registra una acción en el historial
     */
    public void logAccion(String accion, String descripcion, String modulo) {
        Usuario usuarioActual = SessionManager.getInstance().getUsuarioActual();

        if (usuarioActual == null) {
            logger.warn("No hay usuario activo para registrar la acción: {}", accion);
            return;
        }

        HistorialMovimiento movimiento = new HistorialMovimiento();
        movimiento.setUsuario(usuarioActual);
        movimiento.setAccion(accion);
        movimiento.setDescripcion(descripcion);
        movimiento.setModulo(modulo);
        movimiento.setFechaHora(LocalDateTime.now());

        historialRepository.save(movimiento);
        logger.info("Acción registrada: {} - {} por usuario {}", accion, descripcion, usuarioActual.getUsername());
    }

    /**
     * Registra una acción con usuario específico
     */
    public void logAccion(Usuario usuario, String accion, String descripcion, String modulo) {
        HistorialMovimiento movimiento = new HistorialMovimiento();
        movimiento.setUsuario(usuario);
        movimiento.setAccion(accion);
        movimiento.setDescripcion(descripcion);
        movimiento.setModulo(modulo);
        movimiento.setFechaHora(LocalDateTime.now());

        historialRepository.save(movimiento);
        logger.info("Acción registrada: {} - {} por usuario {}", accion, descripcion, usuario.getUsername());
    }

    /**
     * Obtener historial completo ordenado por fecha descendente
     */
    public List<HistorialMovimiento> obtenerTodos() {
        return historialRepository.findAllOrderByFechaDesc();
    }

    /**
     * Obtener historial de un usuario específico
     */
    public List<HistorialMovimiento> obtenerPorUsuario(Usuario usuario) {
        return historialRepository.findByUsuario(usuario);
    }

    /**
     * Obtener historial de un módulo específico
     */
    public List<HistorialMovimiento> obtenerPorModulo(String modulo) {
        return historialRepository.findByModulo(modulo);
    }

    /**
     * Obtener historial entre fechas
     */
    public List<HistorialMovimiento> obtenerPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return historialRepository.findByFechaHoraBetween(inicio, fin);
    }

    /**
     * Obtener historial reciente (últimas 24 horas)
     */
    public List<HistorialMovimiento> obtenerRecientes() {
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);
        return historialRepository.findRecientes(hace24Horas);
    }

    /**
     * Obtener historial de hoy
     */
    public List<HistorialMovimiento> obtenerHoy() {
        LocalDateTime inicioHoy = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finHoy = inicioHoy.plusDays(1);
        return historialRepository.findByFechaHoraBetween(inicioHoy, finHoy);
    }
}
