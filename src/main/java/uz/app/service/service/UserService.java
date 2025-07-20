package uz.app.service.service;

import uz.app.service.model.User;
import uz.app.service.model.enums.UserState;
import uz.app.service.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerNewUser(User user) {
        if (userRepository.findByChatId(user.getChatId()).isPresent()) {
            throw new RuntimeException("User already exists with chatId: " + user.getChatId());
        }
        userRepository.save(user);
    }

    public void changeUserState(long chatId, UserState newState) {
        User user = userRepository.findByChatId(chatId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserState(newState);
        userRepository.update(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByChatId(Long chatId) {
        return userRepository.findByChatId(chatId).orElse(null);
    }
}

