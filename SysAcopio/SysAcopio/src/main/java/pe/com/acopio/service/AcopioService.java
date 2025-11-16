package pe.com.acopio.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.acopio.model.*;
import pe.com.acopio.repository.AcopioDetalleRepository;
import pe.com.acopio.repository.AcopioRepository;
import pe.com.acopio.util.SessionManager;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class AcopioService {

    private static final Logger logger = LoggerFactory.getLogger(AcopioService.class);

    @Autowired
    private AcopioRepository acopioRepository;

    @Autowired
    private AcopioDetalleRepository acopioDetalleRepository;

    @Autowired
    private HistorialService historialService;

    @Value("${app.reports.path:reports/}")
    private String reportsPath;

    /**
     * Crear un nuevo acopio con sus detalles
     */
    public Acopio crear(Acopio acopio, List<AcopioDetalle> detalles) {
        logger.info("Creando nuevo acopio para proveedor: {}", acopio.getProveedor().getNombreCompleto());

        // Generar número de acopio automático
        String numeroAcopio = generarNumeroAcopio();
        acopio.setNumeroAcopio(numeroAcopio);

        // Agregar detalles y calcular cada uno
        int numeroItem = 1;
        for (AcopioDetalle detalle : detalles) {
            detalle.setNumeroItem(numeroItem++);
            detalle.calcular(); // Ejecuta la fórmula del Excel
            acopio.addDetalle(detalle);
        }

        // Calcular total del acopio
        acopio.calcularTotal();

        // Guardar
        Acopio acopioGuardado = acopioRepository.save(acopio);

        // Registrar en historial
        historialService.logAccion(
                "REGISTRO_ACOPIO",
                "Acopio " + numeroAcopio + " registrado por S/. " + acopio.getTotalPagar(),
                "ACOPIO"
        );

        logger.info("Acopio creado exitosamente: {}", numeroAcopio);
        return acopioGuardado;
    }

    /**
     * Genera número de acopio automático: ACO-YYYY-NNNN
     */
    private String generarNumeroAcopio() {
        LocalDate hoy = LocalDate.now();
        String año = String.valueOf(hoy.getYear());

        Long count = acopioRepository.countByFecha(hoy);
        String secuencia = String.format("%04d", count + 1);

        return "ACO-" + año + "-" + secuencia;
    }

    /**
     * Obtener acopio por ID con sus detalles
     */
    public Optional<Acopio> obtenerPorId(Long id) {
        return acopioRepository.findById(id);
    }

    /**
     * Obtener todos los acopios
     */
    public List<Acopio> obtenerTodos() {
        return acopioRepository.findAll();
    }

    /**
     * Obtener acopios por proveedor
     */
    public List<Acopio> obtenerPorProveedor(Proveedor proveedor) {
        return acopioRepository.findByProveedor(proveedor);
    }

    /**
     * Obtener acopios por rango de fechas
     */
    public List<Acopio> obtenerPorFechas(LocalDate inicio, LocalDate fin) {
        return acopioRepository.findByFechaAcopioBetween(inicio, fin);
    }

    /**
     * Obtener acopios de hoy
     */
    public List<Acopio> obtenerHoy() {
        return acopioRepository.findByFecha(LocalDate.now());
    }

    /**
     * Anular acopio
     */
    public void anular(Long id, String motivo) {
        logger.info("Anulando acopio ID: {}", id);

        Optional<Acopio> acopioOpt = acopioRepository.findById(id);
        if (acopioOpt.isPresent()) {
            Acopio acopio = acopioOpt.get();
            acopio.setEstado("ANULADO");
            acopio.setObservaciones("ANULADO: " + motivo);
            acopioRepository.save(acopio);

            historialService.logAccion(
                    "ANULACION_ACOPIO",
                    "Acopio " + acopio.getNumeroAcopio() + " anulado. Motivo: " + motivo,
                    "ACOPIO"
            );
        }
    }

    /**
     * Generar reporte (voucher) de acopio usando JasperReports
     */
    public JasperPrint generarVoucher(Long acopioId) {
        logger.info("Generando voucher para acopio ID: {}", acopioId);

        try {
            Optional<Acopio> acopioOpt = acopioRepository.findById(acopioId);
            if (!acopioOpt.isPresent()) {
                throw new RuntimeException("Acopio no encontrado");
            }

            Acopio acopio = acopioOpt.get();

            // Cargar el archivo JRXML
            InputStream reportStream = getClass().getResourceAsStream("/reports/comprobante_acopio.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("No se encontró el archivo de reporte");
            }

            // Compilar el reporte
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Preparar parámetros
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("numeroAcopio", acopio.getNumeroAcopio());
            parameters.put("fechaAcopio", acopio.getFechaAcopio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            parameters.put("proveedorNombre", acopio.getProveedor().getNombreCompleto());
            parameters.put("proveedorDocumento", acopio.getProveedor().getNumeroDocumento());
            parameters.put("proveedorDireccion", acopio.getProveedor().getDireccion());
            parameters.put("usuarioNombre", acopio.getUsuario().getNombreCompleto());
            parameters.put("totalPagar", acopio.getTotalPagar());
            parameters.put("observaciones", acopio.getObservaciones() != null ? acopio.getObservaciones() : "");

            // Preparar datasource con los detalles
            List<AcopioDetalle> detalles = acopioDetalleRepository.findByAcopioOrderByNumeroItemAsc(acopio);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalles);

            // Generar el reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            logger.info("Voucher generado exitosamente para acopio: {}", acopio.getNumeroAcopio());

            // Registrar en historial
            historialService.logAccion(
                    "IMPRESION_VOUCHER",
                    "Voucher generado para acopio " + acopio.getNumeroAcopio(),
                    "ACOPIO"
            );

            return jasperPrint;

        } catch (Exception e) {
            logger.error("Error al generar voucher para acopio ID: " + acopioId, e);
            throw new RuntimeException("Error al generar voucher: " + e.getMessage());
        }
    }

    /**
     * Calcular un detalle usando la fórmula del Excel
     * Este método es público para que pueda ser usado por los controladores
     * para pre-visualizar cálculos antes de guardar
     */
    public AcopioDetalle calcularDetalle(BigDecimal peso, BigDecimal ley, BigDecimal deduccion,
                                         BigDecimal precioOnza, BigDecimal tipoCambio) {
        AcopioDetalle detalle = new AcopioDetalle();
        detalle.setPeso(peso);
        detalle.setLey(ley);
        detalle.setDeduccion(deduccion);
        detalle.setPrecioOnzaBase(precioOnza);
        detalle.setTipoCambioDolar(tipoCambio);

        detalle.calcular();

        return detalle;
    }
}
