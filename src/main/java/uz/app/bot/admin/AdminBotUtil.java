package uz.app.bot.admin;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.app.service.model.Category;
import uz.app.service.model.Product;
import uz.app.service.service.CategoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminBotUtil {

    /**
     * Creates an inline keyboard showing categories with edit and delete options.
     *
     * @param categoryService the service to fetch categories
     * @return InlineKeyboardMarkup for the admin categories view
     */
    public static InlineKeyboardMarkup buildCategoryInlineKeyboard(CategoryService categoryService) {
        List<Category> categories = categoryService.getAllCategories(); // Fetch from DB
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Category category : categories) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            // 1. Category button
            InlineKeyboardButton catButton = new InlineKeyboardButton();
            catButton.setText("📁 " + category.getName());
            catButton.setCallbackData("VIEW_CATEGORY_" + category.getId());

            // 2. Edit button
            InlineKeyboardButton editButton = new InlineKeyboardButton();
            editButton.setText("✏️");
            editButton.setCallbackData("EDIT_CATEGORY_" + category.getId());

            // 3. Delete button
            InlineKeyboardButton deleteButton = new InlineKeyboardButton();
            deleteButton.setText("❌");
            deleteButton.setCallbackData("DELETE_CATEGORY_" + category.getId());

            row.add(catButton);
            row.add(editButton);
            row.add(deleteButton);
            rows.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    public static ReplyKeyboard getAdminMenuButtons() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("➕ Add product");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("📂 Add category");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("🛠 Manage Categories and Products 📂");

        KeyboardRow row4 = new KeyboardRow();
        row4.add("🛠 Manage orders");

        KeyboardRow row5 = new KeyboardRow();
        row5.add("📊 Get statistics");

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setKeyboard(List.of(row1, row2, row3, row4, row5));

        return markup;
    }

    public static InlineKeyboardMarkup buildProductInlineKeyboard(List<Product> products) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Product product : products) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            // 1. Product nomi (lekin bosilmaydi, faqat ko‘rinadi)
            InlineKeyboardButton nameButton = new InlineKeyboardButton();
            nameButton.setText("📦 " + product.getName());
            nameButton.setCallbackData("DETAILS"); // yoki null

            // 2. Edit
            InlineKeyboardButton editButton = new InlineKeyboardButton();
            editButton.setText("✏️");
            editButton.setCallbackData("EDIT_PRODUCT_" + product.getId());

            // 3. Delete
            InlineKeyboardButton deleteButton = new InlineKeyboardButton();
            deleteButton.setText("❌");
            deleteButton.setCallbackData("DELETE_PRODUCT_" + product.getId());

            row.add(nameButton);
            row.add(editButton);
            row.add(deleteButton);
            rows.add(row);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    public static InlineKeyboardMarkup getEditCategoryOptions(Long categoryId) {
        InlineKeyboardButton nameBtn = new InlineKeyboardButton("📝 Edit Name");
        nameBtn.setCallbackData("EDIT_CATEGORY_NAME_" + categoryId);

        InlineKeyboardButton descBtn = new InlineKeyboardButton("📝 Edit Description");
        descBtn.setCallbackData("EDIT_CATEGORY_DESCRIPTION_" + categoryId);

        List<InlineKeyboardButton> row = Arrays.asList(nameBtn, descBtn);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(row);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

}