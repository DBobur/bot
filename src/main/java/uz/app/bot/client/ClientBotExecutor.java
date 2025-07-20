package uz.app.bot.client;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class ClientBotExecutor {

    private static ClientBotExecutor instance;
    private final ClientBot bot;

    private ClientBotExecutor() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            // todo create bot
            this.bot = new ClientBot(
                    "8151950236:AAFEZ1GtCuAIUQ8ymqG9Kf1z02Z7cJSd-O8",
                    "@product_client2025_bot"
            );
            api.registerBot(bot);
            System.out.println("client bot is ready");
        } catch (Exception e) {
            throw new RuntimeException("Failed to register bot", e);
        }
    }

    public static synchronized ClientBotExecutor getInstance() {
        if (instance == null) {
            instance = new ClientBotExecutor();
        }
        return instance;
    }

    @SneakyThrows
    public <T extends BotApiMethodMessage> void execute(T method){
        bot.execute(method);
    }
}

