package sistemacaja.com;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResumenCajaResponse {

    private LocalDate fecha;
    private BigDecimal entradas;
    private BigDecimal salidas;
    private BigDecimal libre;
    private BigDecimal promedioLibre;
    private BigDecimal promedioRecaudacion;

    public ResumenCajaResponse(
            LocalDate fecha,
            BigDecimal entradas,
            BigDecimal salidas,
            BigDecimal libre,
            BigDecimal promedioLibre,
            BigDecimal promedioRecaudacion
    ) {
        this.fecha = fecha;
        this.entradas = entradas;
        this.salidas = salidas;
        this.libre = libre;
        this.promedioLibre = promedioLibre;
        this.promedioRecaudacion = promedioRecaudacion;
    }

    public LocalDate getFecha() { return fecha; }
    public BigDecimal getEntradas() { return entradas; }
    public BigDecimal getSalidas() { return salidas; }
    public BigDecimal getLibre() { return libre; }
    public BigDecimal getPromedioLibre() { return promedioLibre; }
    public BigDecimal getPromedioRecaudacion() { return promedioRecaudacion; }
}
