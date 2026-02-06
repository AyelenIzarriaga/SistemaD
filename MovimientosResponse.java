package sistemacaja.com;


import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientosResponse {

    private Long id;
    private LocalDate fechaRegistro;
    private LocalDate fechaMovimiento;
    private BigDecimal monto;
    private String descripcion;
    private OrigenMov origen;
    private String proveedor;

    public MovimientosResponse(
            Long id,
            LocalDate fechaRegistro,
            LocalDate fechaMovimiento,
            BigDecimal monto,
            String descripcion,
            OrigenMov origen,
            Proveedor proveedor
    ) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.fechaMovimiento = fechaMovimiento;
        this.monto = monto;
        this.descripcion = descripcion;
        this.origen = origen;
        this.proveedor = proveedor != null ? proveedor.getNombre() : null;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDate getFechaMovimiento() {
        return fechaMovimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public OrigenMov getOrigen() {
        return origen;
    }

    public String getProveedor() {
        return proveedor;
    }
}
