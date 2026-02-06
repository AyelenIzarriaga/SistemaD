package sistemacaja.com;

import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;

@Configuration
public class TelegramConfig {

    private final CajaBot cajaBot;

    public TelegramConfig(CajaBot cajaBot) {
        this.cajaBot = cajaBot;
    }

    @PostConstruct
    public void init() throws Exception {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(cajaBot);
        System.out.println("🤖 Bot registrado correctamente");
    }
}
