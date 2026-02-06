package sistemacaja.com;
   
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {Optional<Proveedor> findByNombreAndAlmacenId(String nombre, Long almacenId);
};
