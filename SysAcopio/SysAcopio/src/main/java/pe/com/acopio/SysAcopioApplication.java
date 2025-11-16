package pe.com.acopio;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SysAcopioApplication {

    public static void main(String[] args) {
        // Lanzar JavaFX con Spring Boot
        javafx.application.Application.launch(MainApplication.class, args);
    }
}
