package pe.com.acopio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.com.acopio.model.Acopio;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AcopioRepository extends JpaRepository<Acopio, Long> {

    List<Acopio> findByProveedorId(Long proveedorId);

    List<Acopio> findByFechaAcopioBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT a FROM Acopio a ORDER BY a.fechaAcopio DESC")
    List<Acopio> findAllOrderByFechaDesc();
}
