package uz.app.bot.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class UpdateContext {
    private final Update update;
    private final Long chatId;
    private final Integer messageId;
    private final String data;
    public UpdateContext(Update update) {
        this.update = update;
        this.data = update.getCallbackQuery().getData();
        this.chatId = update.getCallbackQuery().getMessage().getChatId();
        this.messageId = update.getCallbackQuery().getMessage().getMessageId();
    }

}
