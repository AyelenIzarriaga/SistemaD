package sistemacaja.com;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CajaBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private MovimientosService movimientosService;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String texto = update.getMessage().getText().toLowerCase();
        Long chatId = update.getMessage().getChatId();

        try {

            if (texto.startsWith("/ingreso")) {
                procesarIngreso(texto, chatId);
                return;
            }

            if (texto.startsWith("/gasto")) {
                procesarGasto(texto, chatId);
                return;
            }

            if (texto.equals("/hoy")) {
                procesarHoy(chatId);
                return;
            }

            if (texto.equals("/mes")) {
                procesarMes(chatId);
                return;
            }

            if (texto.equals("/semana")) {
                procesarSemana(chatId);
                return;
            }

            if (texto.equals("/balance")) {
                procesarBalance(chatId);
                return;
            }

            if (texto.equals("/deshacer")) {
                procesarDeshacer(chatId);
                return;
            }

            enviarHelp(chatId);

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ " + e.getMessage());
        }
    }

    // ======================
    // ====== BOT ==========
    // ======================

    private void procesarIngreso(String texto, Long chatId) {

        try {
            String[] partes = texto.trim().split("\\s+");

            if (partes.length < 3) {
                enviar(chatId, "❌ Uso: /ingreso monto proveedor [detalle] [fecha]");
                return;
            }

            String montoTexto = partes[1]
                    .replace("$", "")
                    .replace(",", ".")
                    .replaceAll("[^0-9.]", "");

            BigDecimal monto = new BigDecimal(montoTexto);

            LocalDate fecha = extraerFechaFinal(texto);
            String proveedorNombre = extraerProveedor(texto);
            String desc = extraerDescripcion(texto);

            OrigenMov origen = texto.contains("casa")
                    ? OrigenMov.CASA
                    : OrigenMov.LOCAL;

            Long idUsuario = 3L;

            movimientosService.crearDesdeBot(
                    movimientoTipo.ENTRADA,
                    monto,
                    proveedorNombre,
                    desc,
                    origen,
                    idUsuario,
                    fecha
            );

            enviar(chatId, "✅ Ingreso: $" + monto + " → " + proveedorNombre +
                    " (" + fecha + ")");

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ " + e.getMessage());
        }
    }

    private void procesarGasto(String texto, Long chatId) {

        try {
            String[] partes = texto.trim().split("\\s+");

            if (partes.length < 3) {
                enviar(chatId, "❌ Uso: /gasto monto proveedor [detalle] [fecha]");
                return;
            }

            String montoTexto = partes[1]
                    .replace("$", "")
                    .replace(",", ".")
                    .replaceAll("[^0-9.]", "");

            BigDecimal monto = new BigDecimal(montoTexto);

            LocalDate fecha = extraerFechaFinal(texto);
            String proveedorNombre = extraerProveedor(texto);
            String desc = extraerDescripcion(texto);

            OrigenMov origen = texto.contains("casa")
                    ? OrigenMov.CASA
                    : OrigenMov.LOCAL;

            Long idUsuario = 3L;

            movimientosService.crearDesdeBot(
                    movimientoTipo.SALIDA,
                    monto,
                    proveedorNombre,
                    desc,
                    origen,
                    idUsuario,
                    fecha
            );

            enviar(chatId, "❌ Gasto: $" + monto + " → " + proveedorNombre +
                    " (" + fecha + ")");

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ " + e.getMessage());
        }
    }

    private void procesarHoy(Long chatId) {

        try {
            Long idUsuario = 3L;

            ResumenCajaResponse r =
                    movimientosService.resumenDia(LocalDate.now(), idUsuario);

            enviar(chatId,
                    "📅 Caja hoy\n" +
                    "Entradas: $" + r.getEntradas() + "\n" +
                    "Salidas: $" + r.getSalidas() + "\n" +
                    "Libre: $" + r.getLibre());

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ Error obteniendo caja de hoy");
        }
    }

    private void procesarMes(Long chatId) {

        try {
            Long idUsuario = 3L;

            ResumenCajaResponse r =
                    movimientosService.resumenMes(LocalDate.now(), idUsuario);

            enviar(chatId,
                    "📆 Caja mes\n" +
                    "Entradas: $" + r.getEntradas() + "\n" +
                    "Salidas: $" + r.getSalidas() + "\n" +
                    "Libre: $" + r.getLibre() + "\n" +
                    "Prom. libre: $" + r.getPromedioLibre() + "\n" +
                    "Prom. recaudación: $" + r.getPromedioRecaudacion());

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ Error obteniendo caja del mes");
        }
    }

    private void procesarSemana(Long chatId) {

        try {
            Long idUsuario = 3L;

            ResumenCajaResponse r =
                    movimientosService.resumenSemana(LocalDate.now(), idUsuario);

            enviar(chatId,
                    "📆 Caja semana\n" +
                    "Entradas: $" + r.getEntradas() + "\n" +
                    "Salidas: $" + r.getSalidas() + "\n" +
                    "Libre: $" + r.getLibre() + "\n" +
                    "Prom. libre: $" + r.getPromedioLibre() + "\n" +
                    "Prom. recaudación: $" + r.getPromedioRecaudacion());

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ Error obteniendo caja semanal");
        }
    }

    private void procesarBalance(Long chatId) {

        try {
            Long idUsuario = 3L;

            ResumenCajaResponse r =
                    movimientosService.resumenMes(LocalDate.now(), idUsuario);

            enviar(chatId,
                    "📊 Balance mensual\n" +
                    "Entradas: $" + r.getEntradas() + "\n" +
                    "Salidas: $" + r.getSalidas() + "\n" +
                    "Libre: $" + r.getLibre() + "\n" +
                    "Prom. libre: $" + r.getPromedioLibre() + "\n" +
                    "Prom. recaudación: $" + r.getPromedioRecaudacion());

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ Error obteniendo balance");
        }
    }

    private void procesarDeshacer(Long chatId) {

        try {
            Long idUsuario = 3L;

            Movimientos m = movimientosService.deshacerUltimo(idUsuario);

            enviar(chatId,
                    "↩ Deshecho:\n" +
                    m.getMovimiento() + " $" + m.getMonto() +
                    " → " + m.getProveedor().getNombre() +
                    " (" + m.getFechaMovimiento() + ")");

        } catch (Exception e) {
            e.printStackTrace();
            enviar(chatId, "❌ " + e.getMessage());
        }
    }

    private void enviar(Long chatId, String texto) {

        SendMessage msg = new SendMessage(chatId.toString(), texto);

        try {
            execute(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarHelp(Long chatId) {

        enviar(chatId,
                "🤖 Comandos disponibles:\n\n" +
                "/ingreso monto proveedor [detalle] [fecha]\n" +
                "/gasto monto proveedor [detalle] [fecha]\n\n" +
                "/hoy → caja del día\n" +
                "/semana → resumen semanal\n" +
                "/mes → resumen mensual\n" +
                "/balance → balance mensual\n" +
                "/deshacer → deshace el último movimiento");
    }

    // ======================
    // ===== HELPERS =======
    // ======================

    private LocalDate extraerFechaFinal(String texto) {
        String[] partes = texto.trim().split("\\s+");
        String ultimo = partes[partes.length - 1];

        try { return LocalDate.parse(ultimo); } catch (Exception e) {}

        try {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(ultimo, f);
        } catch (Exception e) {}

        if (ultimo.equalsIgnoreCase("ayer")) return LocalDate.now().minusDays(1);
        if (ultimo.equalsIgnoreCase("hoy")) return LocalDate.now();

        return LocalDate.now();
    }

    private String extraerProveedor(String texto) {
        String[] partes = texto.trim().split("\\s+");
        if (partes.length < 3) return "general";
        return partes[2];
    }

    private String extraerDescripcion(String texto) {
        String[] partes = texto.trim().split("\\s+");

        if (partes.length <= 3) return "";

        String ultimo = partes[partes.length - 1];
        boolean hayFecha =
                ultimo.matches("\\d{4}-\\d{2}-\\d{2}") ||
                ultimo.matches("\\d{2}/\\d{2}/\\d{4}") ||
                ultimo.equalsIgnoreCase("ayer") ||
                ultimo.equalsIgnoreCase("hoy");

        int desde = 3;
        int hasta = hayFecha ? partes.length - 1 : partes.length;

        if (desde >= hasta) return "";

        return String.join(" ",
                java.util.Arrays.copyOfRange(partes, desde, hasta));
    }
}
