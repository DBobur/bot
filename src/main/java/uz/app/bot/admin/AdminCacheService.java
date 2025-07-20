package uz.app.bot.admin;

import uz.app.service.model.Category;
import uz.app.service.model.Product;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdminCacheService {

    private static final Map<Long, AdminBotState> botStates = new ConcurrentHashMap<>();
    private static final Map<Long, Category> categoryTemplate = new ConcurrentHashMap<>();
    private static final Map<Long, Product> productTemplate = new ConcurrentHashMap<>();

    // === Bot State ===
    public void setBotState(Long chatId, AdminBotState state) {
        botStates.put(chatId, state);
    }

    public AdminBotState getBotState(Long chatId) {
        return botStates.get(chatId);
    }

    public void removeBotState(Long chatId) {
        botStates.remove(chatId);
    }

    // === Category Template ===
    public void setCategoryTemplate(Long chatId, Category category) {
        categoryTemplate.put(chatId, category);
    }

    public Category getCategoryTemplate(Long chatId) {
        return categoryTemplate.get(chatId);
    }

    public void removeCategoryTemplate(Long chatId) {
        categoryTemplate.remove(chatId);
    }

    // === Product Template ===
    public void setProductTemplate(Long chatId, Product product) {
        productTemplate.put(chatId, product);
    }

    public Product getProductTemplate(Long chatId) {
        return productTemplate.get(chatId);
    }

    public void removeProductTemplate(Long chatId) {
        productTemplate.remove(chatId);
    }
}
