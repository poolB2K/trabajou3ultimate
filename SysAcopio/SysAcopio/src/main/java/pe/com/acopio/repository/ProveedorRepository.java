package pe.com.acopio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.acopio.model.Proveedor;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByDni(String dni);

    List<Proveedor> findByActivoTrue();

    List<Proveedor> findByNombresContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCase(
            String nombres, String apellido);
}
