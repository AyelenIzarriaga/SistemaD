package sistemacaja.com;

public class CriterioProveedorValido implements Criterio {

    public boolean cumple(Movimientos m) {
        return m.getProveedor() != null;
    }
}
