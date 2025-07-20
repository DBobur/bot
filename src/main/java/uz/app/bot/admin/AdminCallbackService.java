package uz.app.bot.admin;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.app.service.model.Category;
import uz.app.service.model.Product;
import uz.app.service.service.CategoryService;
import uz.app.service.service.ProductService;
import uz.app.service.service.UserService;

import java.util.List;
import java.util.Map;


public class AdminCallbackService {

    // DB service
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    // Cache service
    private final AdminBotExecutor executor;
    private final AdminCacheService adminCacheService;

    // All args constructor
    public AdminCallbackService(CategoryService categoryService,
                                ProductService productService,
                                UserService userService,
                                AdminBotExecutor executor,
                                AdminCacheService cacheService
                               ) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.executor = executor;
        this.adminCacheService = cacheService;
    }

    // Create product area
    public void handleCategorySelection(Update update) {
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        long categoryId = Long.parseLong(data.split(":")[1]);
        // create product
        Product product = new Product();
        product.setCategoryId(categoryId);
        product.setUserId(userService.getUserByChatId(chatId).getId());
        adminCacheService.setProductTemplate(chatId, product);
        // update bot state for product add name
        adminCacheService.setBotState(chatId, AdminBotState.ADD_PRODUCT_NAME);
        // execute enter name request
        SendMessage sendMessage = new SendMessage(chatId.toString(), "Please enter the *product name*:");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        executor.execute(sendMessage);
        // remove inline buttons
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(chatId);
        editMarkup.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMarkup.setReplyMarkup(null);
        // execute
        executor.execute(editMarkup);
    }

    // View category products
    public void handleViewCategory(Update update) {
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        long categoryId = Long.parseLong(data.substring("VIEW_CATEGORY_".length()));
        Category category = categoryService.getCategoryById(categoryId);
        List<Product> products = productService.getProductsByCategoryId(categoryId);
        SendMessage sendMessage = new SendMessage(chatId.toString(),
                products.isEmpty()
                        ? "❌ No products found in *" + category.getName() + "* category."
                        : "🧾 Products in *" + category.getName() + "* category:"
        );
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        // check category products list
        if (!products.isEmpty()) {
            sendMessage.setReplyMarkup(AdminBotUtil.buildProductInlineKeyboard(products));
        }
        // execute
        executor.execute(sendMessage);
    }

    // Edit category name area
    public void handleEditCategoryName(Update update) {
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long categoryId = Long.valueOf(data.split("_")[3]);
        // get and put category inside category template
        adminCacheService.setCategoryTemplate(chatId, categoryService.getCategoryById(categoryId));
        // edit bot state
        adminCacheService.setBotState(chatId, AdminBotState.EDIT_CATEGORY_NAME);
        // create and execute send message
        SendMessage sendMessage = new SendMessage(chatId.toString(), "✏️ Enter new *name* for the category:");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        executor.execute(sendMessage);
    }

    // Edit category description area
    public void handleEditCategoryDescription(Update update) {
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long categoryId = Long.valueOf(data.split("_")[3]);
        // get and put category inside category template
        adminCacheService.setCategoryTemplate(chatId, categoryService.getCategoryById(categoryId));
        adminCacheService.setBotState(chatId, AdminBotState.EDIT_CATEGORY_DESCRIPTION);
        // create and execute send message
        SendMessage sendMessage = new SendMessage(chatId.toString(), "📝 Enter new *description* for the category:");
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        executor.execute(sendMessage);
    }

    // Create edit category area
    public void handleEditCategory(Update update) {
        String data = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Long categoryId = Long.valueOf(data.split("_")[2]);

        Category category = categoryService.getCategoryById(categoryId);
        adminCacheService.setCategoryTemplate(chatId, categoryService.getCategoryById(categoryId));
        adminCacheService.setBotState(chatId, AdminBotState.EDIT_CATEGORY_DESCRIPTION);

        SendMessage editMsg = new SendMessage(chatId.toString(),
                "What would you like to edit for *" + category.getName() + "*?"
        );
        editMsg.setParseMode(ParseMode.MARKDOWN);
        editMsg.setReplyMarkup(AdminBotUtil.getEditCategoryOptions(categoryId));
        executor.execute(editMsg);
    }
}
