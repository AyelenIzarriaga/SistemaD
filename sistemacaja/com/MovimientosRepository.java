package sistemacaja.com;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {

    @Query("""
        select m
        from Movimientos m
        where m.fechaMovimiento = :fecha
          and m.almacen.id = :idAlmacen
    """)
    List<Movimientos> findByFechaMovimiento(
            @Param("fecha") LocalDate fecha,
            @Param("idAlmacen") Long idAlmacen
    );

    @Query("""
        select m
        from Movimientos m
        where m.fechaMovimiento between :desde and :hasta
          and m.almacen.id = :idAlmacen
    """)
    
    List<Movimientos> findByRango(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("idAlmacen") Long idAlmacen
    );

    List<Movimientos> findByAlmacenId(Long idAlmacen);

    @Query("""
select coalesce(sum(m.monto),0)
from Movimientos m
where m.fechaMovimiento = :fecha
  and m.almacen.id = :idAlmacen
  and m.movimiento = :tipo
""")
BigDecimal sumarPorTipo(
        @Param("fecha") LocalDate fecha,
        @Param("idAlmacen") Long idAlmacen,
        @Param("tipo") movimientoTipo tipo
);

Optional<Movimientos> findTopByAlmacenIdOrderByFechaRegistroDesc(Long almacenId);


}
