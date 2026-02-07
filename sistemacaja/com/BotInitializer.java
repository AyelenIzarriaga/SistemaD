package sistemacaja.com;



import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;

@Configuration
public class BotInitializer {

    private final CajaBot cajaBot;

    public BotInitializer(CajaBot cajaBot) {
        this.cajaBot = cajaBot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(cajaBot);
            System.out.println("🤖 Bot iniciado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
