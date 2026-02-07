package sistemacaja.com;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity 

public class Usuario{
	@Id
	@GeneratedValue
	private Long id;
	private String nombre;
	@ManyToOne
        @JoinColumn(name = "almacen_id")
        private Almacen almacen;
		@Column(unique = true)
		private Long telegramId;

	public Usuario(){}

	public Long getIdUsuario(){
		return id;
	}

	public String getNombreUsuario(){
		return nombre;
	}

	public Almacen getAlmacen(){
		return almacen;
	}
	
	public void setIdUsuario(Long id){
		this.id=id;
	}

	public void setNombreUsuario(String nombre){
		this.nombre=nombre;
	}

	public void setAlmacen(Almacen almacen){
		this.almacen=almacen;
	}

	//Contructor
	
	public Usuario(Long id, String nombre, Almacen almacen){
		this.id=id;
		this.nombre=nombre;
		this.almacen=almacen;
	}
}
	
