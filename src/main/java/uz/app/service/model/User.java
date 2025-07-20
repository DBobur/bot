package uz.app.service.model;

import lombok.*;
import uz.app.service.model.enums.UserRole;
import uz.app.service.model.enums.UserState;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private long chatId;
    private String firstName;
    private String lastName;
    private String bio;
    private String number;
    private UserRole role;
    private UserState userState;
}
