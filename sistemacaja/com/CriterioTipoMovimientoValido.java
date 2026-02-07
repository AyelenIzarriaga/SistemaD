package sistemacaja.com;

public class CriterioTipoMovimientoValido implements Criterio {
    public boolean cumple(Movimientos m) {
        return m.getMovimiento() != null;
    }
}