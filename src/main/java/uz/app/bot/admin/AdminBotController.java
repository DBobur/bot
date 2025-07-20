package uz.app.bot.admin;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.bot.BotUtil;
import uz.app.bot.Util;
import uz.app.service.model.Category;
import uz.app.service.model.Product;
import uz.app.service.model.User;
import uz.app.service.model.enums.QuantityUnit;
import uz.app.service.model.enums.UserRole;
import uz.app.service.repository.CategoryRepository;
import uz.app.service.repository.DbConnection;
import uz.app.service.repository.ProductRepository;
import uz.app.service.repository.UserRepository;
import uz.app.service.service.CategoryService;
import uz.app.service.service.ProductService;
import uz.app.service.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static uz.app.bot.admin.AdminBotState.*;
import static uz.app.service.model.enums.UserState.*;


public class AdminBotController {

    private final AdminBotExecutor executor = AdminBotExecutor.getInstance();
    private final UserService userService = new UserService(new UserRepository(DbConnection.getConnection()));
    private final CategoryService categoryService = new CategoryService(new CategoryRepository(DbConnection.getConnection()));
    private final ProductService productService = new ProductService(new ProductRepository(DbConnection.getConnection()));

    private final AdminCacheService cacheService = new AdminCacheService();

    private final AdminCallbackService adminCallbackService = new AdminCallbackService(
            categoryService,
            productService,
            userService,
            executor,
            cacheService
    );
    private final TextMessageHandlerService textMessageHandlerService = new TextMessageHandlerService(
            categoryService,
            productService,
            userService,
            executor,
            cacheService
    );



    public void handle(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            User user = userService.getUserByChatId(chatId);
            if (user == null && handleUnregisteredUser(message)) return;

            if (handleAdminMenuButtons(message, user)) return;

            if (handleBotState(message, user)) return;

            if (handleUserStateFallback(message, user)) return;

            SendMessage unknown = new SendMessage(chatId.toString(), "⚠ Unknown command or state.");
            executor.execute(unknown);
        }else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();

            if (data.startsWith("CATEGORY_ID:")) {
                adminCallbackService.handleCategorySelection(update);
            } else if (data.startsWith("VIEW_CATEGORY_")) {
                adminCallbackService.handleViewCategory(update);
            } else if (data.startsWith("EDIT_CATEGORY_NAME_")) {
                adminCallbackService.handleEditCategoryName(update);
            } else if (data.startsWith("EDIT_CATEGORY_DESCRIPTION_")) {
                adminCallbackService.handleEditCategoryDescription(update);
            } else if (data.startsWith("EDIT_CATEGORY_")) {
                adminCallbackService.handleEditCategory(update);
            }
        }
    }

    private boolean handleUnregisteredUser(Message message) {
        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (message.getContact() != null) {
            Contact contact = message.getContact();
            User user = User.builder()
                    .firstName(contact.getFirstName())
                    .lastName(contact.getLastName())
                    .number(contact.getPhoneNumber())
                    .role(UserRole.ADMIN_ROLE)
                    .chatId(chatId)
                    .userState(REGISTERED)
                    .build();
            userService.registerNewUser(user);
            sendMessage.setText("Muoffaqqiyatli ro'yhatdan o'tdingiz!");
            sendMessage.setReplyMarkup(AdminBotUtil.getAdminMenuButtons());
        } else {
            sendMessage.setText("Iltimos, ro'yhatdan o'ting!");
            sendMessage.setReplyMarkup(BotUtil.getContactRequestKeyboard());
        }

        executor.execute(sendMessage);
        return true;
    }

    private boolean handleAdminMenuButtons(Message message, User user) {
        String text = message.getText();
        Long chatId = message.getChatId();

        if (!Util.buttonMessages().contains(text)) return false;

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        switch (text) {
            case "📂 Add category" -> {
                Category category = new Category();
                cacheService.setCategoryTemplate(chatId, category);
                cacheService.setBotState(chatId, AdminBotState.ADD_CATEGORY_NAME);
                sendMessage.setText("Please enter the *category name*:");
            }
            case "➕ Add product" -> {
                Product product = new Product();
                cacheService.setProductTemplate(chatId, product);
                cacheService.setBotState(chatId, AdminBotState.ADD_PRODUCT_NAME);
                List<Category> categories = categoryService.getAllCategories();
                sendMessage.setText("Please choose a *category*:");
                sendMessage.setReplyMarkup(BotUtil.getCategoryInlineButtons(categories));
            }
            case "🛠 Manage Categories and Products 📂" -> {
                cacheService.setBotState(chatId, AdminBotState.MANAGE_CATEGORIES_AND_PRODUCTS);
                user.setUserState(MANAGE_CP);
                sendMessage.setText("📂 Categories and Products:");
                sendMessage.setReplyMarkup(AdminBotUtil.buildCategoryInlineKeyboard(categoryService));
            }
        }

        executor.execute(sendMessage);
        return true;
    }

    private boolean handleBotState(Message message, User user) {
        Long chatId = message.getChatId();
        AdminBotState state = cacheService.getBotState(chatId);
        if (state == null) return false;

        SendMessage sendMessage = textMessageHandlerService.handleTextMessage(
                chatId,
                user,
                message
        );
        executor.execute(sendMessage);
        return true;
    }

    private boolean handleUserStateFallback(Message message, User user) {
        Long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        switch (user.getUserState()) {
            case REGISTERED -> {
                sendMessage.setText("Feel free to use");
                sendMessage.setReplyMarkup(AdminBotUtil.getAdminMenuButtons());
            }
            default -> {
                return false;
            }
        }

        executor.execute(sendMessage);
        return true;
    }

}

