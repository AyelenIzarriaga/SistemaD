package sistemacaja.com;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimientos")


public class MovimientosController {

    private final MovimientosService movimientosService;

    public MovimientosController(MovimientosService movimientosService) {
        this.movimientosService = movimientosService;
    }

    @PostMapping
   public MovimientosResponse crear(@RequestBody CrearMovimientoRequest request) {
    	return movimientosService.crearMovimiento(request, request.getIdUsuario());
    }


   @GetMapping
    public List<MovimientosResponse> listar(@RequestParam Long idUsuario) {
    	return movimientosService.listar(idUsuario);
    }

  @GetMapping("/resumen-dia")
public ResumenCajaResponse resumenDia(
        @RequestParam
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fecha,
        @RequestParam Long idUsuario
) {
    return movimientosService.resumenDia(fecha, idUsuario);
}

   @GetMapping("/resumen-semana")
    public ResumenCajaResponse resumenSemana(
        @RequestParam 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fecha,
	@RequestParam Long idUsuario
    ) {
    return movimientosService.resumenSemana(fecha,idUsuario);
}

   @GetMapping("/resumen-mes")
    public ResumenCajaResponse resumenMes(
        @RequestParam 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fecha,
	@RequestParam Long idUsuario
   ) {
    return movimientosService.resumenMes(fecha,idUsuario);
}


}

