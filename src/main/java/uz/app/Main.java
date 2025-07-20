package uz.app;

import uz.app.bot.admin.AdminBotExecutor;
import uz.app.bot.client.ClientBotExecutor;

public class Main {
    public static void main(String[] args) {
        AdminBotExecutor.getInstance();
        ClientBotExecutor.getInstance();
    }
}