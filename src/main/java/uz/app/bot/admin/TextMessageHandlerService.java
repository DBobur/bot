package uz.app.bot.admin;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import uz.app.service.model.Category;
import uz.app.service.model.Product;
import uz.app.service.model.User;
import uz.app.service.model.enums.QuantityUnit;
import uz.app.service.service.CategoryService;
import uz.app.service.service.ProductService;
import uz.app.service.service.UserService;

import java.util.List;

import static uz.app.bot.admin.AdminBotState.*;
import static uz.app.service.model.enums.UserState.ADD_CATEGORY;

public class TextMessageHandlerService {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final AdminBotExecutor executor;
    private final AdminCacheService adminCacheService;

    public TextMessageHandlerService(CategoryService categoryService,
                                     ProductService productService,
                                     UserService userService,
                                     AdminBotExecutor executor,
                                     AdminCacheService adminCacheService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.executor = executor;
        this.adminCacheService = adminCacheService;
    }


    public SendMessage handleTextMessage(Long chatId, User user, Message message) {
        return switch (adminCacheService.getBotState(chatId)) {
            // add category
            case ADD_CATEGORY_NAME -> handleAddCategoryName(chatId, user, message.getText());
            case ADD_CATEGORY_DESCRIPTION -> handleAddCategoryDescription(chatId, user,message.getText());
            // add product
            case ADD_PRODUCT_NAME -> handleAddProductName(chatId, message.getText());
            case ADD_PRODUCT_DESCRIPTION -> handleAddProductDescription(chatId, message.getText());
            case ADD_PRODUCT_IMAGE -> handleAddProductImage(message);
            case ADD_PRODUCT_PRICE -> handleAddProductPrice(chatId, message.getText());
            case ADD_PRODUCT_QUANTITY_UNIT -> handleAddQuantityUnit(chatId, message.getText());
            case ADD_PRODUCT_QUANTITY -> handleAddQuantity(chatId, message.getText());
            // edit category
            case EDIT_CATEGORY_NAME -> handleEditCategoryName(chatId, message.getText());
            case EDIT_CATEGORY_DESCRIPTION -> handleEditCategoryDescription(chatId, message.getText());
            default -> new SendMessage(chatId.toString(), "❗ Unknown state!");
        };
    }

    private SendMessage handleAddProductPrice(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        try {
            double price = Double.parseDouble(text);
            adminCacheService.getProductTemplate(chatId).setPrice(price);
            adminCacheService.setBotState(chatId, ADD_PRODUCT_QUANTITY_UNIT);
            sendMessage.setText("📏 Enter *quantity unit* (e.g., KG, LITER, PIECE):");
        } catch (NumberFormatException e) {
            sendMessage.setText("❌ Invalid price. Please enter a number.");
        }
        return sendMessage;
    }

    private SendMessage handleAddProductImage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (message.hasPhoto()) {
            List<PhotoSize> photos = message.getPhoto();
            String fileId = photos.get(photos.size() - 1).getFileId();
            adminCacheService.getProductTemplate(message.getChatId()).setImage(fileId);
            adminCacheService.setBotState(message.getChatId(), ADD_PRODUCT_PRICE);
            sendMessage.setText("💰 Enter *product price* (numbers only):");
        } else {
            sendMessage.setText("❌ Please send a photo.");
        }
        return sendMessage;
    }

    private SendMessage handleAddProductDescription(Long chatId, String text) {
        adminCacheService.getProductTemplate(chatId).setDescription(text);
        adminCacheService.setBotState(chatId, ADD_PRODUCT_IMAGE);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("\uD83D\uDCF7 Please send a *product image*:");
        return sendMessage;
    }

    private SendMessage handleAddProductName(Long chatId, String text) {
        adminCacheService.getProductTemplate(chatId).setName(text);
        adminCacheService.setBotState(chatId, ADD_PRODUCT_DESCRIPTION);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("\uD83E\uDDFE Enter *product description*:");
        return sendMessage;

    }

    private SendMessage handleAddProduct(Long chatId, String text) {
        return null;
    }

    private SendMessage handleAddCategoryDescription(Long chatId, User user, String text) {
        adminCacheService.getCategoryTemplate(chatId);
        adminCacheService.removeBotState(chatId);
        adminCacheService.removeCategoryTemplate(chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("✅ Category successfully added!");
        return sendMessage;
    }

    private SendMessage handleAddCategoryName(Long chatId, User user, String text) {
        adminCacheService.getCategoryTemplate(chatId).setName(text);
        adminCacheService.getCategoryTemplate(chatId).setCreatedByUserId(user.getId());
        adminCacheService.setBotState(chatId, ADD_CATEGORY_DESCRIPTION);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText("Please enter the *category description*:");
        return sendMessage;
    }


    private SendMessage handleAddQuantityUnit(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        try {
            QuantityUnit unit = QuantityUnit.valueOf(text.toUpperCase());
            adminCacheService.getProductTemplate(chatId).setQuantityUnit(unit);
            adminCacheService.setBotState(chatId, ADD_PRODUCT_QUANTITY);
            sendMessage.setText("🔢 Enter *quantity* (e.g., 1, 5.5, etc.):");
        } catch (Exception e) {
            sendMessage.setText("❌ Invalid unit. Use one of: KG, LITER, PIECE, etc.");
        }
        return sendMessage;
    }

    private SendMessage handleAddQuantity(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        try {
            double quantity = Double.parseDouble(text);
            Product productTemplate = adminCacheService.getProductTemplate(chatId);
            productTemplate.setQuantity(quantity);
            productService.addProduct(productTemplate);

            adminCacheService.removeProductTemplate(chatId);
            adminCacheService.removeBotState(chatId);
            sendMessage.setText("✅ Category successfully added!");
        } catch (NumberFormatException e) {
            sendMessage.setText("❌ Invalid quantity. Please enter a number.");
        }
        return sendMessage;
    }

    private SendMessage handleEditCategoryName(Long chatId, String text) {
        Category categoryTemplate = adminCacheService.getCategoryTemplate(chatId);
        categoryTemplate.setName(text);

        categoryService.updateCategory(categoryTemplate);
        adminCacheService.removeCategoryTemplate(chatId);
        adminCacheService.removeBotState(chatId);

        return new SendMessage(chatId.toString(), "✅ Category name updated successfully!");
    }

    private SendMessage handleEditCategoryDescription(Long chatId, String text) {
        Category categoryTemplate = adminCacheService.getCategoryTemplate(chatId);
        categoryTemplate.setDescription(text);

        categoryService.updateCategory(categoryTemplate);
        adminCacheService.removeBotState(chatId);
        adminCacheService.removeCategoryTemplate(chatId);

        return new SendMessage(chatId.toString(), "✅ Category description updated successfully!");
    }
}