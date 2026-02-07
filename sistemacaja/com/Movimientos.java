package sistemacaja.com;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;



@Table(name = "Movimientos")
@Entity

public class Movimientos{
	@Enumerated(EnumType.STRING)
	@Column(name = "movimiento")
	private movimientoTipo movimiento;
	@Column(nullable = false)
	private LocalDate fechaMovimiento;
	@Column(nullable = false, updatable = false)
	private LocalDate fechaRegistro;	
	private BigDecimal monto;
	private String descripcion;
	@Enumerated(EnumType.STRING)
	private OrigenMov origen; //origen movimiento local o casa
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proveedor_id")
	@JsonIgnore
	private Proveedor proveedor;
	@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "almacen_id")
	private Almacen almacen;


	public Movimientos(){}
	
	public Long getId() {
        return id;
    }
	public movimientoTipo getMovimiento(){
		return movimiento;
	}

	public LocalDate getFechaMovimiento(){
		return fechaMovimiento;
	}
	
	public LocalDate getFechaRegistro(){
		return fechaRegistro;
	}

	public BigDecimal getMonto(){
		return monto;
	}
	
	public String getDescripcion(){
		return descripcion;
	}

	public OrigenMov getOrigen(){
		return  origen;
	}
	
	public Proveedor getProveedor(){
		return proveedor;
	}

        public Almacen getAlmacen() {
    		return almacen;
	}

	public void setAlmacen(Almacen almacen) {
    		this.almacen = almacen;
	}

	public void setMovimiento(movimientoTipo movimiento){
    		this.movimiento = movimiento;
	}
	public void setFechaMovimiento(LocalDate fechaMovimiento){
    		this.fechaMovimiento = fechaMovimiento;
	}

	public void setMonto(BigDecimal monto){
    		this.monto = monto;
	}

	public void setDescripcion(String descripcion){
    		this.descripcion = descripcion;
	}
	public void setOrigen(OrigenMov origen){
		this.origen=origen;
	}
	
	public void setProveedor(Proveedor proveedor){
		this.proveedor=proveedor;
	}

	@PrePersist
	public void prePersist(){
    		if(fechaRegistro == null){
        		fechaRegistro = LocalDate.now();
    		}
	}


	public Movimientos(movimientoTipo movimiento, LocalDate fechaMovimiento, LocalDate fechaRegistro, BigDecimal monto, String descripcion, OrigenMov origen, Proveedor proveedor){
    		this.movimiento = movimiento;
    		this.fechaMovimiento = fechaMovimiento;
    		this.fechaRegistro = fechaRegistro;
		this.monto=monto;
		this.descripcion=descripcion;
		this.origen=origen;
		this.proveedor=proveedor;
    	}
}
