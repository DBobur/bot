package uz.app.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.app.service.model.Category;

import java.util.ArrayList;
import java.util.List;

public class BotUtil {

    public static ReplyKeyboardMarkup getContactRequestKeyboard() {
        //todo: text
        KeyboardButton contactButton = new KeyboardButton("📱 Raqamni yuborish");
        contactButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(List.of(row));
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);
        return markup;
    }


    public static InlineKeyboardMarkup getCategoryInlineButtons(List<Category> categories) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Category category : categories) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(category.getName());
            button.setCallbackData("CATEGORY_ID:" + category.getId());

            rows.add(List.of(button));
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }
}
