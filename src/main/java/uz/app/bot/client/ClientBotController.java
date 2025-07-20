package uz.app.bot.client;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.app.bot.BotUtil;
import uz.app.service.model.User;
import uz.app.service.model.enums.UserRole;
import uz.app.service.repository.DbConnection;
import uz.app.service.repository.UserRepository;
import uz.app.service.service.UserService;

import static uz.app.service.model.enums.UserState.*;


public class ClientBotController {

    private final ClientBotExecutor executor = ClientBotExecutor.getInstance();
    private final UserService userService = new UserService(new UserRepository(DbConnection.getConnection()));

    public void handle(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            User user = userService.getUserByChatId(chatId);

            if(user == null){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                if (message.getContact() != null) {
                    Contact contact = message.getContact();
                    user = User.builder()
                            .firstName(contact.getFirstName())
                            .lastName(contact.getLastName())
                            .number(contact.getPhoneNumber())
                            .role(UserRole.ADMIN_ROLE)
                            .chatId(chatId)
                            .userState(REGISTERED)
                            .build();
                    userService.registerNewUser(user);
                    // ✅ Muvaffaqiyatli ro‘yxatdan o‘tganligi haqida xabar
                    sendMessage.setText("Muoffaqqiyatli ro'yhatdan o'tdingiz!");
                    // ✅ Tugmalarni o‘rnatish
                    sendMessage.setReplyMarkup(ClientBotUtil.getClientMenuButtons());
                } else {
                    // ❌ Agar contact null bo‘lsa, ro‘yxatdan o‘tishni so‘rash
                    sendMessage.setText("Iltimos, ro'yhatdan o'ting!");
                    sendMessage.setReplyMarkup(BotUtil.getContactRequestKeyboard());
                }
                executor.execute(sendMessage);
                return;
            }

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            switch (user.getUserState()){
                case REGISTERED -> {
                    sendMessage.setText("Feel free to use\n");
                    sendMessage.setReplyMarkup(ClientBotUtil.getClientMenuButtons());
                }
            }
            executor.execute(sendMessage);
        }else if (update.hasCallbackQuery()) {

        }
    }
}

