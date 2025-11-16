package pe.com.acopio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.com.acopio.model.Usuario;
import pe.com.acopio.model.Material;
import pe.com.acopio.repository.UsuarioRepository;
import pe.com.acopio.repository.MaterialRepository;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Iniciando carga de datos por defecto ===");

        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setRol("ADMIN");
            admin.setActivo(true);
            admin.setFechaCreacion(LocalDateTime.now());
            usuarioRepository.save(admin);
            System.out.println("✓ Usuario admin creado (user: admin, pass: admin123)");
        }

        if (!usuarioRepository.existsByUsername("operador")) {
            Usuario operador = new Usuario();
            operador.setUsername("operador");
            operador.setPassword("oper123");
            operador.setNombreCompleto("Juan Perez Lopez");
            operador.setRol("OPERADOR");
            operador.setActivo(true);
            operador.setFechaCreacion(LocalDateTime.now());
            usuarioRepository.save(operador);
            System.out.println("✓ Usuario operador creado (user: operador, pass: oper123)");
        }

        if (materialRepository.count() == 0) {
            Material oro = new Material();
            oro.setNombre("Oro");
            oro.setDescripcion("Oro de acopio");
            oro.setActivo(true);
            materialRepository.save(oro);

            Material plata = new Material();
            plata.setNombre("Plata");
            plata.setDescripcion("Plata de acopio");
            plata.setActivo(true);
            materialRepository.save(plata);

            Material cobre = new Material();
            cobre.setNombre("Cobre");
            cobre.setDescripcion("Cobre de acopio");
            cobre.setActivo(true);
            materialRepository.save(cobre);

            System.out.println("✓ Materiales creados (Oro, Plata, Cobre)");
        }

        System.out.println("=== Carga de datos completada ===");
    }
}
