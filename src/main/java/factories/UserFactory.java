package factories;

import models.User;

public class UserFactory {

    public static User getUser(String name, String email, String gender, String status) {

        return User.builder()
                .email(email)
                .name(name)
                .gender(gender)
                .status(status)
                .build();
    }
}
