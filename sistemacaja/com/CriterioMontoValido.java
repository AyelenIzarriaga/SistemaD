package sistemacaja.com;

import java.math.BigDecimal;

public class CriterioMontoValido implements Criterio {

    public boolean cumple(Movimientos m){
       return m.getMonto() != null 
    		&& m.getMonto().compareTo(BigDecimal.ZERO) > 0;


    }
}
