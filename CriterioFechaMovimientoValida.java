package sistemacaja.com;

import java.time.LocalDate;


public class CriterioFechaMovimientoValida implements Criterio {

    public boolean cumple(Movimientos m){
        return m.getFechaMovimiento() != null
            && !m.getFechaMovimiento().isAfter(LocalDate.now());
    }
}
