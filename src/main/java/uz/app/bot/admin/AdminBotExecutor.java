package uz.app.bot.admin;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class AdminBotExecutor {

    private static AdminBotExecutor instance;
    private final AdminBot bot;

    private AdminBotExecutor() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            // todo create bot
            this.bot = new AdminBot(
                    "7900636835:AAG_OaE5m_dVcf7j3UCoaBQ_9WvFrR79iDE",
                    "@product_admin2025_bot"
            );
            api.registerBot(bot);
            System.out.println("admin bot is ready");
        } catch (Exception e) {
            throw new RuntimeException("Failed to register bot", e);
        }
    }

    public static synchronized AdminBotExecutor getInstance() {
        if (instance == null) {
            instance = new AdminBotExecutor();
        }
        return instance;
    }

    @SneakyThrows
    public void execute(BotApiMethod<?> method){
        bot.execute(method);
    }
}

