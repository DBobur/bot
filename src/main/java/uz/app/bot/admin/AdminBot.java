package uz.app.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminBot extends TelegramLongPollingBot {

    private final String botUsername;

    public AdminBot(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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

