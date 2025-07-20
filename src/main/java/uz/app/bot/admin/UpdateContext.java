package uz.app.bot.admin;

public class UpdateContext {
    private final Long chatId;
    private final Integer messageId;
    private final String data;

    public UpdateContext(Long chatId, Integer messageId, String data) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.data = data;
    }

    public Long getChatId() {
        return chatId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public String getData() {
        return data;
    }
}
