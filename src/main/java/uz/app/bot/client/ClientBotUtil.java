package uz.app.bot.client;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class ClientBotUtil {
    public static ReplyKeyboard getClientMenuButtons() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("🛒 Mahsulotlar");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("📦 Buyurtmalarim");
        row2.add("📜 Tarixim");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("💰 Balans to‘ldirish");

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(List.of(row1, row2, row3));

        return markup;
    }
}
