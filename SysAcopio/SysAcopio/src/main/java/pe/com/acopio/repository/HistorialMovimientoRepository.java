package pe.com.acopio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.com.acopio.model.HistorialMovimiento;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialMovimientoRepository extends JpaRepository<HistorialMovimiento, Long> {

    List<HistorialMovimiento> findByUsuarioId(Long usuarioId);

    List<HistorialMovimiento> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT h FROM HistorialMovimiento h ORDER BY h.fechaHora DESC")
    List<HistorialMovimiento> findAllOrderByFechaDesc();
}
