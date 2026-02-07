package sistemacaja.com;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MovimientosService {

    private final MovimientosRepository movimientosRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;

    private final List<Criterio> criterios = List.of(
            new CriterioFechaMovimientoValida(),
            new CriterioMontoValido(),
            new CriterioTipoMovimientoValido(),
            new CriterioProveedorValido()
    );

    public MovimientosService(MovimientosRepository movimientosRepository,
                              ProveedorRepository proveedorRepository,
                              UsuarioRepository usuarioRepository) {
        this.movimientosRepository = movimientosRepository;
        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // =======================
    // ===== UTIL ===========
    // =======================

    private Usuario obtenerUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private Long obtenerIdAlmacen(Long idUsuario) {
        Usuario usuario = obtenerUsuario(idUsuario);

        if (usuario.getAlmacen() == null) {
            throw new IllegalStateException("El usuario no tiene almacén asignado");
        }

        return usuario.getAlmacen().getIdAlmacen();
    }

    // =======================
    // ===== CREAR ==========
    // =======================

    public MovimientosResponse crearMovimiento(CrearMovimientoRequest request, Long idUsuario) {

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

        Movimientos mov = new Movimientos();
        mov.setMovimiento(request.getMovimiento());
        mov.setFechaMovimiento(request.getFechaMovimiento());
        mov.setMonto(request.getMonto());
        mov.setDescripcion(request.getDescripcion());
        mov.setOrigen(request.getOrigen());
        mov.setProveedor(proveedor);
        mov.setAlmacen(obtenerUsuario(idUsuario).getAlmacen());

        validar(mov);

        Movimientos guardado = movimientosRepository.save(mov);

        return new MovimientosResponse(
                guardado.getId(),
                guardado.getFechaRegistro(),
                guardado.getFechaMovimiento(),
                guardado.getMonto(),
                guardado.getDescripcion(),
                guardado.getOrigen(),
                guardado.getProveedor()
        );
    }

    private void validar(Movimientos m) {
        for (Criterio c : criterios) {
            if (!c.cumple(m)) {
                throw new IllegalStateException(
                        "Movimiento inválido: " + c.getClass().getSimpleName()
                );
            }
        }
    }

    // =======================
    // ===== LISTAR =========
    // =======================

    public List<MovimientosResponse> listar(Long idUsuario) {

        return movimientosRepository.findByAlmacenId(obtenerIdAlmacen(idUsuario))
                .stream()
                .map(m -> new MovimientosResponse(
                        m.getId(),
                        m.getFechaRegistro(),
                        m.getFechaMovimiento(),
                        m.getMonto(),
                        m.getDescripcion(),
                        m.getOrigen(),
                        m.getProveedor()
                ))
                .toList();
    }

    // =======================
    // ===== CAJA ===========
    // =======================

    private BigDecimal promedio(BigDecimal total, long dias) {
        if (dias <= 0) return BigDecimal.ZERO;

        return total.divide(
                BigDecimal.valueOf(dias),
                2,
                RoundingMode.HALF_UP
        );
    }

    public ResumenCajaResponse resumenDia(LocalDate fecha, Long idUsuario) {

        Long idAlmacen = obtenerIdAlmacen(idUsuario);

        List<Movimientos> lista =
                movimientosRepository.findByFechaMovimiento(fecha, idAlmacen);

        BigDecimal entradas = lista.stream()
                .filter(m -> movimientoTipo.ENTRADA.equals(m.getMovimiento()))
                .map(Movimientos::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal salidas = lista.stream()
                .filter(m -> movimientoTipo.SALIDA.equals(m.getMovimiento()))
                .map(Movimientos::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal libre = entradas.subtract(salidas);

        return new ResumenCajaResponse(
                fecha,
                entradas,
                salidas,
                libre,
                libre,
                entradas
        );
    }

    public ResumenCajaResponse resumenSemana(LocalDate fecha, Long idUsuario) {

        Long idAlmacen = obtenerIdAlmacen(idUsuario);

        LocalDate desde = fecha.with(DayOfWeek.MONDAY);
        LocalDate hasta = fecha.with(DayOfWeek.SUNDAY);

        long diasPeriodo = ChronoUnit.DAYS.between(desde, hasta.plusDays(1));

        List<Movimientos> lista =
                movimientosRepository.findByRango(desde, hasta, idAlmacen);

        BigDecimal entradas = lista.stream()
                .filter(m -> movimientoTipo.ENTRADA.equals(m.getMovimiento()))
                .map(Movimientos::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal salidas = lista.stream()
                .filter(m -> movimientoTipo.SALIDA.equals(m.getMovimiento()))
                .map(Movimientos::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal libre = entradas.subtract(salidas);

        BigDecimal promedioLibre = promedio(libre, diasPeriodo);
        BigDecimal promedioRecaudacion = promedio(entradas, diasPeriodo);

        return new ResumenCajaResponse(
                desde,
                entradas,
                salidas,
                libre,
                promedioLibre,
                promedioRecaudacion
        );
    }

    public ResumenCajaResponse resumenMes(LocalDate fecha, Long idUsuario) {

        Long idAlmacen = obtenerIdAlmacen(idUsuario);

        LocalDate desde = fecha.withDayOfMonth(1);
        LocalDate hasta = fecha.withDayOfMonth(fecha.lengthOfMonth());

        long diasPeriodo = ChronoUnit.DAYS.between(desde, hasta.plusDays(1));

        List<Movimientos> lista =
                movimientosRepository.findByRango(desde, hasta, idAlmacen);

        BigDecimal entradas = lista.stream()
                .filter(m -> movimientoTipo.ENTRADA.equals(m.getMovimiento()))
                .map(Movimientos::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal salidas = lista.stream()
                .filter(m -> movimientoTipo.SALIDA.equals(m.getMovimiento()))
                .map(Movimientos::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal libre = entradas.subtract(salidas);

        BigDecimal promedioLibre = promedio(libre, diasPeriodo);
        BigDecimal promedioRecaudacion = promedio(entradas, diasPeriodo);

        return new ResumenCajaResponse(
                desde,
                entradas,
                salidas,
                libre,
                promedioLibre,
                promedioRecaudacion
        );
    }

    // =======================
    // ===== BOT ============
    // =======================

    public Movimientos crearDesdeBot(
            movimientoTipo tipo,
            BigDecimal monto,
            String proveedorNombre,
            String descripcion,
            OrigenMov origen,
            Long idUsuario,
            LocalDate fecha
    ) {

        Long idAlmacen = obtenerIdAlmacen(idUsuario);

        Proveedor proveedor = proveedorRepository
                .findByNombreAndAlmacenId(proveedorNombre, idAlmacen)
                .orElseGet(() -> {
                    Proveedor p = new Proveedor();
                    p.setNombre(proveedorNombre);
                    p.setAlmacen1(obtenerUsuario(idUsuario).getAlmacen());
                    return proveedorRepository.save(p);
                });

        Movimientos mov = new Movimientos();
        mov.setMovimiento(tipo);
        mov.setMonto(monto);
        mov.setDescripcion(descripcion);
        mov.setFechaMovimiento(fecha);
        mov.setOrigen(origen);
        mov.setProveedor(proveedor);
        mov.setAlmacen(obtenerUsuario(idUsuario).getAlmacen());

        validar(mov);

        return movimientosRepository.save(mov);
    }

    public Movimientos deshacerUltimo(Long idUsuario) {

        Movimientos m = movimientosRepository
                .findTopByAlmacenIdOrderByFechaRegistroDesc(
                        usuarioRepository.findById(idUsuario).get().getAlmacen().getIdAlmacen()
                )
                .orElseThrow(() -> new RuntimeException("No hay movimientos para borrar"));

        movimientosRepository.delete(m);

        return m;
    }
}
