package sistemacaja.com;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "Proveedores")
@Entity

public class Proveedor{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	@OneToMany(mappedBy = "proveedor")
	@JsonIgnore
	private List<Movimientos> movimientos=new ArrayList<>();
	@ManyToOne
	@JoinColumn(name = "almacen_id")
	private Almacen almacen;

	
	public Proveedor(){}
	
	public Long getId(){
		return id;
	}

	public String getNombre(){
		return nombre;
	}

	public List<Movimientos> getMovimientos(){
		return movimientos;
	}

	
	public void setId(Long id){
		this.id=id;
	}

	public void setNombre(String nombre){
		this.nombre=nombre;
	}

	public void setMovimientos(List<Movimientos> movimientos){
		this.movimientos=movimientos;
	}

	public Almacen getAlmacen() {
		return almacen;
	}

	public void setAlmacen1(Almacen almacen) {
		this.almacen = almacen;
	}
	
	public Proveedor(Long id, String nombre, List<Movimientos> movimientos,Almacen almacen){
		this.id=id;
		this.nombre=nombre;
		this.movimientos=movimientos;
		this.almacen=almacen;	
	}

    public void setAlmacen(Almacen almacen) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAlmacen'");
    }
}
