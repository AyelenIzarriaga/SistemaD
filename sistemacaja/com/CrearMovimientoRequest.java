package sistemacaja.com;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CrearMovimientoRequest {

    private movimientoTipo movimiento;
    private LocalDate fechaMovimiento;
    private BigDecimal monto;
    private String descripcion;
    private OrigenMov origen;
    private Long idProveedor;
    private Long idUsuario;


    public movimientoTipo getMovimiento() {
        return movimiento;
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

    public Long getIdProveedor() {
        return idProveedor;
    }
	
    public Long getIdUsuario(){
	return idUsuario;
    }
}
