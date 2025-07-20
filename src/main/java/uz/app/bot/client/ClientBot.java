package uz.app.bot.client;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientBot extends TelegramLongPollingBot {

    private final String botUsername;

    public ClientBot(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final ThreadLocal<ClientBotController> botController =
            ThreadLocal.withInitial(ClientBotController::new);

    @Override
    public void onUpdateReceived(Update update) {
        CompletableFuture.runAsync(() -> {
            botController.get().handle(update);
        }, executorService);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }
}

