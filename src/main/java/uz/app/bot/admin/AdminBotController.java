package uz.app.productbot.bot.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.app.productbot.bot.BotUtil;
import uz.app.productbot.shop.entity.UserEntity;
import uz.app.productbot.shop.service.UserService;



public class AdminBotController {

    private final AdminBotExecutor executor = AdminBotExecutor.getInstance();

    public void handle(Update update) {
        // userService ni ishlatish mumkin
    }
}

