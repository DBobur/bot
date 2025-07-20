package uz.app.productbot.bot.admin;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.app.productbot.bot.Main;
import uz.app.productbot.shop.service.UserService;

public class AdminBotExecutor {

    private static AdminBotExecutor instance;
    private final AdminBot bot;

    private AdminBotExecutor() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            this.bot = Main.bot;
            api.registerBot(bot);
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

    public <T extends BotApiMethodMessage> void execute(T method) throws TelegramApiException {
        bot.execute(method);
    }
}

